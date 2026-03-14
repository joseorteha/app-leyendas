package com.example.casadehistorias.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.casadehistorias.ui.auth.AuthViewModel
import com.example.casadehistorias.ui.navigation.NavGraph
import com.example.casadehistorias.ui.navigation.Screen
import com.example.casadehistorias.ui.theme.*

data class BottomNavItem(
    val label: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainContainer() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    // Determine start destination based on auth state
    val startDestination = if (authViewModel.isLoggedIn) {
        Screen.Discover.route
    } else {
        Screen.Welcome.route
    }

    val bottomNavItems = listOf(
        BottomNavItem("Explorar", Screen.Discover.route, Icons.Filled.AutoAwesome, Icons.Outlined.AutoAwesome),
        BottomNavItem("Leyendas", Screen.StoryList.route, Icons.Filled.AutoStories, Icons.Outlined.AutoStories),
        BottomNavItem("Mapa", Screen.StoryMap.route, Icons.Filled.Explore, Icons.Outlined.Explore),
        BottomNavItem("Favoritos", Screen.Favorites.route, Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder),
        BottomNavItem("Perfil", Screen.Profile.route, Icons.Filled.Person, Icons.Outlined.Person)
    )

    // Determine if the bottom bar should be shown
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute in listOf(
        Screen.Discover.route,
        Screen.StoryList.route,
        Screen.StoryMap.route,
        Screen.Favorites.route,
        Screen.Profile.route
    )

    Scaffold(
        containerColor = NocheProfunda,
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = FondoTarjeta,
                    contentColor = LunaLlena
                ) {
                    bottomNavItems.forEach { item ->
                        val isSelected = navBackStackEntry?.destination?.hierarchy?.any {
                            it.route == item.route
                        } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = AguaSagrada,
                                selectedTextColor = AguaSagrada,
                                unselectedIconColor = TextoSecundario,
                                unselectedTextColor = TextoSecundario,
                                indicatorColor = AguaSagradaOscura.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavGraph(
                navController = navController,
                authViewModel = authViewModel,
                startDestination = startDestination
            )
        }
    }
}
