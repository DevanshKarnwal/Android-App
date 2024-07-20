package com.example.livechat.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.request.Options
import com.example.livechat.CommonProgressbar
import com.example.livechat.CommonRow
import com.example.livechat.DestinationScreens
import com.example.livechat.LCViewModel
import com.example.livechat.R
import com.example.livechat.data.UserData
import com.example.livechat.navigateTo

@Composable
fun ChatListScreen(navController: NavController, vm: LCViewModel) {

    var isProgress = vm.inProgressChats

    if (isProgress.value) {
        CommonProgressbar()
    } else {
        val chats = vm.chats.value
        val users = vm.userData.value
        var showDailog = remember {
            mutableStateOf(false)
        }
        val onFabClick: () -> Unit = {
            showDailog.value = true
        }
        val onDismiss: () -> Unit = {
            showDailog.value = false
        }

        val onAddChat: (String) -> Unit = {
            vm.onAddChat(it)
            showDailog.value = false
        }
        Scaffold(
            floatingActionButton = {
                fab(
                    showDailog = showDailog.value,
                    onFabClick = { onFabClick },
                    onDismiss = { onDismiss },
                    onAddChat = { onAddChat(it) }
                )
            },
            bottomBar = {
                BottomAppBar {
                    BottomNavigationMenu(
                        selectedItem = BottomNavigationItem.CHATLIST,
                        navController = navController
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                if (chats.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No Chats Available")
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        items(chats) { chat ->
                            val chatUser = if (chat.user1.userId == vm.userData.value?.userId) {
                                chat.user2
                            } else {
                                chat.user1
                            }
                            CommonRow(imageUrl = chatUser.imageUrl, name = chatUser.name) {
                                chat.chatId?.let{
                                    navigateTo(
                                        navController,
                                        DestinationScreens.SingleChat.createRoute(id = it))
                                }

                            }
                        }
                    }
                }
            }
        }


    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun fab(
    showDailog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }
    if (showDailog) {
        AlertDialog(onDismissRequest = {
            onDismiss()
            addChatNumber.value = ""
        }, confirmButton = {
            Button(onClick = { onAddChat(addChatNumber.value) }) {
                Text(text = "AddChat")
            }
        },
            title = { "Add Chat" },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value,
                    onValueChange = { addChatNumber.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )
        FloatingActionButton(
            onClick = { onFabClick },
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 10.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
        }
    }
}