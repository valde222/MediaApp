package com.example.mediaapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.PlaylistAdd
import androidx.compose.material.icons.outlined.PlaylistRemove
import androidx.compose.material.icons.outlined.ThumbUpAlt
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mediaapp.R
import com.example.mediaapp.backend.apirequests.APIHandler
import com.example.mediaapp.models.Crew
import com.example.mediaapp.models.Genre
import com.example.mediaapp.models.TMDBMovieDetail
import com.example.mediaapp.ui.nav.TopNavBarD
import com.example.mediaapp.ui.theme.MediaAppTheme
import com.example.mediaapp.ui.theme.md_theme_dark_background
import com.example.mediaapp.viewModels.MovieDetailViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import com.example.mediaapp.ui.StandardBoxInRow
import com.example.mediaapp.ui.StandardBoxInRowActors
import com.example.mediaapp.ui.StandardBoxInRowCrew
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import org.koin.compose.viewmodel.koinViewModel
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.floor

private const val baseURL = "https://image.tmdb.org/t/p/original"
@Composable
fun MovieDetailPage(
    movieId: String,
    viewModel: MovieDetailViewModel = koinViewModel(),
    navController: NavController,
    drawerState: DrawerState
) {
    val movieDetails by viewModel.movieDetails.collectAsState()
    val movieCredits by viewModel.movieCredits.collectAsState()
    val actors = movieCredits?.cast?.take(20)
    val crew = movieCredits?.crew?.take(20)
    val similarMovies by viewModel.similarMovies.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(movieId) {
        viewModel.checkIfInWatchlist(movieId)
        viewModel.fetchMovieDetails(movieId)
        viewModel.fetchMovieCredits(movieId)
        viewModel.checkIfWatched(movieId)
        viewModel.updateRating(movieId)
        viewModel.fetchUserRating(movieId)
        viewModel.fetchSimilarMovies(movieId)
    }

    val movie = movieDetails ?: return
    MediaAppTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp)
                .background(md_theme_dark_background)
        ) {

            item {
                TopNavBarD(navController = navController, drawerState = drawerState)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(210.dp)
                ) {
                    AsyncImage(
                        model = baseURL + movie.backdrop_path,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
            item {
                Divider(
                    color = Color.Black,
                    thickness = 2.dp
                )
            }
            item {
                //Top part
                MovieDescription(movie, viewModel, movieId)
            }
            item {
                Text(
                    text = "Release Date: ${movie.release_date}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 10.dp, bottom = 5.dp)
                )
            }
            item {
                RatingAndBookmark(viewModel = viewModel, movieId = movieId)
            }
            item {
                WatchAndRecommend(viewModel, movieId)
            }
            item {
                SeparationBox()
            }
            item {
                //Actor Showcase
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        Text(
                            text = "Actors",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (actors != null) {
                                items(actors.size) {
                                    StandardBoxInRowActors(
                                        baseURL + actors[it].profile_path,
                                        actors[it].name,
                                        actors[it].character
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item {
                SeparationBox()
                SeparationBox()
            }
            item {
                //Crew Showcase
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column {
                        Text(
                            text = "Crew",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.labelMedium,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (crew != null) {
                                items(crew.size) {
                                    StandardBoxInRowCrew(
                                        baseURL + crew[it].profile_path,
                                        crew[it].name,
                                        crew[it].job
                                    )
                                }
                            }
                        }
                    }
                }
            }
            //SimilarMovies Showcase
            if (similarMovies?.isNotEmpty() == true) {
                item {
                    SeparationBox()
                    SeparationBox()
                }
                item {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column {
                            Text(
                                text = "Similar Movies",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )
                            LazyRow(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(similarMovies!!.size) {
                                    StandardBoxInRow(
                                        navController = navController,
                                        movie_poster_path = baseURL + similarMovies!![it].poster_path,
                                        movieTitle = similarMovies!![it].title,
                                        scope = scope,
                                        movieId = similarMovies!![it].id.toString()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WatchAndRecommend(viewModel: MovieDetailViewModel, movieId: String) {
    var hasRecommended by remember { mutableStateOf(false) }
    val watchedBool by viewModel.watchedBool.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 7.dp, bottom = 5.dp)
            .height(40.dp)
    ) {
        // The watch button
        Button(
            // The onClick is working as intended
            onClick = { viewModel.updateWatchedBool(movieId) },
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(1f),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = if (watchedBool) Icons.Filled.Visibility else Icons.Outlined.VisibilityOff,
                contentDescription = "Watched",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                text = if (watchedBool) "Watched" else "Not Watched",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        // The recommend button
        Button(
            // The onClick is working as intended
            onClick = { hasRecommended = !hasRecommended },
            modifier = Modifier
                .padding(end = 10.dp)
                .weight(1f),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceVariant),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector = if (hasRecommended) Icons.Filled.ThumbUp else Icons.Outlined.ThumbUpAlt,
                contentDescription = "Recommended",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                text = if (hasRecommended) "Recommended" else "Not Recommended",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}



@Composable
fun MovieDescription(movie: TMDBMovieDetail, viewModel: MovieDetailViewModel, movieId: String) {
    val genreIds = convertGenresToIntList(movie.genres)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 10.dp)
        ) {
            Text(
                text = movie.title,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge
            )
        }
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .height(187.dp)
        ) {
            AsyncImage(
                model = baseURL + movie.poster_path,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.composeImageModifier()
            )
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxHeight(),
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        GenreTags(genreIds)
                    }
                }
                ExpandableTextDescription(movie.overview)
                //RatingAndBookmark(viewModel = viewModel, movieId = movieId)
            }
        }
        /*
        Text(
            text = "Release Date: ${movie.release_date}",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 2.dp, top = 5.dp)
        )
         */
    }
}

@Composable
fun RatingAndBookmark(viewModel: MovieDetailViewModel, movieId: String) {
    var isRating by remember { mutableStateOf(false) }
    val isInWatchlist by viewModel.isInWatchlist.collectAsState()
    val dataForRating by viewModel.movieRating.collectAsState()

    //Inner Function
    fun Double?.toDisplayString(): String {
        return when {
            this == null -> "0.0"
            else -> String.format("%.1f", this)
        }
    }

    if (isRating) {
        RatingDialog(onDismissRequest = { isRating = false }, viewModel = viewModel, movieId = movieId)
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // "Rate" button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(vertical = 6.dp)
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    modifier = Modifier.height(30.dp),
                    onClick = { isRating = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        "Rate",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            // Star and count
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "star",
                    tint = Color.Yellow
                )
                Column(
                    modifier = Modifier.padding(start = 7.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Log.d("Avg","${dataForRating?.average}")
                    Text(
                        text = if (dataForRating?.average?.isNaN() != true) "${dataForRating?.average.toDisplayString()}/10" else "0.0/10",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "${dataForRating?.amount}",
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }

            }
            // "Bookmark" Icon
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = if (isInWatchlist) Icons.Outlined.PlaylistRemove else Icons.Outlined.PlaylistAdd,
                    contentDescription = "Bookmark",
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .clickable {
                            if (isInWatchlist) {
                                viewModel.removeFromWatchlist(movieId)
                                viewModel.checkIfInWatchlist(movieId)
                            } else {
                                viewModel.addToWatchlist(movieId)
                                viewModel.checkIfInWatchlist(movieId)
                            }
                        }
                )
            }
        }
    }
}

@Composable
fun RatingDialog(onDismissRequest: () -> Unit, viewModel: MovieDetailViewModel, movieId: String) {
    val userRating by viewModel.movieUserRating.collectAsState()
    var tempRating by remember { mutableFloatStateOf(userRating?.toFloat() ?: 0f) }
    var chooseRating: Int by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .padding(4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Rating",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(10.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = { onDismissRequest() }) {
                        Icon(Icons.Filled.Close, "Exit")
                    }
                }
                Divider(modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                // Rating bar
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //This is a library we have imported from GITHUB
                    RatingBar(
                        modifier = Modifier.padding(10.dp),
                        value = tempRating,
                        style = RatingBarStyle.Fill(),
                        onValueChange = {
                            tempRating = it
                        },
                        onRatingChanged = {
                            Log.d("TAG", "onRatingChanged: $it")
                            chooseRating = it.toInt()
                        },
                        numOfStars = 10,
                        size = 24.dp,
                        spaceBetween = 5.dp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(
                            "Cancel",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    TextButton(
                        onClick = {
                            onDismissRequest()
                            Log.w("Rating", "Rating: $chooseRating")
                            viewModel.rateMovie(movieId, chooseRating)

                        }
                    ) {
                        Text(
                            "Confirm",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

//The design of the description pop-up
@Composable
fun DescriptionDialog(onDismissRequest: () -> Unit, text: String) {
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                //.height(450.dp)
                .padding(4.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface
            )
        ) {
            LazyColumn {
                item{
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(10.dp)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { onDismissRequest() }) {
                            Icon(Icons.Filled.Close, "Exit")
                        }
                    }
                }
                item{
                    Divider(modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                item{
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun ExpandableTextDescription(text: String, maxLength: Int = 250) {
    var isExpanded by remember { mutableStateOf(false) }

    val displayText = if (text.length <= maxLength) {
        text
    } else {
        text.take(maxLength) + "..."
    }

    if (isExpanded) {
        DescriptionDialog(onDismissRequest = { isExpanded = false }, text)
    }

    Column {
        Text(
            text = displayText,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 7,
            softWrap = true,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 10.dp)
                .clickable { isExpanded = true }
        )
        if (text.length > maxLength && !isExpanded) {
            Text(
                text = stringResource(R.string.view_more),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                modifier = Modifier
                    .clickable { isExpanded = true }
                    .padding(top = 5.dp),
                textDecoration = TextDecoration.Underline
            )
        }
    }
}


@Composable
private fun Modifier.composeImageModifier(): Modifier {
    return this
        .shadow(elevation = 4.dp, spotColor = MaterialTheme.colorScheme.background)
        .border(1.dp, Color(0xFF000000), RoundedCornerShape(10.dp))
        .height(180.dp)
        .clip(RoundedCornerShape(10.dp))
}
//can be made more customizable by making the textlength decide the length of the box
@Composable
fun GenreTags(genreIds: List<Int>) {
    val genres = APIHandler().getGenrebyID(genreIds)
    Row {
        for (genre in genres) {
            TagBox(shape = RoundedCornerShape(4.dp), tag = genre)
            Spacer(modifier = Modifier.width(8.dp)) // Add space between tags
        }
    }
}

// Functions that convert the Api lists into the one you want
fun convertGenresToIntList(genres: List<Genre>): List<Int> {
    return genres.map { it.id }
}
fun convertCrewToStringList(crew: List<Crew>?): List<String> {
    return crew?.map { it.toString() } ?: emptyList()
}

@Composable
fun TagBox(shape : Shape, tag : String) {
    Row(modifier = Modifier
        .height(15.dp)
        .width(80.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .clip(shape)
            .background(color = Color(0xFF282727))) {
            Text(text = tag,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 12.sp)
        }
    }
}
