package com.example.musicappui.ui.theme

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import com.example.musicappui.R

@Composable
fun Browse(){
    val cont = listOf("Hits" , "Happy" , "Workout","Running","TGIF","Yoga")
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(cont){
            item -> BrowserItem(cat = item, drawable = R.drawable.baseline_apps_24)
        }
    }
}