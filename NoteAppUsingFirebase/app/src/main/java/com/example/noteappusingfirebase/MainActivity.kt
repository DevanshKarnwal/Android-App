package com.example.noteappusingfirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteappusingfirebase.detail.DetailViewModel
import com.example.noteappusingfirebase.home.HomeViewModel
import com.example.noteappusingfirebase.home.Navigation
import com.example.noteappusingfirebase.login.LoginViewModel
import com.example.noteappusingfirebase.ui.theme.NoteAppUsingFirebaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val loginViewModel : LoginViewModel = viewModel()
            val homeViewModel : HomeViewModel = viewModel()
            val detailViewModel : DetailViewModel = viewModel()
            NoteAppUsingFirebaseTheme {
                Navigation(loginViewModel =loginViewModel,
                    detailViewModel = detailViewModel,
                    homeViewModel = homeViewModel)
            }
        }
    }
}
