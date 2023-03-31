package com.cl.swafoody.ui.detail.nutritions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.data.source.remote.response.NutritionResponse
import com.cl.swafoody.network.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class NutritionViewModel : ViewModel() {
    private val _dataNutrition = MutableLiveData<ResultState<NutritionResponse>>()
    val dataNutrition : LiveData<ResultState<NutritionResponse>> = _dataNutrition

    fun getRecipeNutrition(id: Int) {
        _dataNutrition.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = ApiConfig.getApiService().getRecipeNutrition(id)
                _dataNutrition.postValue(ResultState.Success(recipe))
            } catch (e: Exception) {
                _dataNutrition.postValue(ResultState.Failure(e))
            }
        }
    }
}