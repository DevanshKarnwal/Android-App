package com.example.whislist.Data

import kotlinx.coroutines.flow.Flow


class WishRepository(private val wishDao: WishDao) {
    suspend fun addAwish(wish: Wish){
        wishDao.addAWish(wish)
    }
    fun getWishes() : Flow<List<Wish>> = wishDao.getWish()

    fun getWishById(id : Long)  : Flow<Wish> {return  wishDao.getAWishById(id) }

    suspend fun updateAwish(wish: Wish){
        wishDao.updateWish(wish)
    }

    suspend fun deleteAwish(wish: Wish){
        wishDao.deleteWish(wish)
    }

}