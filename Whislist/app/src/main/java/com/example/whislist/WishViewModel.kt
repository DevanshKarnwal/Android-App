package com.example.whislist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whislist.Data.Wish
import com.example.whislist.Data.WishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class WishViewModel(
    private val wishRepository: WishRepository = Graph.wishRepository
) : ViewModel(){

    var wishTitleState by mutableStateOf("")
    var wishDescriptionState by mutableStateOf("")

    fun onWishTitleChange(newString:String ){
        wishTitleState = newString
    }
    fun onWishDescriptionState(newString:String ){
        wishDescriptionState = newString
    }

    lateinit var getAllWishes : Flow<List<Wish>>
    init{
        viewModelScope.launch {
            getAllWishes = wishRepository.getWishes()
        }
    }

    fun addWish(wish: Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.addAwish(wish)
        }
    }
    fun getAWishById(id : Long) : Flow<Wish>{
        return wishRepository.getWishById(id)
    }
    fun updateWish(wish : Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.updateAwish(wish)
        }
    }
    fun deleteWish(wish : Wish){
        viewModelScope.launch(Dispatchers.IO) {
            wishRepository.deleteAwish(wish)
        }
    }

}