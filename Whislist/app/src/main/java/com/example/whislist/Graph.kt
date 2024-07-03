package com.example.whislist

import android.content.Context
import androidx.room.Room
import com.example.whislist.Data.WishDatabase
import com.example.whislist.Data.WishRepository


// represent dependency injection
object Graph {
    lateinit var database : WishDatabase

    val wishRepository by lazy {
        WishRepository(database.wishDao())
    }

    fun provide(context : Context){
        database = Room.databaseBuilder(context, WishDatabase::class.java, "wishlist.db").build()
    }

}