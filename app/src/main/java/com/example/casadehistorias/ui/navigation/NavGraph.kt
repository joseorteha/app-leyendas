package com.example.casadehistorias.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.casadehistorias.ui.auth.LoginScreen
import com.example.casadehistorias.ui.auth.SignUpScreen
import com.example.casadehistorias.ui.story.AddStoryScreen
import com.example.casadehistorias.ui.story.StoryDetailScreen
import com.example.casadehistorias.ui.story.StoryListScreen
import com.example.casadehistorias.ui.story.StoryMapScreen
import com.example.casadehistorias.ui.story.StoryViewModel
import com.example.casadehistorias.ui.welcome.WelcomeScreen

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object StoryList : Screen("story_list")
    object AddStory : Screen("add_story")
    object StoryMap : Screen("story_map")
    object StoryDetail : Screen("story_detail/{storyId}") {
        fun createRoute(storyId: String) = "story_detail/$storyId"
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Welcome.route
) {
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
                        popUpTo(Screen.Login.route) { inclusive = true }
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
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.StoryList.route) {
            val viewModel: StoryViewModel = hiltViewModel()

            StoryListScreen(
                viewModel = viewModel,
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryDetail.createRoute(storyId))
                },
                onAddStoryClick = {
                    navController.navigate(Screen.AddStory.route)
                }
            )
        }

        composable(Screen.AddStory.route) {
            AddStoryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.StoryMap.route) {
            val viewModel: StoryViewModel = hiltViewModel()
            StoryMapScreen(
                viewModel = viewModel,
                onStoryClick = { storyId ->
                    navController.navigate(Screen.StoryDetail.createRoute(storyId))
                }
            )
        }

        composable(
            route = Screen.StoryDetail.route,
            arguments = listOf(
                navArgument("storyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId") ?: ""
            val viewModel: StoryViewModel = hiltViewModel()
            val stories = viewModel.stories.collectAsState()
            val story = stories.value.find { it.id == storyId }

            story?.let {
                StoryDetailScreen(
                    story = it,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
