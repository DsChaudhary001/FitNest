package com.fitnest.app.ui.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.fitnest.app.ui.ai.AiScreen
import com.fitnest.app.ui.food.FoodScreen
import com.fitnest.app.ui.home.HomeScreen
import com.fitnest.app.ui.profile.ProfileSetupScreen
import com.fitnest.app.ui.progress.ProgressScreen
import com.fitnest.app.ui.workout.WorkoutScreen

private sealed class Dest(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Dest("home", "Home", Icons.Filled.Home)
    object Food : Dest("food", "Food", Icons.Filled.Restaurant)
    object Workout : Dest("workout", "Workout", Icons.Filled.FitnessCenter)
    object Progress : Dest("progress", "Progress", Icons.Filled.ShowChart)
    object Ai : Dest("ai", "AI", Icons.Filled.SmartToy)
}

private val bottomDests = listOf(Dest.Home, Dest.Food, Dest.Workout, Dest.Progress, Dest.Ai)

@Composable
fun FitNestApp() {
    val vm: AppViewModel = viewModel()
    val profile by vm.profile.collectAsState()

    if (profile == null || profile?.profileComplete != true) {
        ProfileSetupScreen(onSaved = { vm.saveProfile(it) })
        return
    }

    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        MainNavHost(navController, vm, Modifier.padding(padding))
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        bottomDests.forEach { dest ->
            NavigationBarItem(
                selected = currentRoute == dest.route,
                onClick = {
                    navController.navigate(dest.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(dest.icon, contentDescription = dest.label) },
                label = { Text(dest.label) }
            )
        }
    }
}

@Composable
private fun MainNavHost(navController: NavHostController, vm: AppViewModel, modifier: Modifier) {
    val profile by vm.profile.collectAsState()
    val allFood by vm.allFood.collectAsState()
    val todayFood by vm.observeFoodToday().collectAsState(initial = emptyList())
    val todayWater by vm.observeWaterToday().collectAsState(initial = emptyList())
    val workoutLogs by vm.workoutLogs.collectAsState()
    val weightHistory by vm.weightHistory.collectAsState()

    NavHost(navController, startDestination = Dest.Home.route, modifier = modifier) {
        composable(Dest.Home.route) {
            HomeScreen(
                profile = profile,
                todayFood = todayFood,
                todayWater = todayWater,
                recentWorkouts = workoutLogs,
                onQuickWater = { vm.logWater(it) },
                onGoToFood = { navController.navigate(Dest.Food.route) },
                onGoToWorkout = { navController.navigate(Dest.Workout.route) }
            )
        }
        composable(Dest.Food.route) {
            FoodScreen(todayFood = todayFood, onLog = { vm.logFood(it) })
        }
        composable(Dest.Workout.route) {
            WorkoutScreen(onComplete = { vm.logWorkout(it) })
        }
        composable(Dest.Progress.route) {
            ProgressScreen(
                weightHistory = weightHistory,
                workoutLogs = workoutLogs,
                allFood = allFood,
                onLogWeight = { vm.logWeight(it) }
            )
        }
        composable(Dest.Ai.route) {
            AiScreen()
        }
    }
}
