package com.salwa.adikapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.salwa.adikapp.ui.screens.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute != AppDestinations.Home.route) {
                TopAppBar(
                    title = {
                        Text(destinationLabel(currentRoute))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali"
                            )
                        }
                    }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = AppDestinations.Home.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(AppDestinations.Home.route) {
                HomeScreen(
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable(AppDestinations.Finance.route) {
                FinanceScreen()
            }

            composable(AppDestinations.Wishlist.route) {
                WishlistScreen()
            }

            composable(AppDestinations.StudyTarget.route) {
                StudyTargetScreen()
            }

            composable(AppDestinations.Schedule.route) {
                ScheduleScreen()
            }

            composable(AppDestinations.Activity.route) {
                ActivityScreen()
            }

            composable(AppDestinations.Diary.route) {
                DiaryScreen()
            }

            composable(AppDestinations.TaskNote.route) {
                TaskNoteScreen()
            }
        }
    }
}

private fun destinationLabel(route: String?): String = when (route) {
    AppDestinations.Finance.route -> AppDestinations.Finance.label
    AppDestinations.Wishlist.route -> AppDestinations.Wishlist.label
    AppDestinations.StudyTarget.route -> AppDestinations.StudyTarget.label
    AppDestinations.Schedule.route -> AppDestinations.Schedule.label
    AppDestinations.Activity.route -> AppDestinations.Activity.label
    AppDestinations.Diary.route -> AppDestinations.Diary.label
    AppDestinations.TaskNote.route -> AppDestinations.TaskNote.label
    else -> ""
}
