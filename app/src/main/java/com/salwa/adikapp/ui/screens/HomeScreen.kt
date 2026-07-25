package com.salwa.adikapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.salwa.adikapp.ui.navigation.AppDestinations

data class MenuItem(val destination: AppDestinations, val icon: ImageVector, val subtitle: String)

private val menuItems = listOf(
    MenuItem(AppDestinations.Finance, Icons.Filled.AccountBalanceWallet, "Saldo, pemasukan & pengeluaran"),
    MenuItem(AppDestinations.Wishlist, Icons.Filled.Favorite, "Barang impian"),
    MenuItem(AppDestinations.StudyTarget, Icons.Filled.School, "Target belajar semester ini"),
    MenuItem(AppDestinations.Schedule, Icons.Filled.CalendarViewWeek, "Jadwal kuliah mingguan"),
    MenuItem(AppDestinations.Activity, Icons.Filled.Checklist, "Kegiatan & agenda harian"),
    MenuItem(AppDestinations.Diary, Icons.Filled.PhotoLibrary, "Cerita hari ini + foto"),
    MenuItem(AppDestinations.TaskNote, Icons.Filled.Assignment, "Tugas kuliah & deadline")
)

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text("Halo, Salwa! ✨", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Semoga hari ini menyenangkan ya~", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(20.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(menuItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clickable { onNavigate(item.destination.route) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(item.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                        Column {
                            Text(item.destination.label, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
                            Text(item.subtitle, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                        }
                    }
                }
            }
        }
    }
}
