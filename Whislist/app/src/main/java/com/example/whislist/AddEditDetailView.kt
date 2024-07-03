package com.example.whislist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.whislist.Data.Wish
import kotlinx.coroutines.launch

@Composable
fun AddEditDetailView(
    id: Long,
    viewModel: WishViewModel,
    navController: NavController
){
    val snackMessage = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()    // for asynchronous task
    val scaffoldState = rememberScaffoldState()
    if(id !=0L){
        val wish = viewModel.getAWishById(id).collectAsState(initial = Wish(0L,"",""))
        viewModel.wishTitleState = wish.value.title
        viewModel.wishDescriptionState = wish.value.description
    }else{
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBarView(title = if(id!= 0L) "UpdateWish" else "Add Wish"
                    ,   {navController.navigateUp()}
        )
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            ) {
                       Spacer(modifier = Modifier.height(10.dp) )
                    WishTextField(label = "Title", value = viewModel.wishTitleState, onValueChanged = {
                        viewModel.onWishTitleChange(it)
                    })
                WishTextField(label = "Description", value = viewModel.wishDescriptionState, onValueChanged = {
                viewModel.onWishDescriptionState(it)
            })
            Spacer(modifier = Modifier.height(10.dp) )
            Button(onClick = {
                if(viewModel.wishTitleState.isNotEmpty() && viewModel.wishTitleState.isNotEmpty()){
                    if(id!= 0L){
                            viewModel.updateWish(Wish(id,viewModel.wishTitleState.trim(),viewModel.wishDescriptionState.trim()))
                    }
                    else{
                        // To add
                        viewModel.addWish(
                            Wish(
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                        snackMessage.value = "Wish has been created"
                    }
                }
                else{
                        // enter field for wiah to be created
                        snackMessage.value = "Enter fields for wish to be created"
                }
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(snackMessage.value)
                    navController.navigateUp() }
            }
            ) {
                Text(
                    text = if (id != 0L) "UpdateWish" else "Add Wish",
                    style = TextStyle(fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun WishTextField(
    label : String,
    value : String,
    onValueChanged : (String) -> Unit,
    modifier : Modifier = Modifier
){
        OutlinedTextField(value = value, onValueChange = onValueChanged
            , label = { Text(text = label, color = Color.Black)},
            modifier = modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
            backgroundColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Black
            )
        )
}