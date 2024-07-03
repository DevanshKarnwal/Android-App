package com.example.whislist

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Scaffold
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.whislist.Data.DummyWish
import com.example.whislist.Data.Wish

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navController: NavController,
    viewModel : WishViewModel
){
    val context = LocalContext.current
    Scaffold(
        topBar = { AppBarView(title = "WishList", {
                Toast.makeText(context, "Button Clicked", Toast.LENGTH_LONG).show()
        }) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.White,
                backgroundColor = Color.Black,
                onClick = { Toast.makeText(context, " + Button Clicked", Toast.LENGTH_LONG).show()
                        navController.navigate(add_screen(0L))
                }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            }
        }
    ) {
        val wishlist = viewModel.getAllWishes.collectAsState(initial = emptyList())
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
                        items(wishlist.value){
                            wish ->
                            val dismissState = rememberDismissState(
                                confirmStateChange = {
                                    if(it == DismissValue.DismissedToEnd || it == DismissValue.DismissedToStart){
                                    viewModel.deleteWish(wish)
                                }
                                true
                                }
                            )
                            SwipeToDismiss(
                                state = dismissState,
                                background = {},
                                directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
                                dismissThresholds = { FractionalThreshold(0.5f) },
                                dismissContent = {
                                    WishItem(wish = wish) {
                                        val id = wish.id
                                        navController.navigate(add_screen(id))
                                    }
                                }
                                )
                            WishItem(wish = wish) {
                                val id = wish.id
                                navController.navigate(add_screen(id))
                            }
                        }
        }
    }

}

@Composable
fun WishItem (wish : Wish, onClick : () -> Unit ){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .clickable { onClick() }
            , elevation = 10.dp,
            backgroundColor = Color.White
        ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(text = wish.title , fontWeight = FontWeight.ExtraBold)
                    Text(text = wish.description)
                }
        }
}