package com.example.noteappusingfirebase.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteappusingfirebase.models.Notes
import com.example.noteappusingfirebase.repository.Resource
import com.example.noteappusingfirebase.repository.StorageRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository : StorageRepository = StorageRepository()
)  : ViewModel(){
    var homeUiState by mutableStateOf(HomeUiState())
    val user = repository.user()
    val hasUser : Boolean
        get() = repository.hasUser()
    private val userId : String
    get() = repository.getUserId()
    fun loadNotes(){
        if(hasUser){
            if(userId.isNotBlank()){
                getUserNotes(userId)
            }
        }else{
            homeUiState = homeUiState.copy(notesList = Resource.Error(Throwable("User not found")))
        }
    }
    private fun getUserNotes(userId : String) = viewModelScope.launch {
        repository.getUserNotes(userId).collect{
            homeUiState = homeUiState.copy(notesList = it)
        }
    }

    fun deleteNote(noteId : String) = repository.deleteNote(noteId){
        homeUiState = homeUiState.copy(notDeletedStatus = it)
    }

    fun signOut(){
        repository.signOut()
    }


}

data class HomeUiState(
    val notesList : Resource<List<Notes>>  = Resource.Loading(),
    val notDeletedStatus :Boolean = false
)