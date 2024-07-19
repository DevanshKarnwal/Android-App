package com.example.livechat.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.livechat.CommonDivider
import com.example.livechat.CommonImage
import com.example.livechat.CommonProgressbar
import com.example.livechat.DestinationScreens
import com.example.livechat.LCViewModel
import com.example.livechat.navigateTo

@Composable
fun Profile(navController: NavHostController, vm: LCViewModel) {

    val inProcess = vm.inProgress.value
    if (inProcess) {
        CommonProgressbar()
    } 
        val userData = vm.userData.value
        var name by rememberSaveable {
            mutableStateOf(userData?.name ?: "")
        }
        var number by rememberSaveable {
            mutableStateOf(userData?.number ?: "")
        }
        Column {
            ProfileContent(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                vm = vm,
                name = name,
                number = number,
                onNameChange = { name = it },
                onNumberChange = { number = it },
                onBack = { navigateTo(navController, DestinationScreens.ChatList.route) },
                onSave = { vm.createOrUpdateProfile(name = name, number = number) },
                onLogOut = {
                    vm.logOut()
                    navigateTo(navController, DestinationScreens.Login.route)
                }
            )
            BottomAppBar {
                BottomNavigationMenu(
                    selectedItem = BottomNavigationItem.PROFILE,
                    navController = navController
                )
            }
        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier = Modifier,
    vm: LCViewModel,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit,
    onLogOut: () -> Unit
) {
    val imageUrl = vm.userData.value?.imageUrl
    Spacer(modifier = Modifier.height(50.dp))
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Back", modifier = Modifier.clickable { onBack() })
            Text("Save", modifier = Modifier.clickable { onSave() })
        }
        
            CommonDivider()

            ProfileImage(imageUrl = imageUrl, vm = vm)

            CommonDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Name ", modifier = Modifier.padding(end = 8.dp))
                TextField(
                    value = name,
                    onValueChange = { onNameChange(it) },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = Color.Black,
                        containerColor = Color.Transparent
                    )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Number ", modifier = Modifier.padding(end = 8.dp))
                TextField(
                    value = number,
                    onValueChange = { onNumberChange(it) },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedTextColor = Color.Black,
                        containerColor = Color.Transparent
                    )
                )
            }
            CommonDivider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "LogOut", modifier = Modifier.clickable { onLogOut() })
            }
    }
}

@Composable
fun ProfileImage(
    imageUrl: String?, vm: LCViewModel
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            vm.uploadProfileImage(uri)
        }
    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize()
                .clickable {
                    launcher.launch("image/*")
                }, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape, modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Picture")
            if (vm.inProgress.value) {
                CommonProgressbar()
            }

        }
    }
}







