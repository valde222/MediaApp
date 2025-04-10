package com.example.mediaapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mediaapp.ui.nav.BottomNavBar
import com.example.mediaapp.ui.nav.NavigationGraph
import com.example.mediaapp.ui.nav.TopNavBarE
import com.example.mediaapp.viewModels.CurrentUserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(loginNavController: NavController, viewModel: CurrentUserViewModel = koinViewModel()) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet (
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                drawerContentColor = MaterialTheme.colorScheme.surface

            ) {
                TopNavBarE(drawerState = drawerState)
                Text(
                    stringResource(R.string.menu),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(16.dp)
                )
                NavigationDrawerItem(
                    label = { currentUser?.user?.username?.let { Text(it) } },
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "account circle",
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    },
                    selected = true,
                    onClick = {
                        navController.navigate(Screen.Profile.route)
                        scope.launch {
                            if(drawerState.isOpen) {
                                drawerState.close()
                            }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.youfollow)) },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.surface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Group,
                            contentDescription = "group",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = false,
                    badge = { Badge(containerColor = MaterialTheme.colorScheme.tertiaryContainer) { Text(text = currentUser?.user?.following?.size.toString(), color = MaterialTheme.colorScheme.onTertiaryContainer) }  },
                    onClick = {
                        navController.navigate(Screen.YouFollow.route)
                        scope.launch {
                            if(drawerState.isOpen) {
                                drawerState.close()
                            }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.yourfollowers)) },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.surface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Groups,
                            contentDescription = "group",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = false,
                    badge = { Badge(containerColor = MaterialTheme.colorScheme.tertiaryContainer) { Text(text = currentUser?.user?.followers?.size.toString(), color = MaterialTheme.colorScheme.onTertiaryContainer) } },
                    onClick = {
                        navController.navigate(Screen.YourFollowers.route)
                        scope.launch {
                            if(drawerState.isOpen) {
                                drawerState.close()
                            }
                        }
                    }
                )
                NavigationDrawerItem(
                    label = { Text(stringResource(R.string.settings)) },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = MaterialTheme.colorScheme.surface,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurface
                    ),
                    icon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "settings",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                        scope.launch {
                            if(drawerState.isOpen) {
                                drawerState.close()
                            }
                        }
                    }
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Button(
                        onClick = {
                            Firebase.auth.signOut()
                            navController.navigate(Screen.Login.route)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier
                            .width(296.dp)
                            .height(40.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.logout),
                            color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }

            }
        },
        //Need something to handle this in the feature
        gesturesEnabled = false,
        drawerState = drawerState,
        content = {
            Scaffold(
                bottomBar = { BottomNavBar(navController = navController) },
            ) { innerPadding ->
                NavigationGraph(navController = navController, loginNavController = loginNavController, drawerState = drawerState)
                Modifier.padding(innerPadding)
            }
        },
    )
}