package com.example.mediaapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mediaapp.R
import com.example.mediaapp.ui.nav.TopNavBarB
import com.example.mediaapp.ui.theme.MediaAppTheme

@Composable
fun YoufollowPageLayout(navController: NavController, drawerState: DrawerState){
    MediaAppTheme {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)) {
            Column {
                TopNavBarB(navController = navController, drawerState = drawerState)
                Text(
                    text = stringResource(id = R.string.youfollow),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(start = 18.dp, bottom = 10.dp, top = 10.dp)
                )
                Text(
                        "Feature coming soon!",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(start = 10.dp, top = 5.dp)
                )/*
                searchBar(R.string.searchtext)
                LazyColumn() {
                    item {
                        ProfileListItem(name = R.string.benjamin, iconColor = R.color.purple, true)
                    }
                    item {
                        ProfileListItem(name = R.string.kevin, iconColor = R.color.purple, true)
                    }
                    item {
                        ProfileListItem(name = R.string.valdemar, iconColor = R.color.lightgreen, true)
                    }
                    item {
                        ProfileListItem(name = R.string.simon, iconColor = R.color.red, true)
                    }
                    item {
                        ProfileListItem(name = R.string.david, iconColor = R.color.blue, true)
                    }
                    item {
                        ProfileListItem(name = R.string.jonathan, iconColor = R.color.teal, true)
                    }
                    item {
                        ProfileListItem(name = R.string.patrick, iconColor = R.color.lightpurple, true)
                    }
                    item {
                        ProfileListItem(name = R.string.charlie, iconColor = R.color.blue, true)
                    }
                    item {
                        ProfileListItem(name = R.string.mikkel, iconColor = R.color.lightpurple, true)
                    }
                    item {
                        ProfileListItem(name = R.string.tanja, iconColor = R.color.purple, true)
                    }
                    item {
                        ProfileListItem(name = R.string.valde, iconColor = R.color.purple, true)
                    }
                    item {
                        ProfileListItem(name = R.string.mads, iconColor = R.color.purple, true)
                    }
                }*/
            }
        }
    }
}
private val stdWidthHeightFollowPage = Modifier
    .fillMaxWidth()
    .height(56.dp)

@Composable
fun ProfileListItem(name: Int, iconColor: Int, followstatus: Boolean) {
    var isFollowing by remember { mutableStateOf(followstatus) }
    var tempString = stringResource(id = name)

    Row(modifier = stdWidthHeightFollowPage
        .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween ){

        Row(horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp)) {
            Box(modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colorResource(id = iconColor)),
                contentAlignment = Alignment.Center
            ) {
                Text (
                    text = tempString[0].toString(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                )
            }
            Text (
                text = stringResource(id = name),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(start = 16.dp)
            )
        }

        Icon(
            imageVector = if (isFollowing) Icons.Filled.PersonRemove else Icons.Filled.PersonAddAlt1,
            contentDescription = if (isFollowing) "Person_Remove" else "Person_Add",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .clickable {
                    // Toggle the follow status when clicked
                    isFollowing = !isFollowing
                }
                .padding(end = 16.dp)
        )

    }
}

@Composable
fun searchBar(searchText: Int) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },

        label = {
            Text(text = stringResource(id = searchText),
            color = MaterialTheme.colorScheme.onSurfaceVariant) },

        trailingIcon = { Icon(
            Icons.Filled.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant) },

        modifier = stdWidthHeightFollowPage,

        shape = RoundedCornerShape(size = 28.dp),

        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.primary)
    )
}