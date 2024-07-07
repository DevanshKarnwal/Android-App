package com.example.musicappui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Subscription(

){
    Column(
        modifier = Modifier.height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Subscription")
        Card(
            modifier = Modifier.padding(8.dp), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)){
                Column {
                    Text(text = "Musical")
                    Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                        Text(text = "Free Trial")
                        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "See ALl Plans")
                    }
                }

            }
        }
        Divider(modifier = Modifier.padding(horizontal = 8.dp),thickness = 1.dp)
        Row (
            Modifier.padding(16.dp),
        ){
            Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Get A Plan" )
            Text(text = "Get A Plan")
        }
    }
}