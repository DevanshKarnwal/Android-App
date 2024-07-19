package com.example.livechat.Screens

import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.livechat.LCViewModel

@Composable
fun StatusScreen(modifier: Modifier = Modifier, vm: LCViewModel, navController: NavHostController) {


    BottomAppBar {
        BottomNavigationMenu(selectedItem = BottomNavigationItem.STATUSLIST , navController =navController )
    }
}