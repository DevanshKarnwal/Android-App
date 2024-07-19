package com.example.livechat.Screens

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.livechat.LCViewModel

@Composable
fun ChatListScreen(navController: NavController , vm : LCViewModel) {
    BottomAppBar {
        BottomNavigationMenu(selectedItem = BottomNavigationItem.CHATLIST , navController =navController )
    }
}