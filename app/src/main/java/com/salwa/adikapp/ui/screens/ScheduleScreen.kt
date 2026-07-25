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
import com.salwa.adikapp.viewmodel.ScheduleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(vm: ScheduleViewModel = viewModel()) {
    val schedules by vm.schedules.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    val grouped = schedules.groupBy { it.dayOfWeek }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, contentDescription = "Tambah") }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (day in 1..7) {
                val list = grouped[day] ?: continue
                item {
                    Text(DateUtils.dayNames[day - 1], fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                }
                items(list) { sch ->
                    Card {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(sch.courseName, fontWeight = FontWeight.SemiBold)
                                Text("${sch.startTime} - ${sch.endTime} • ${sch.room}", style = MaterialTheme.typography.bodyMedium)
                                if (sch.lecturer.isNotBlank()) Text(sch.lecturer, style = MaterialTheme.typography.bodyMedium)
                            }
                            IconButton(onClick = { vm.deleteSchedule(sch) }) { Icon(Icons.Filled.Delete, contentDescription = "Hapus") }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddScheduleDialog(onDismiss = { showDialog = false }) { course, lecturer, room, day, start, end ->
            vm.addSchedule(course, lecturer, room, day, start, end)
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddScheduleDialog(onDismiss: () -> Unit, onSave: (String, String, String, Int, String, String) -> Unit) {
    var course by remember { mutableStateOf("") }
    var lecturer by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var day by remember { mutableStateOf(1) }
    var start by remember { mutableStateOf("08:00") }
    var end by remember { mutableStateOf("10:00") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Jadwal Kuliah") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = course, onValueChange = { course = it }, label = { Text("Mata kuliah") })
                OutlinedTextField(value = lecturer, onValueChange = { lecturer = it }, label = { Text("Dosen (opsional)") })
                OutlinedTextField(value = room, onValueChange = { room = it }, label = { Text("Ruangan") })
                Text("Hari:")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    DateUtils.dayNames.forEachIndexed { idx, name ->
                        FilterChip(selected = day == idx + 1, onClick = { day = idx + 1 }, label = { Text(name.take(3)) })
                    }
                }
                OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Jam mulai (HH:mm)") })
                OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("Jam selesai (HH:mm)") })
            }
        },
        confirmButton = {
            TextButton(onClick = { if (course.isNotBlank()) onSave(course, lecturer, room, day, start, end) }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
