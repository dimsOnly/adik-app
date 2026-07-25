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
import com.salwa.adikapp.viewmodel.TaskNoteViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskNoteScreen(vm: TaskNoteViewModel = viewModel()) {
    val tasks by vm.tasks.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, contentDescription = "Tambah") }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text(
                "Kamu akan diingatkan otomatis 2 hari sebelum deadline 🔔",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tasks) { task ->
                    Card {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { vm.toggleDone(task) }) {
                                Icon(if (task.isDone) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked, contentDescription = "Selesai")
                            }
                            Column(Modifier.weight(1f)) {
                                Text(task.title, fontWeight = FontWeight.SemiBold, textDecoration = if (task.isDone) TextDecoration.LineThrough else null)
                                if (task.courseName.isNotBlank()) Text(task.courseName, style = MaterialTheme.typography.bodyMedium)
                                Text("Deadline: ${DateUtils.formatDateTime(task.deadlineMillis)}", style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = { vm.deleteTask(task) }) { Icon(Icons.Filled.Delete, contentDescription = "Hapus") }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddTaskDialog(onDismiss = { showDialog = false }) { title, desc, course, deadline ->
            vm.addTask(title, desc, course, deadline)
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTaskDialog(onDismiss: () -> Unit, onSave: (String, String, String, Long) -> Unit) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    // default deadline: 7 hari dari sekarang
    var daysFromNow by remember { mutableStateOf(7) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Note Tugas") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul tugas") })
                OutlinedTextField(value = course, onValueChange = { course = it }, label = { Text("Mata kuliah (opsional)") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Deskripsi (opsional)") })
                Text("Deadline: $daysFromNow hari lagi")
                Slider(
                    value = daysFromNow.toFloat(),
                    onValueChange = { daysFromNow = it.toInt() },
                    valueRange = 1f..30f,
                    steps = 28
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isNotBlank()) {
                    val cal = Calendar.getInstance()
                    cal.add(Calendar.DAY_OF_YEAR, daysFromNow)
                    cal.set(Calendar.HOUR_OF_DAY, 9)
                    cal.set(Calendar.MINUTE, 0)
                    onSave(title, desc, course, cal.timeInMillis)
                }
            }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
