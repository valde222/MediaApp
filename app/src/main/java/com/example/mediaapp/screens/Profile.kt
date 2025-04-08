package com.example.mediaapp.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mediaapp.R
import com.example.mediaapp.ui.TabsAndFilters
import com.example.mediaapp.ui.nav.TopNavBarA
import com.example.mediaapp.ui.theme.MediaAppTheme
import com.example.mediaapp.viewModels.CurrentUserViewModel

@Composable
fun ProfilePageLayout(
    navController: NavController,
    drawerState: DrawerState,
    viewModel: CurrentUserViewModel = viewModel()) {

    val currentUser by viewModel.currentUser.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }
    val user = currentUser ?: return
    MediaAppTheme {
        Column {
            TopNavBarA(drawerState = drawerState)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 75.dp)
                    .background(color = MaterialTheme.colorScheme.surface)
            )
            {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                    ) {
                        ProfileDescription(
                            profilePicture = if (user.user.profilePicture == "") R.drawable.image_not_found_picture else R.drawable.image_not_found_picture,
                            description = if (user.user.description != "") user.user.description else stringResource(
                                R.string.profile_page_no_description
                            ),
                            countryFlag = viewModel.getCountryFlag(user.user.location),
                            countryName = if (user.user.location != "") user.user.location else stringResource(
                                R.string.profile_page_unknown_location
                            ),
                            followers = user.user.followers.size,
                            following = user.user.following.size,
                            username = user.user.username,
                            nameOfUser = if (user.user.name != "") user.user.name else stringResource(
                                R.string.profile_page_unknown
                            )
                        )
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column {
                            Text(
                                stringResource(R.string.profile_page_favorite_movies),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )
                            Text(
                                "Feature coming soon!",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )
                            /*
                            LazyRow(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.thegodfather, R.string.thegodfather)
                                }
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.parasite, R.string.parasite)
                                }
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.thematrix, R.string.thematrix)
                                }
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.casablanca, R.string.casablanca)
                                }
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.indianajones, R.string.indianajones)
                                }
                            }
                        }*/
                        }
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(118.dp)
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column {
                            Text(
                                "Profile Statistics",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )
                            Text(
                                "Feature coming soon!",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )
                        }

                        /*ProfileStatistics(
                            R.string.watched_number,
                            R.string.reviews_number,
                            R.string.rated_number,
                            R.string.recommends_number,
                            R.string.saved_number
                        )*/
                    }
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column {
                            Text(
                                stringResource(R.string.profile_page_recently),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            )
                            Text(
                                "Feature coming soon!",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                            ) /*
                            LazyRow(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.oppenheimerposter, R.string.oppenheimer)
                                }
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.thedayaftertomorrow, R.string.thedayaftertomorrow)
                                }
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.avatarthewayofwater, R.string.avatarthewayofwater)
                                }
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.freeguy, R.string.freeguy)
                                }
                                item {
                                    StandardBoxInRowOld(navController, R.drawable.readyplayerone, R.string.readyplayerone)
                                }
                            }
                        }*/
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileStatistics(
    watched: Int,
    reviews: Int,
    rated: Int,
    recommends: Int,
    saved: Int,
) {
    Box(modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .fillMaxWidth()
        .height(118.dp)) {
        Column(modifier = Modifier.padding(start = 7.dp, end = 7.dp)) {
            Text (
                stringResource(R.string.moviecollector),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(start = 3.dp, top = 6.dp, bottom = 4.dp)
                    .height(28.dp)
            )
            Row(modifier = Modifier
                .height(66.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(top = 9.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround){
                StatisticsText(watched, R.string.watched)
                StatisticsText(reviews, R.string.reviews)
                StatisticsText(rated, R.string.rated)
                StatisticsText(recommends, R.string.recommends)
                StatisticsText(saved, R.string.saved)
            }
        }
    }
}

@Composable
private fun StatisticsText(number: Int, text: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            stringResource(number),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 9.dp, top = 0.dp)
                .height(28.dp)
        )
        Text(
            stringResource(text),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(start = 9.dp, top = 0.dp)
                .height(28.dp)
        )
    }
}

@Composable
fun ProfileDescription(
    profilePicture: Int,
    description: String,
    countryFlag: Int,
    countryName: String,
    followers: Int,
    following: Int,
    username: String,
    nameOfUser: String) {
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        EditProfile(onDismissRequest = { isEditing = false })
    }
    Box(modifier = Modifier
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colorScheme.surfaceVariant)
        .fillMaxWidth()
        .wrapContentHeight()) {
        Row(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier
                .width(145.dp)
                .wrapContentHeight()
                .padding(bottom = 5.dp)) {
                Image(
                    painter = painterResource(profilePicture),
                    contentDescription = "profile_picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(start = 9.dp, top = 7.dp, bottom = 3.dp)
                        .fillMaxWidth()
                        .height(119.dp)
                        .clip(RoundedCornerShape(25.dp))
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    FollowText(
                        stringResource(R.string.followers),
                        paddingValue = 9.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End
                    )
                    FollowText(
                        followers.toString(),
                        paddingValue = 0.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Start
                    )

                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    FollowText(
                        stringResource(R.string.following),
                        paddingValue = 9.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End
                    )
                    FollowText(
                        following.toString(),
                        paddingValue = 0.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End
                    )
                }
            }
            Column {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                        .height(32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text (
                        text = username,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    IconButton(onClick = { isEditing = true }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "edit_button",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(24.dp)

                        )
                    }
                }

                Row(modifier = Modifier
                    .padding(start = 10.dp, bottom = 5.dp)
                    .height(13.dp)
                    .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {

                    if (countryFlag != -1) {
                        Image(modifier = Modifier
                            .size(13.dp, 8.dp)
                            .padding(start = 0.dp, top = 0.dp),
                            painter = painterResource(id = countryFlag),
                            contentDescription = "country_flag")
                    }
                    Text (
                        text = countryName,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
                Text (
                    text = nameOfUser,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(start = 10.dp, end = 8.dp)
                )
                Text (
                    text = description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier
                        .padding(start = 10.dp, top = 5.dp)
                        .width(200.dp)
                )
            }
        }
    }
}

@Composable
fun EditProfile(onDismissRequest: () -> Unit, viewModel: CurrentUserViewModel = viewModel()) {
    val countryList = viewModel.getCountryNames()
    val pictures : List<String> = listOf<String>()
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(10.dp))
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp, start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.edit_profile),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
                IconButton(onClick = {
                    onDismissRequest()
                    viewModel.desc = ""
                    viewModel.name = ""
                    viewModel.username = ""
                    viewModel.location = ""}) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close_button",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
            TextfieldForEditUsername(viewModel = viewModel)
            TextfieldForEditName(viewModel = viewModel)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 29.dp, top = 16.dp, end = 29.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DropdownLocation(filterOption = TabsAndFilters.FilterOption(
                    stringResource(R.string.location),
                    stringResource(R.string.location),
                    countryList,
                ), viewModel = viewModel)
                DropdownLocation(filterOption = TabsAndFilters.FilterOption(
                    stringResource(R.string.picture),
                    stringResource(R.string.picture),
                    pictures,
                ), viewModel = viewModel)
            }

            TextfieldForEditDesc(viewModel = viewModel)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(start = 29.dp, end = 29.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        onDismissRequest()
                        viewModel.desc = ""
                        viewModel.name = ""
                        viewModel.username = ""
                        viewModel.location = ""}
                )
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        viewModel.updateAccountDetails()
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

@Composable
fun DropdownLocation(filterOption: TabsAndFilters.FilterOption, viewModel: CurrentUserViewModel) {
    var selectedOption by remember { mutableStateOf(filterOption.label) }
    var expanded by remember { mutableStateOf(false)}
    val (backgroundColor, textColor) = getDropdownColors(expanded)
    val itemHeight = 48.dp
    val dropdownHeight = itemHeight * (if (filterOption.options.size < 8) filterOption.options.size else 6)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
    ) {
        DropdownMenuToggle(
            label = selectedOption,
            expanded = expanded,
            onToggle = { expanded = !expanded },
            textColor = textColor
        )
        if (expanded) {
            Popup(
                onDismissRequest = { expanded = false },
                alignment = Alignment.TopStart,
                offset = IntOffset(10, 100)
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(start = 6.dp, top = 3.dp, bottom = 3.dp)
                        .wrapContentWidth()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(max = dropdownHeight)
                            .wrapContentWidth()) {
                        items(filterOption.options.size) { option ->
                            DropdownMenuItem(
                                text = { Text(text = filterOption.options[option]) },
                                modifier = Modifier
                                    .height(itemHeight)
                                    .wrapContentWidth(),
                                onClick = {
                                    expanded = false
                                    selectedOption = filterOption.options[option]
                                    viewModel.location = filterOption.options[option]
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuToggle(
    label: String,
    expanded: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, top = 6.dp, end = 8.dp, bottom = 6.dp)
            .clickable { onToggle(!expanded) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        val shortlabel = if (label.length > 18) label.substring(0, 18) + "..." else label
        Text(
            text = shortlabel,
            style = MaterialTheme.typography.labelLarge,
            overflow = TextOverflow.Ellipsis,
        )
        Icon(
            Icons.Filled.ArrowDropDown,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier
                .padding(1.dp)
                .width(18.dp)
                .height(18.dp)
                .rotate(if (expanded) 180f else 0f))
    }
}

@Composable
private fun getDropdownColors(expanded: Boolean): Pair<Color, Color> {
    val backgroundColor = animateColorAsState(if (expanded) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        label = ""
    ).value
    val textColor = animateColorAsState(if (expanded) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        label = ""
    ).value
    return Pair(backgroundColor, textColor)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextfieldForEditUsername(viewModel: CurrentUserViewModel) {
    var username by remember { mutableStateOf(TextFieldValue()) }

    TextField(
        modifier = textFieldModifier(),
        value = username,
        onValueChange = { newValue ->
            username = newValue
            viewModel.username = newValue.text},
        label = {labelStyle("Username")},
        placeholder = {placeholderStyle("Enter your username")},
        colors = textFieldColors(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextfieldForEditName(viewModel: CurrentUserViewModel) {
    var name by remember { mutableStateOf(TextFieldValue()) }

    TextField(
        modifier = textFieldModifier(),
        value = name,
        onValueChange = { newValue ->
            name = newValue
            viewModel.name = newValue.text},
        label = {labelStyle("Name")},
        placeholder = {placeholderStyle("Enter your name")},
        colors = textFieldColors(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
private fun TextfieldForEditDesc(viewModel: CurrentUserViewModel) {
    var desc by remember { mutableStateOf(TextFieldValue()) }

    TextField(
        modifier = Modifier
            .padding(start = 29.dp, top = 16.dp, end = 29.dp, bottom = 16.dp)
            .fillMaxWidth()
            .height(198.dp)
            .clip(RoundedCornerShape(10.dp)),
        value = desc,
        onValueChange = { newValue ->
            desc = newValue
            viewModel.desc = newValue.text},
        label = {labelStyle("Description")},
        placeholder = {placeholderStyle("Enter a description")},
        colors = textFieldColors(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
private fun textFieldModifier() = Modifier
    .padding(start = 29.dp, top = 16.dp, end = 29.dp)
    .fillMaxWidth()
    .height(56.dp)
    .clip(RoundedCornerShape(10.dp))

@Composable
private fun labelStyle(text: String) = Text(
    text = text,
    color = MaterialTheme.colorScheme.primary,
    style = MaterialTheme.typography.labelMedium
)

@Composable
private fun placeholderStyle(text: String) = Text(
    text = text,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
    fontStyle = FontStyle.Italic,
    style = MaterialTheme.typography.titleMedium,
    letterSpacing = 0.5.sp
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
    unfocusedIndicatorColor = Color.Transparent,
    focusedLabelColor = MaterialTheme.colorScheme.primary,
    unfocusedLabelColor = MaterialTheme.colorScheme.primary
)

@Composable
private fun FollowText(text: String, paddingValue: Dp, color: Color, textAlign: TextAlign) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight(400),
        color = color,
        textAlign = textAlign,
        modifier = Modifier
            .padding(start = paddingValue, top = 5.dp)
    )


}