package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.MainViewModel
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.ChatbotScreen
import com.example.ui.screens.DietScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.TrackerScreen
import com.example.ui.screens.WorkoutCompleteScreen
import com.example.ui.screens.WorkoutListScreen
import com.example.ui.theme.HomeWorkoutProTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeWorkoutProTheme {
                MainAppScreen()
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Default.Home)
    object Workouts : BottomNavItem("workouts", "Workouts", Icons.Default.FitnessCenter)
    object Tracker : BottomNavItem("tracker", "Tracker", Icons.Default.DirectionsRun)
    object Diet : BottomNavItem("diet", "Diet", Icons.Default.Restaurant)
    object Profile : BottomNavItem("profile", "Profile", Icons.Default.Person)
}

@Composable
fun MainAppScreen() {
    val navController = rememberNavController()
    val viewModel: MainViewModel = viewModel()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Workouts,
        BottomNavItem.Tracker,
        BottomNavItem.Diet,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val hideBottomNavRoutes = listOf("active_workout", "workout_complete", "chatbot")
    val shouldShowBottomNav = currentRoute !in hideBottomNavRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (shouldShowBottomNav) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) },
                            selected = currentRoute == item.route,
                            modifier = Modifier.testTag("nav_item_${item.route}"),
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onSelectPlan = { plan ->
                        viewModel.selectWorkoutPlan(plan)
                        navController.navigate("active_workout")
                    },
                    onNavigateToWorkouts = { navController.navigate(BottomNavItem.Workouts.route) },
                    onNavigateToTracker = { navController.navigate(BottomNavItem.Tracker.route) },
                    onNavigateToChat = { navController.navigate("chatbot") }
                )
            }

            composable(BottomNavItem.Workouts.route) {
                WorkoutListScreen(
                    viewModel = viewModel,
                    onSelectPlan = { plan ->
                        viewModel.selectWorkoutPlan(plan)
                        navController.navigate("active_workout")
                    }
                )
            }

            composable(BottomNavItem.Tracker.route) {
                TrackerScreen(viewModel = viewModel)
            }

            composable(BottomNavItem.Diet.route) {
                DietScreen()
            }

            composable(BottomNavItem.Profile.route) {
                ProfileScreen(viewModel = viewModel)
            }

            composable("active_workout") {
                val plan = viewModel.selectedPlan.value ?: viewModel.workoutPlans.value.first()
                ActiveWorkoutScreen(
                    plan = plan,
                    onWorkoutComplete = { duration, calories ->
                        viewModel.finishWorkout(
                            title = plan.title,
                            durationSeconds = duration,
                            calories = calories,
                            exerciseCount = plan.exerciseCount
                        )
                        navController.navigate("workout_complete/$duration/$calories") {
                            popUpTo("active_workout") { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("workout_complete/{duration}/{calories}") { backStackEntry ->
                val duration = backStackEntry.arguments?.getString("duration")?.toIntOrNull() ?: 0
                val calories = backStackEntry.arguments?.getString("calories")?.toIntOrNull() ?: 0

                WorkoutCompleteScreen(
                    durationSeconds = duration,
                    caloriesBurned = calories,
                    onReturnHome = {
                        navController.navigate(BottomNavItem.Home.route) {
                            popUpTo(BottomNavItem.Home.route) { inclusive = true }
                        }
                    }
                )
            }

            composable("chatbot") {
                ChatbotScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
