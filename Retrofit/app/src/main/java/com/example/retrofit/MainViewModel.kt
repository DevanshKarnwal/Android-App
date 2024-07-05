package com.example.retrofit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _categoryState = mutableStateOf(RecipeState())
    val categoryState: State<RecipeState> = _categoryState

    init{
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch{
            try{
                val response = recipeService.getCategories()
                _categoryState.value = _categoryState.value.copy(isLoading = false, categories = response.categories,error = null)
            }catch ( e: Exception ){
                _categoryState.value = _categoryState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    data class RecipeState(
        val isLoading: Boolean = true,
        val categories: List<Category> = emptyList(),
        val error: String? = null
    )
}