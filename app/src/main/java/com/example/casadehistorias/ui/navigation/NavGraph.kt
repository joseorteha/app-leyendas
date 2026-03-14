package com.example.casadehistorias.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.casadehistorias.ui.auth.AuthState
import com.example.casadehistorias.ui.auth.AuthViewModel
import com.example.casadehistorias.ui.auth.LoginScreen
import com.example.casadehistorias.ui.auth.SignUpScreen
import com.example.casadehistorias.ui.favorites.FavoritesScreen
import com.example.casadehistorias.ui.favorites.FavoritesViewModel
import com.example.casadehistorias.ui.profile.ProfileScreen
import com.example.casadehistorias.ui.search.SearchScreen
import com.example.casadehistorias.ui.settings.SettingsScreen
import com.example.casadehistorias.ui.story.*
import com.example.casadehistorias.ui.discover.DiscoverScreen
import com.example.casadehistorias.ui.dictionary.DictionaryScreen
import com.example.casadehistorias.ui.welcome.WelcomeScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Discover : Screen("discover")
    object Dictionary : Screen("dictionary")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object StoryList : Screen("story_list")
    object AddStory : Screen("add_story")
    object StoryMap : Screen("story_map")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object StoryDetail : Screen("story_detail/{storyId}") {
        fun createRoute(storyId: String) = "story_detail/$storyId"
    }
    object EditStory : Screen("edit_story/{storyId}") {
        fun createRoute(storyId: String) = "edit_story/$storyId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    startDestination: String
) {
    val storyViewModel: StoryViewModel = hiltViewModel()
    val favoritesViewModel: FavoritesViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onContinue = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.StoryList.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.StoryList.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Discover.route) {
            DiscoverScreen(
                viewModel = storyViewModel,
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryDetail.createRoute(storyId))
                },
                onNavigateToStoryList = {
                    navController.navigate(Screen.StoryList.route)
                },
                onNavigateToDictionary = {
                    navController.navigate(Screen.Dictionary.route)
                }
            )
        }
        
        composable(Screen.Dictionary.route) {
            DictionaryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.StoryList.route) {
            StoryListScreen(
                viewModel = storyViewModel,
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryDetail.createRoute(storyId))
                },
                onAddStoryClick = {
                    navController.navigate(Screen.AddStory.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                }
            )
        }

        composable(Screen.AddStory.route) {
            AddStoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.StoryMap.route) {
            StoryMapScreen(
                viewModel = storyViewModel,
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryDetail.createRoute(storyId))
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                viewModel = favoritesViewModel,
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryDetail.createRoute(storyId))
                }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                authViewModel = authViewModel,
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    authViewModel.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryDetail.createRoute(storyId))
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.StoryDetail.route,
            arguments = listOf(
                navArgument("storyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId") ?: ""
            val stories by storyViewModel.stories.collectAsState()
            val story = stories.find { it.id == storyId }

            LaunchedEffect(storyId) {
                if (story == null) {
                    storyViewModel.loadStoryById(storyId)
                }
            }

            val selectedStory by storyViewModel.selectedStory.collectAsState()
            val displayStory = story ?: selectedStory

            if (displayStory != null) {
                StoryDetailScreen(
                    story = displayStory,
                    favoritesViewModel = favoritesViewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onEditClick = {
                        navController.navigate(Screen.EditStory.createRoute(storyId))
                    }
                )
            } else {
                // Loading state is handled by StoryDetailScreen internally
                StoryDetailLoadingScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Screen.EditStory.route,
            arguments = listOf(
                navArgument("storyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId") ?: ""
            val stories by storyViewModel.stories.collectAsState()
            val story = stories.find { it.id == storyId }

            story?.let {
                EditStoryScreen(
                    story = it,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
