package com.kreeda.ankana

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Sports
import androidx.compose.material.icons.filled.SportsCricket
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kreeda.ankana.feature.auth.AuthScreen
import com.kreeda.ankana.feature.auth.AuthViewModel
import com.kreeda.ankana.feature.booking.BookingScreen
import com.kreeda.ankana.feature.booking.BookingViewModel
import com.kreeda.ankana.feature.challenges.ChallengesScreen
import com.kreeda.ankana.feature.challenges.ChallengesViewModel
import com.kreeda.ankana.feature.grounds.GroundsScreen
import com.kreeda.ankana.feature.grounds.GroundsViewModel
import com.kreeda.ankana.feature.profile.ProfileScreen
import com.kreeda.ankana.feature.profile.ProfileViewModel
import com.kreeda.ankana.feature.scores.ScoresScreen
import com.kreeda.ankana.feature.scores.ScoresViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authViewModel: AuthViewModel = hiltViewModel()
                    val authState by authViewModel.uiState.collectAsState()

                    when {
                        authState.isLoading && authState.currentUserId == null -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                        authState.currentUserId == null -> {
                            AuthScreen(viewModel = authViewModel)
                        }
                        else -> {
                            MainAppContent()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainAppContent() {
    val navController = rememberNavController()
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val tabs = listOf(
        AppRoute.Grounds,
        AppRoute.Booking,
        AppRoute.Challenges,
        AppRoute.Scores,
        AppRoute.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { route ->
                    NavigationBarItem(
                        selected = currentRoute == route.route,
                        onClick = {
                            navController.navigate(route.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(route.icon, contentDescription = route.label) },
                        label = { Text(route.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Grounds.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(AppRoute.Grounds.route) {
                val vm: GroundsViewModel = hiltViewModel()
                GroundsScreen(viewModel = vm, onGroundSelected = {})
            }
            composable(AppRoute.Booking.route) {
                val vm: BookingViewModel = hiltViewModel()
                BookingScreen(viewModel = vm)
            }
            composable(AppRoute.Challenges.route) {
                val vm: ChallengesViewModel = hiltViewModel()
                ChallengesScreen(viewModel = vm)
            }
            composable(AppRoute.Scores.route) {
                val vm: ScoresViewModel = hiltViewModel()
                ScoresScreen(viewModel = vm)
            }
            composable(AppRoute.Profile.route) {
                val vm: ProfileViewModel = hiltViewModel()
                ProfileScreen(viewModel = vm)
            }
        }
    }
}

private sealed class AppRoute(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Grounds : AppRoute("grounds", "Grounds", Icons.Default.Place)
    data object Booking : AppRoute("booking", "Booking", Icons.Default.AddCircle)
    data object Challenges : AppRoute("challenges", "Challenges", Icons.Default.Sports)
    data object Scores : AppRoute("scores", "Scores", Icons.Default.SportsCricket)
    data object Profile : AppRoute("profile", "Profile", Icons.Default.AccountCircle)
}
