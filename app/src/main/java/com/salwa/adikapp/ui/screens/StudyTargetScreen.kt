package com.salwa.adikapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salwa.adikapp.util.DateUtils
import com.salwa.adikapp.viewmodel.StudyTargetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyTargetScreen(vm: StudyTargetViewModel = viewModel()) {
    val targets by vm.targets.collectAsState()
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
            items(targets) { target ->
                Card {
                    Column(Modifier.fillMaxWidth().padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(target.title, fontWeight = FontWeight.SemiBold)
                            IconButton(onClick = { vm.deleteTarget(target) }) { Icon(Icons.Filled.Delete, contentDescription = "Hapus") }
                        }
                        if (target.description.isNotBlank()) Text(target.description, style = MaterialTheme.typography.bodyMedium)
                        target.targetDateMillis?.let { Text("Target: ${DateUtils.formatDate(it)}", style = MaterialTheme.typography.bodyMedium) }
                        Spacer(Modifier.height(6.dp))
                        LinearProgressIndicator(progress = { target.progressPercent / 100f }, modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf(25, 50, 75, 100).forEach { p ->
                                AssistChip(onClick = { vm.updateProgress(target, p) }, label = { Text("$p%") })
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddStudyTargetDialog(onDismiss = { showDialog = false }) { title, desc, date ->
            vm.addTarget(title, desc, date)
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStudyTargetDialog(onDismiss: () -> Unit, onSave: (String, String, Long?) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Target Belajar") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul target") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Deskripsi (opsional)") })
            }
        },
        confirmButton = {
            TextButton(onClick = { if (title.isNotBlank()) onSave(title, desc, System.currentTimeMillis() + 14L * 24 * 60 * 60 * 1000) }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
