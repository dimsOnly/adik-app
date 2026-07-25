package com.salwa.adikapp.ui.navigation

sealed class AppDestinations(val route: String, val label: String) {
    data object Home : AppDestinations("home", "Beranda")
    data object Finance : AppDestinations("finance", "Keuangan")
    data object Wishlist : AppDestinations("wishlist", "Wishlist")
    data object StudyTarget : AppDestinations("study_target", "Target Belajar")
    data object Schedule : AppDestinations("schedule", "Jadwal Kuliah")
    data object Activity : AppDestinations("activity", "Kegiatan")
    data object Diary : AppDestinations("diary", "Note Harian")
    data object TaskNote : AppDestinations("task_note", "Note Tugas")
}
