package com.cl.swafoody.ui.detail.ingredient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.data.source.remote.response.IngredientResponse
import com.cl.swafoody.network.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class IngredientViewModel : ViewModel() {
    private val _dataIngredient = MutableLiveData<ResultState<IngredientResponse>>()
    val dataIngredient : LiveData<ResultState<IngredientResponse>> = _dataIngredient

    fun getRecipeIngredient(id: Int) {
        _dataIngredient.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val ingredient = ApiConfig.getApiService().getRecipeIngredient(id)
                _dataIngredient.postValue(ResultState.Success(ingredient))
            } catch (e: Exception) {
                _dataIngredient.postValue(ResultState.Failure(e))
            }
        }
    }
}