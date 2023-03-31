package com.cl.swafoody.ui.recipe


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cl.swafoody.data.RecipeRepository
import com.cl.swafoody.data.source.local.entity.RecipeEntity
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.data.source.remote.response.RecipeItem
import com.cl.swafoody.network.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RecipeViewModel(private val recipeRepository: RecipeRepository) : ViewModel() {

    private val _dataRecipe = MutableLiveData<ResultState<List<RecipeItem>>>()
    val dataRecipe : LiveData<ResultState<List<RecipeItem>>> = _dataRecipe

    private val _dataFilter = MutableLiveData<ResultState<List<RecipeItem>>>()
    val dataFilter : LiveData<ResultState<List<RecipeItem>>> = _dataFilter

    fun getRecipeSaved() = recipeRepository.getRecipeSaved()

    fun getBookmarkedRecipe() = recipeRepository.getBookmarkedRecipe()

    fun saveRecipe(recipe: RecipeItem) {
        viewModelScope.launch {
            recipeRepository.setRecipeBookmark(recipe, true)
        }
    }

    fun deleteRecipe(recipe: RecipeItem) {
        viewModelScope.launch {
            recipeRepository.setRecipeBookmark(recipe, false)
        }
    }

    fun getRecipeByIngredients(ingredients: String) {
        _dataRecipe.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = ApiConfig.getApiService().getRecipeByIngredients(ingredients)
                val data = recipe.results
                _dataRecipe.postValue(ResultState.Success(data))
            } catch (e: Exception) {
                _dataRecipe.postValue(ResultState.Failure(e))
            }
        }
    }

    fun getRecipeByDiet(ingredients: String, diet: String) {
        _dataFilter.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = ApiConfig.getApiService().getRecipeByDiet(ingredients, diet)
                val data = recipe.results
                _dataFilter.postValue(ResultState.Success(data))
            } catch (e: Exception) {
                _dataFilter.postValue(ResultState.Failure(e))
            }
        }
    }
}