package com.example.whislist.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wish_table")
data class Wish(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0L,
    @ColumnInfo(name = "wish-title")
    val title : String,
    @ColumnInfo(name = "wish-desc")
    val description : String = ""
)

object DummyWish{
    val wishList = listOf(
        Wish(title = "Google Watch 2" , description = "An android watch"),
        Wish(title = "Oculus quest 2" , description = "A vr headset for playing"),
        Wish(title = "A book" , description = "A science fiction book from best seller"),
        Wish(title = "Bean Bag" , description = "A comfy bean bag to substitue for a chair"),
    )
}