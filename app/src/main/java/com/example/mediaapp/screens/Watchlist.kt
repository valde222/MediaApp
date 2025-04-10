package com.example.mediaapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mediaapp.R
import com.example.mediaapp.ui.MovieListLayout
import com.example.mediaapp.ui.TabsAndFilters
import com.example.mediaapp.ui.nav.TopNavBarA
import com.example.mediaapp.viewModels.WatchlistViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistPage(
    navController: NavController,
    drawerState: DrawerState,
    viewModel: WatchlistViewModel = koinViewModel()
) {

    val watchlistMovies = viewModel.filteredWatchList.collectAsState()
    val listOfGenres: List<String> = listOf(
        stringResource(R.string.action),
        stringResource(R.string.adventure),
        stringResource(R.string.animation),
        stringResource(R.string.comedy),
        stringResource(R.string.crime),
        stringResource(R.string.documentary),
        stringResource(R.string.drama),
        stringResource(R.string.family),
        stringResource(R.string.fantasy),
        stringResource(R.string.horror),
        stringResource(R.string.music),
        stringResource(R.string.mystery),
        stringResource(R.string.romance),
        stringResource(R.string.tv_movie),
        stringResource(R.string.thriller),
        stringResource(R.string.war),
        stringResource(R.string.western)
    )
    val listOfYearOrders: List<String> = listOf(
        stringResource(R.string.year_asc),
        stringResource(R.string.year_desc)
    )
    val listOfNameOrders: List<String> = listOf(
        stringResource(R.string.name_asc),
        stringResource(R.string.name_desc)
    )

    LaunchedEffect(Unit) {
        viewModel.getWatchlistMovies()
    }

    val scope = rememberCoroutineScope()
    Column (
        //Adding this padding for the bottom navigation bar
        modifier = Modifier.padding(bottom = 70.dp)
    ) {
        TopNavBarA(drawerState = drawerState)
        // UI Tabs and Filters

        val tabs = listOf(
            stringResource(R.string.all),
            stringResource(R.string.watched),
            stringResource(R.string.not_watched)
        )
        val filters = listOf(
            TabsAndFilters.FilterOption(
                stringResource(R.string.genre),
                stringResource(R.string.genre),
                listOfGenres,
            ),
            TabsAndFilters.FilterOption(
                stringResource(R.string.year),
                stringResource(R.string.year),
                listOfYearOrders
            ),
            TabsAndFilters.FilterOption(
                stringResource(R.string.name),
                stringResource(R.string.name),
                listOfNameOrders
            )
            /*
            TabsAndFilters.FilterOption(
                stringResource(R.string.rating_from),
                (0..10).map { it.toString() }),
            TabsAndFilters.FilterOption(
                stringResource(R.string.rating_to),
                (0..10).map { it.toString() })
                */
        )
        TabsAndFilters(filters, tabs,
            onFilterSelected = { filterId, option ->
            viewModel.onFilterOptionSelected(filterId, option)
        }, onTabSelected = { tab ->
            viewModel.filterWatchedMovies(tab)
        }).Render(navController = navController)


        val movieLayout = watchlistMovies.value?.let { MovieListLayout(it, navController, scope, viewModel) }
        movieLayout?.MovieList()
    }
}

