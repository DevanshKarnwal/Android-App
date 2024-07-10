package com.example.firebaseproject1.navigation

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.runtime.staticCompositionLocalOf

private val localBackPressedDispatcher = staticCompositionLocalOf<OnBackPressedDispatcherOwner?>{null}
private class ComposableBackNavigationHandler(enabled : Boolean): OnBackPressedCallback(enabled){
    lateinit var onBackPressed :()->Unit

override fun handleOnBackPressed() {
    onBackPressed()
}
}