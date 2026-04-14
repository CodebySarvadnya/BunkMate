package com.example.bunkmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bunkmate.ui.theme.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calculator : Screen("calculator")
    object Target : Screen("target")
    object Prediction : Screen("prediction")
    object DailyEntry : Screen("daily_entry")
    object History : Screen("history")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BunkMateTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onCalculateClick = { navController.navigate(Screen.Calculator.route) },
                                onSetTargetClick = { navController.navigate(Screen.Target.route) },
                                onPredictFutureClick = { navController.navigate(Screen.Prediction.route) },
                                onAddAttendanceClick = { navController.navigate(Screen.DailyEntry.route) },
                                onViewHistoryClick = { navController.navigate(Screen.History.route) }
                            )
                        }

                        composable(Screen.Calculator.route) {
                            CalculatorScreen(onBackClick = { navController.popBackStack() })
                        }

                        composable(Screen.Target.route) {
                            TargetScreen(onBackClick = { navController.popBackStack() })
                        }

                        composable(Screen.Prediction.route) {
                            PredictionScreen(onBackClick = { navController.popBackStack() })
                        }

                        composable(Screen.DailyEntry.route) {
                            DailyEntryScreen(onBackClick = { navController.popBackStack() })
                        }

                        composable(Screen.History.route) {
                            HistoryScreen(onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}