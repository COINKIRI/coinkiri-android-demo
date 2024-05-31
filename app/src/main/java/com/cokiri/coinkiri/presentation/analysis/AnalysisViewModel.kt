package com.cokiri.coinkiri.presentation.analysis

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.usecase.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase
) : ViewModel() {

    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())
    val coinList: StateFlow<List<Coin>> = _coinList.asStateFlow()



    fun loadCoins() {
        viewModelScope.launch {
            try{
                val coins = getCoinsUseCase()
                _coinList.value = coins
                Log.d("AnalysisViewModel", "_coinList.value $_coinList")
            } catch (e: Exception) {
                Log.e("PriceViewModel", "Failed to load coins : ", e)
            }
        }
    }

}