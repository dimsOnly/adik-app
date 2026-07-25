package com.salwa.adikapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salwa.adikapp.viewmodel.WishlistViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(vm: WishlistViewModel = viewModel()) {
    val items by vm.items.collectAsState()
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
            items(items) { item ->
                Card {
                    Row(
                        Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                item.name,
                                fontWeight = FontWeight.SemiBold,
                                textDecoration = if (item.isAchieved) TextDecoration.LineThrough else null
                            )
                            if (item.estimatedPrice > 0) Text(formatRupiah(item.estimatedPrice), style = MaterialTheme.typography.bodyMedium)
                            if (item.note.isNotBlank()) Text(item.note, style = MaterialTheme.typography.bodyMedium)
                        }
                        IconButton(onClick = { vm.toggleAchieved(item) }) {
                            Icon(if (item.isAchieved) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder, contentDescription = "Tercapai")
                        }
                        IconButton(onClick = { vm.deleteItem(item) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Hapus")
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddWishlistDialog(onDismiss = { showDialog = false }) { name, price, priority, note ->
            vm.addItem(name, price, priority, note)
            showDialog = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddWishlistDialog(onDismiss: () -> Unit, onSave: (String, Long, Int, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Wishlist") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nama barang") })
                OutlinedTextField(value = price, onValueChange = { price = it.filter { c -> c.isDigit() } }, label = { Text("Estimasi harga (Rp)") })
                OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Catatan") })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(1 to "Biasa", 2 to "Penting", 3 to "Banget pengen").forEach { (value, label) ->
                        FilterChip(selected = priority == value, onClick = { priority = value }, label = { Text(label) })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { if (name.isNotBlank()) onSave(name, price.toLongOrNull() ?: 0L, priority, note) }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}
