package com.example.mediaapp.ui.nav

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.outlined.FormatListBulleted
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mediaapp.R
import com.example.mediaapp.Screen
import kotlinx.coroutines.launch

data class BottomNavItem(
    val name: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun BottomNavBar(navController: NavController) {
    val bottomNavItems = listOf(
        BottomNavItem(
            name = stringResource(R.string.home),
            route = Screen.Home.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            name = stringResource(R.string.following),
            route = Screen.Following.route,
            selectedIcon = Icons.Filled.Subscriptions,
            unselectedIcon = Icons.Outlined.Subscriptions
        ),
        BottomNavItem(
            name = stringResource(R.string.watchlist),
            route = Screen.Watchlist.route,
            selectedIcon = Icons.Filled.FormatListBulleted,
            unselectedIcon = Icons.Outlined.FormatListBulleted
        ),
        BottomNavItem(
            name = stringResource(R.string.search),
            route = Screen.Search.route,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    val contentColor = MaterialTheme.colorScheme.onSurface
    val containerColor = MaterialTheme.colorScheme.surface
    val indicatorColor = MaterialTheme.colorScheme.secondaryContainer

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navStackBackEntry?.destination?.route
    selectedItemIndex = bottomNavItems.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    NavigationBar(
        containerColor = containerColor,
        tonalElevation = 0.dp,
    ) {
        bottomNavItems.forEachIndexed { index, item ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = contentColor,
                    unselectedIconColor = contentColor,
                    indicatorColor = indicatorColor
                ),
                selected = selectedItemIndex == index,
                onClick = {
                    selectedItemIndex = index
                    navController.navigate(item.route)
                },
                label = {
                    Text(text = item.name, color = contentColor)
                },
                icon = {
                    Icon(
                        imageVector = if (index == selectedItemIndex) {
                            item.selectedIcon
                        } else item.unselectedIcon,
                        contentDescription = item.name
                    )
                }
            )
        }
    }
}


/**
 * TopNavBarA
 *
 * This function has a Hamburger to the left followed by a name. See Figma for a more precise
 * example or preview the function
 *
 * @param navController navigation controller
 * @param modifier optional
 *
 * @return Returns a Top Navigation Bar type A.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBarA(drawerState: DrawerState) {
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val iconColor = MaterialTheme.colorScheme.onSurface
    val scope = rememberCoroutineScope()

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = iconColor,
        ),
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "menu"
                )
            }
        },
        title = {
            Text(stringResource(R.string.mediaapp))
        }
    )
}

/**
 * TopNavBarB
 *
 * This function has a LeftArrow icon to the left, a centered name, and a trailing-icon to the right. See Figma for a more precise
 * example or preview the function
 *
 * @param navController navigation controller
 * @param modifier optional
 * @return Returns a Top Navigation Bar of type B.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBarB(navController: NavController, drawerState: DrawerState) {
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val iconColor = MaterialTheme.colorScheme.onSurface
    val scope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = iconColor,
            actionIconContentColor = iconColor,
        ),
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
                scope.launch{
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "menu"
                )
            }
        },
        title = {
            Text(stringResource(R.string.mediaapp))
        },
    )
}


/**
 * TopNavBarC
 *
 * This function has a Hamburger to the left, a name, and an edit button to the right. See Figma for a more precise
 * example or preview the function
 *
 * @param navController navigation controller
 * @param modifier optional
 * @return Returns a Top Navigation Bar of type C.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBarC(drawerState: DrawerState, modifier: Modifier = Modifier) {
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val iconColor = MaterialTheme.colorScheme.onSurface
    val buttonColor = MaterialTheme.colorScheme.secondaryContainer
    val scope = rememberCoroutineScope()

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = iconColor,
        ),
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "menu"
                )
            }
        },
        title = {
            Text(stringResource(R.string.mediaapp))
        },
        actions = {
            Button(
                onClick = { /*TODO*/  },
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor
                ),
                modifier = modifier.padding(end = 6.dp)
            ) {
                Text(stringResource(R.string.edit),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}

/**
 * TopNavBarD
 *
 * This function has a left_arrow icon to the left and a name centerted with more icon to the left. See Figma for a more precise
 * example or preview the function
 *
 * @param navController navigation controller
 * @param modifier optional
 * @return Returns a Top Navigation Bar of type D.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBarD(navController: NavController, drawerState: DrawerState) {
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val iconColor = MaterialTheme.colorScheme.onSurface
    val scope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = iconColor,
            actionIconContentColor = iconColor
        ),
        navigationIcon = {
            IconButton(onClick = {
                navController.navigateUp()
                scope.launch {
                    if(drawerState.isOpen) {
                        drawerState.close()
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "arrow back"
                )
            }
        },
        title = {
            Text(stringResource(R.string.mediaapp))
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "more vert"
                )
            }
        }
    )
}

/**
 * TopNavBarE
 *
 * This function has a axe icon to the left and a name. See Figma for a more precise
 * example or preview the function
 *
 * @param navController navigation controller
 * @param modifier optional
 * @return Returns a Top Navigation Bar of type D.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBarE(drawerState: DrawerState) {
    val containerColor = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val iconColor = MaterialTheme.colorScheme.onSurface
    val scope = rememberCoroutineScope()

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = iconColor,
        ),
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.close()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "arrow back",
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
            }
        },
        title = {
            Text(stringResource(R.string.mediaapp))
        }
    )
}

/**
 * TopNavBarF
 *
 * This function has a centered name. See Figma for a more precise
 * example or preview the function
 *
 * @param navController navigation controller
 * @param modifier optional
 * @return Returns a Top Navigation Bar of type F.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBarF() {
    val containerColor = colorResource(R.color.top_navbar_container_color)

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
        ),
        title = {
            Text(stringResource(R.string.mediaapp))
        }
    )
}

/*
@Preview
@Composable
fun PreviewNavigation() {
    val navController = rememberNavController()
    //TopNavBarA(navController = navController) //Used in Watchlist, Following, Search,
    //TopNavBarB(navController = navController)  //Used in Settings Page, You Follow, Your Followers
    //TopNavBarC(navController = navController) //Used in profile page
    //TopNavBarD(navController = navController) //Used in Movie Details Page
    //TopNavBarE(navController = navController) //Used in Side Menu (Drawer)
    //TopNavBarF(navController = navController) //Used in Login, Register, Forgot Password Page
    //NavDrawer(navController = navController)
}
 */