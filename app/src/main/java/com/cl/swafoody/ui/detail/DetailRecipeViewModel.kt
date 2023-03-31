package com.cl.swafoody.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.data.source.remote.response.RecipeInformationItem
import com.cl.swafoody.network.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class DetailRecipeViewModel() : ViewModel() {
    private val _dataRecipe = MutableLiveData<ResultState<RecipeInformationItem>>()
    val dataRecipe : LiveData<ResultState<RecipeInformationItem>> = _dataRecipe

    fun getRecipeInformation(id: Int) {
        _dataRecipe.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = ApiConfig.getApiService().getRecipeInformation(id)
                _dataRecipe.postValue(ResultState.Success(recipe))
            } catch (e: Exception) {
                _dataRecipe.postValue(ResultState.Failure(e))
            }
        }
    }
}