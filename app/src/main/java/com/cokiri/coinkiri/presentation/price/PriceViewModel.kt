package com.cokiri.coinkiri.presentation.price

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokiri.coinkiri.domain.model.Coin
import com.cokiri.coinkiri.domain.usecase.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class PriceViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
) : ViewModel() {

    private val _coinList = MutableStateFlow<List<Coin>>(emptyList())      // 코인 데이터를 담는 StateFlow
    val coinList: StateFlow<List<Coin>> = _coinList

    init {
        loadCoins()             // 코인 데이터를 가져옴
    }

    private fun loadCoins() {
        viewModelScope.launch {
            val coins = getCoinsUseCase()
            _coinList.value = coins
        }
    }

    fun byteArrayToPainter(string: String): BitmapPainter {
        val byteArraySymbolImage = Base64.getDecoder().decode(string)
        val bitmap = BitmapFactory.decodeByteArray(byteArraySymbolImage, 0, byteArraySymbolImage.size)
        return BitmapPainter(bitmap.asImageBitmap())
    }
}