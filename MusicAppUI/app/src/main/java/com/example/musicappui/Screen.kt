package com.example.musicappui

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import kotlinx.serialization.Serializable


//sealed class Screen( val title : String , val route : String) {
//    sealed class DrawerScreen(val dTitile:String , val droute : String , @DrawableRes val icon : Int)
//        : Screen(dTitile,droute){
//            object Account : DrawerScreen(
//                "Account," ,
//                "account",
//                R.drawable.ic_account
//            )
//        object Subscription : DrawerScreen(
//            "Subscription",
//            "subscribe",
//            R.drawable.ic_subscribe
//        )
//        object AddAccount : DrawerScreen(
//            "Add Account",
//            "add_account",
//            R.drawable.baseline_person_add_alt_1_24
//        )
//        }
//}

sealed class Screen(val title: String, val route: String) {
    sealed class DrawerScreen(val dTitle: String, val dRoute: String, @DrawableRes val icon: Int) : Screen(dTitle, dRoute) {
        object Account : DrawerScreen(
            "Account",
            "account",
            R.drawable.ic_account
        )

        object Subscription : DrawerScreen(
            "Subscription",
            "subscribe",
            R.drawable.ic_subscribe
        )

        object AddAccount : DrawerScreen(
            "Add Account",
            "add_account",
            R.drawable.baseline_person_add_alt_1_24
        )
    }
}

val screensInDrawer = listOf(Screen.DrawerScreen.Account, Screen.DrawerScreen.Subscription, Screen.DrawerScreen.AddAccount)