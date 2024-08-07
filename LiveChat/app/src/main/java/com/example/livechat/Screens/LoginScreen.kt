package com.example.livechat.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.livechat.CheckedSignedIn
import com.example.livechat.CommonProgressbar
import com.example.livechat.DestinationScreens
import com.example.livechat.LCViewModel
import com.example.livechat.R
import com.example.livechat.navigateTo

@Composable
fun LoginScreen(vm : LCViewModel, navController: NavHostController) {

        var emailState = remember { mutableStateOf("") }
        var passwordState = remember { mutableStateOf("") }
        val focus = LocalFocusManager.current

        CheckedSignedIn(vm,navController)

        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chat),
                    contentDescription = null,
                    modifier = Modifier
                        .width(200.dp)
                        .padding(top = 16.dp)
                        .padding(8.dp)
                )
                Text(
                    text = "Sign In",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    label = { Text(text = "Email") },
                    modifier = Modifier.padding(8.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text(text = "Password") },
                    modifier = Modifier.padding(8.dp),
                    singleLine = true
                )
                Button(onClick = {
                        vm.logIn(emailState.value, passwordState.value)
                }, modifier = Modifier.padding(8.dp)) {
                    Text(text = "Sign In")
                }

                Text(
                    text = "New user go to Sign up",
                    color = Color.Blue,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { navigateTo(navController, DestinationScreens.SignUp.route) })

            }
        }
        if(vm.inProgress.value) {
            CommonProgressbar()
        }

}