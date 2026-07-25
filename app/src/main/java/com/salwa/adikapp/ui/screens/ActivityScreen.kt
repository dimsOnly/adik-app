package com.salwa.adikapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salwa.adikapp.util.DateUtils
import com.salwa.adikapp.viewmodel.ActivityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(vm: ActivityViewModel = viewModel()) {
    val activities by vm.activities.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, contentDescription = "Tambah") }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(activities) { act ->
                Card {
                    Row(
                        Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { vm.toggleDone(act) }) {
                            Icon(if (act.isDone) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked, contentDescription = "Selesai")
                        }
                        Column(Modifier.weight(1f)) {
                            Text(act.title, fontWeight = FontWeight.SemiBold, textDecoration = if (act.isDone) TextDecoration.LineThrough else null)
                            Text("${DateUtils.formatDate(act.dateMillis)} • ${act.startTime}-${act.endTime}", style = MaterialTheme.typography.bodyMedium)
                            if (act.location.isNotBlank()) Text(act.location, style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { vm.deleteActivity(act) }) { Icon(Icons.Filled.Delete, contentDescription = "Hapus") }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddActivityDialog(onDismiss = { showDialog = false }) { title, start, end, location, note ->
            vm.addActivity(title, System.currentTimeMillis(), start, end, location, note)
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddActivityDialog(onDismiss: () -> Unit, onSave: (String, String, String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Kegiatan") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Nama kegiatan") })
                OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Jam mulai (HH:mm)") })
                OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("Jam selesai (HH:mm)") })
                OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Lokasi (opsional)") })
                OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Catatan (opsional)") })
            }
        },
        confirmButton = {
            TextButton(onClick = { if (title.isNotBlank()) onSave(title, start, end, location, note) }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
