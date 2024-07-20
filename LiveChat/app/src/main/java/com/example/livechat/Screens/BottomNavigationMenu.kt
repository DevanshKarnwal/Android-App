package com.example.livechat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livechat.DestinationScreens
import com.example.livechat.R
import com.example.livechat.navigateTo


enum class BottomNavigationItem(val icon: Int, val navdestinationScreens: DestinationScreens) {
    CHATLIST(R.drawable.msg, DestinationScreens.ChatList),
    PROFILE(R.drawable.user, DestinationScreens.Profile)
}


@Composable
fun BottomNavigationMenu(
    selectedItem: BottomNavigationItem,
    navController: NavController,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (item in BottomNavigationItem.values()) {
            Image(
                painter = painterResource(id = item.icon),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .weight(1f)
                    .clickable { navigateTo(navController, item.navdestinationScreens.route) },
                colorFilter = if (item == selectedItem) {
                    ColorFilter.tint(Color.Black)
                } else {
                    ColorFilter.tint(Color.Gray)
                }
            )
        }
    }
}