package com.example.scanai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.scanai.ui.screens.CameraScreen
import com.example.scanai.ui.screens.HistoryScreen
import com.example.scanai.ui.theme.ScanAITheme
import com.example.scanai.viewmodel.CameraViewModel
import com.example.scanai.viewmodel.HistoryViewModel

class MainActivity : ComponentActivity() {
    private lateinit var cameraViewModel: CameraViewModel
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraViewModel = ViewModelProvider(this)[CameraViewModel::class.java]
        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]

        setContent {
            ScanAITheme {
                Surface {
                    MainScreen(cameraViewModel, historyViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    cameraViewModel: CameraViewModel,
    historyViewModel: HistoryViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ScanAI") }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "camera",
                    onClick = {
                        navController.navigate("camera") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = "Scanner"
                        )
                    },
                    label = { Text("Scanner") }
                )
                NavigationBarItem(
                    selected = currentRoute == "history",
                    onClick = {
                        navController.navigate("history") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = "Historique"
                        )
                    },
                    label = { Text("Historique") }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "camera",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("camera") {
                CameraScreen(cameraViewModel)
            }
            composable("history") {
                HistoryScreen(historyViewModel)
            }
        }
    }
}