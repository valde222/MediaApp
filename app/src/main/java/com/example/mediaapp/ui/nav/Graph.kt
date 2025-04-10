package com.example.mediaapp.ui.nav

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mediaapp.MainScreen
import com.example.mediaapp.Screen
import com.example.mediaapp.screens.CreateAccountPageLayout
import com.example.mediaapp.screens.HomeScreen
import com.example.mediaapp.screens.FollowingListPage
import com.example.mediaapp.screens.ForgotPasswordPageLayout
import com.example.mediaapp.screens.LoginPageLayout
import com.example.mediaapp.screens.MovieDetailPage
import com.example.mediaapp.screens.WatchlistPage
import com.example.mediaapp.screens.ProfilePageLayout
import com.example.mediaapp.screens.SearchPage
import com.example.mediaapp.screens.SettingpageLayout
import com.example.mediaapp.screens.YoufollowPageLayout
import com.example.mediaapp.screens.YourfollowersPageLayout

//Main NavigationGraph for the APP
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationGraph(navController: NavHostController, loginNavController: NavController, drawerState: DrawerState) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, drawerState = drawerState)
        }
        composable(route = Screen.Following.route) {
            FollowingListPage(drawerState = drawerState)
        }
        composable(route = Screen.Watchlist.route) {
            WatchlistPage(navController = navController, drawerState = drawerState)
        }
        composable(route = Screen.Search.route) {
            SearchPage(navController = navController, drawerState = drawerState)
        }
        composable(route = Screen.Profile.route) {
            ProfilePageLayout( drawerState = drawerState)
        }
        composable(route = Screen.YouFollow.route) {
            YoufollowPageLayout(navController = navController, drawerState = drawerState)
        }
        composable(route = Screen.YourFollowers.route) {
            YourfollowersPageLayout(navController = navController, drawerState = drawerState)
        }
        composable(route = Screen.Settings.route) {
            SettingpageLayout(navController = navController, drawerState = drawerState)
        }
        composable(route = "${Screen.MoviePage.route}/{movieId}",
            enterTransition = {scaleIntoContainer()},
            exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) },
            popEnterTransition = {
                scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
            popExitTransition = { scaleOutOfContainer() })
        { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")
            if (movieId != null) {
                MovieDetailPage(navController = navController, drawerState = drawerState, movieId = movieId)
            }
        }
        composable(route = Screen.Login.route) {
            loginNavController.navigate("navigationGraphLogin")
        }
    }
}

//NavigationGraph only used for Login System
@Composable
fun NavigationGraphLogin(navController: NavHostController) {
    NavHost(navController = navController, route = "navigationGraphLogin", startDestination = Screen.Login.route) {
        composable(route = Screen.Login.route) {
            LoginPageLayout(navController = navController)
        }
        composable(route = Screen.Register.route) {
            CreateAccountPageLayout(navController = navController)
        }
        composable(route = Screen.ForgotPassword.route) {
            ForgotPasswordPageLayout(navController = navController)
        }
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController)
        }
    }
}

fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(220, delayMillis = 90),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(220, delayMillis = 90))
}

enum class ScaleTransitionDirection {
    INWARDS,
    OUTWARDS
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 220,
            delayMillis = 90
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 90))
}
