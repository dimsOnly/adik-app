package com.salwa.adikapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.salwa.adikapp.util.DateUtils
import com.salwa.adikapp.util.PhotoStorage
import com.salwa.adikapp.viewmodel.DiaryViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryScreen(vm: DiaryViewModel = viewModel()) {
    val entries by vm.entries.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, contentDescription = "Tambah") }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(entries) { entryWithPhotos ->
                Card {
                    Column(Modifier.padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                            Column {
                                if (entryWithPhotos.entry.title.isNotBlank()) Text(entryWithPhotos.entry.title, fontWeight = FontWeight.SemiBold)
                                Text(DateUtils.formatDate(entryWithPhotos.entry.dateMillis), style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = {
                                vm.deleteEntry(entryWithPhotos.entry, entryWithPhotos.photos.map { it.filePath })
                            }) { Icon(Icons.Filled.Delete, contentDescription = "Hapus") }
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(entryWithPhotos.entry.content)
                        if (entryWithPhotos.photos.isNotEmpty()) {
                            Spacer(Modifier.height(8.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(entryWithPhotos.photos) { photo ->
                                    AsyncImage(
                                        model = File(photo.filePath),
                                        contentDescription = null,
                                        modifier = Modifier.size(90.dp).clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddDiaryDialog(onDismiss = { showDialog = false }) { title, content, mood, photoPaths ->
            vm.addEntry(title, content, mood, System.currentTimeMillis(), photoPaths)
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDiaryDialog(onDismiss: () -> Unit, onSave: (String, String, String, List<String>) -> Unit) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var photoPaths by remember { mutableStateOf(listOf<String>()) }
    var pendingCameraFile by remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            pendingCameraFile?.let { photoPaths = photoPaths + it.absolutePath }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val bytes = context.contentResolver.openInputStream(it)?.readBytes()
            if (bytes != null) {
                val file = PhotoStorage.createNewPhotoFile(context)
                file.writeBytes(bytes)
                photoPaths = photoPaths + file.absolutePath
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tulis Note Harian") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul (opsional)") })
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Cerita hari ini") }, minLines = 3)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = {
                        val file = PhotoStorage.createNewPhotoFile(context)
                        pendingCameraFile = file
                        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                        cameraLauncher.launch(uri)
                    }) {
                        Icon(Icons.Filled.CameraAlt, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Kamera")
                    }
                    OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Icon(Icons.Filled.PhotoLibrary, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Galeri")
                    }
                }

                if (photoPaths.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(photoPaths) { path ->
                            AsyncImage(
                                model = File(path),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (content.isNotBlank()) onSave(title, content, "", photoPaths) }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
