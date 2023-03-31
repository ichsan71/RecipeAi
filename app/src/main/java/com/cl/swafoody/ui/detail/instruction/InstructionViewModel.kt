package com.cl.swafoody.ui.detail.instruction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cl.swafoody.data.source.remote.ResultState
import com.cl.swafoody.data.source.remote.response.InstructionResponse
import com.cl.swafoody.data.source.remote.response.NutritionResponse
import com.cl.swafoody.network.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class InstructionViewModel: ViewModel() {

    private val _dataInstruction = MutableLiveData<ResultState<InstructionResponse>>()
    val dataInstruction : LiveData<ResultState<InstructionResponse>> = _dataInstruction

    fun getRecipeInstruction(id: Int) {
        _dataInstruction.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = ApiConfig.getApiService().getRecipeInstruction(id)

                _dataInstruction.postValue(ResultState.Success(recipe))
            } catch (e: Exception) {
                _dataInstruction.postValue(ResultState.Failure(e))
            }
        }
    }
}