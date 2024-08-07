package com.example.livechat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.livechat.Screens.ChatListScreen
import com.example.livechat.Screens.LoginScreen
import com.example.livechat.Screens.Profile
import com.example.livechat.Screens.SignUpScreen
import com.example.livechat.Screens.SingleChatScreen
import com.example.livechat.ui.theme.LiveChatTheme
import dagger.hilt.android.AndroidEntryPoint


sealed class DestinationScreens(val route: String) {
    object SignUp : DestinationScreens("signup")
    object Login : DestinationScreens("login")
    object Profile : DestinationScreens("profile")
    object ChatList : DestinationScreens("chatList")
    object SingleChat : DestinationScreens("singleChat/{chatId}") {
        fun createRoute(id: String) = "singleChat/$id"
    }


}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigation() {
        val navController = rememberNavController()
        var vm = hiltViewModel<LCViewModel>()
        NavHost(navController = navController, startDestination = DestinationScreens.SignUp.route) {

            composable(DestinationScreens.SignUp.route) {
                SignUpScreen(navController, vm)
            }
            composable(DestinationScreens.Login.route) {
                LoginScreen(vm = vm, navController = navController)
            }
            composable(DestinationScreens.ChatList.route) {
                ChatListScreen(navController = navController, vm = vm)
            }
            composable(DestinationScreens.SingleChat.route) {
                val chatId = it.arguments?.getString("chatId")
                chatId?.let {
                    SingleChatScreen(navController, vm, chatId)
                }
            }
            composable(DestinationScreens.Profile.route) {
                Profile(navController = navController, vm = vm)
            }

        }
    }

}

