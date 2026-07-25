package com.salwa.adikapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.salwa.adikapp.data.entity.TransactionType
import com.salwa.adikapp.util.DateUtils
import com.salwa.adikapp.viewmodel.FinanceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceScreen(vm: FinanceViewModel = viewModel()) {
    val transactions by vm.transactions.collectAsState()
    val saldo by vm.saldo.collectAsState()
    val pemasukan by vm.totalPemasukan.collectAsState()
    val pengeluaran by vm.totalPengeluaran.collectAsState()
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) { Icon(Icons.Filled.Add, contentDescription = "Tambah") }
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))) {
                Column(Modifier.padding(16.dp)) {
                    Text("Saldo saat ini", style = MaterialTheme.typography.bodyMedium)
                    Text(formatRupiah(saldo), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        Column {
                            Text("Pemasukan", style = MaterialTheme.typography.bodyMedium)
                            Text(formatRupiah(pemasukan), fontWeight = FontWeight.SemiBold)
                        }
                        Column {
                            Text("Pengeluaran", style = MaterialTheme.typography.bodyMedium)
                            Text(formatRupiah(pengeluaran), fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Riwayat Transaksi", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(transactions) { trx ->
                    Card {
                        Row(
                            Modifier.fillMaxWidth().padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(trx.title, fontWeight = FontWeight.SemiBold)
                                Text("${trx.category} • ${DateUtils.formatDate(trx.dateMillis)}", style = MaterialTheme.typography.bodyMedium)
                            }
                            Row(verticalAlignment = Alignment_CenterVertically) {
                                Text(
                                    (if (trx.type == TransactionType.PEMASUKAN) "+ " else "- ") + formatRupiah(trx.amount),
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { vm.deleteTransaction(trx) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Hapus")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AddTransactionDialog(onDismiss = { showDialog = false }) { title, amount, type, category, note ->
            vm.addTransaction(title, amount, type, category, note, System.currentTimeMillis())
            showDialog = false
        }
    }
}

private val Alignment_CenterVertically = androidx.compose.ui.Alignment.CenterVertically

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddTransactionDialog(onDismiss: () -> Unit, onSave: (String, Long, TransactionType, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.PENGELUARAN) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tambah Transaksi") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(selected = type == TransactionType.PEMASUKAN, onClick = { type = TransactionType.PEMASUKAN }, label = { Text("Pemasukan") })
                    FilterChip(selected = type == TransactionType.PENGELUARAN, onClick = { type = TransactionType.PENGELUARAN }, label = { Text("Pengeluaran") })
                }
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul") })
                OutlinedTextField(value = amount, onValueChange = { amount = it.filter { c -> c.isDigit() } }, label = { Text("Jumlah (Rp)") })
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Kategori") })
                OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Catatan (opsional)") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val amt = amount.toLongOrNull() ?: 0L
                if (title.isNotBlank() && amt > 0) onSave(title, amt, type, category.ifBlank { "Umum" }, note)
            }) { Text("Simpan") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Batal") } }
    )
}

fun formatRupiah(amount: Long): String {
    val formatted = amount.toString().reversed().chunked(3).joinToString(".").reversed()
    return "Rp $formatted"
}
