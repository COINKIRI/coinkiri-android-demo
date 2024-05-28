package com.cokiri.coinkiri.util

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import java.util.Base64

//base64로 인코딩된 이미지를 painter로 변환
fun byteArrayToPainter(string: String?): BitmapPainter {
    val byteArraySymbolImage = Base64.getDecoder().decode(string)
    val bitmap =
        BitmapFactory.decodeByteArray(byteArraySymbolImage, 0, byteArraySymbolImage.size)
    return BitmapPainter(bitmap.asImageBitmap())
}