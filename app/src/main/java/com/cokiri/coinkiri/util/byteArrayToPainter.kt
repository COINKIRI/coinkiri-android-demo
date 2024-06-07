package com.cokiri.coinkiri.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.io.ByteArrayOutputStream
import java.util.Base64

//base64로 인코딩된 이미지를 painter로 변환
fun byteArrayToPainter(string: String?): BitmapPainter {
    val byteArraySymbolImage = Base64.getDecoder().decode(string)
    val bitmap =
        BitmapFactory.decodeByteArray(byteArraySymbolImage, 0, byteArraySymbolImage.size)
    return BitmapPainter(bitmap.asImageBitmap())
}


// base64로 인코딩된 값을 문자열로 변환
fun byteArrayToString(string: String?): String {
    return String(Base64.getDecoder().decode(string))
}


suspend fun loadImageAndConvertToBase64(context: Context, uri: Uri): String {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(uri)
        .allowHardware(false)
        .build()

    val result = (imageLoader.execute(request) as SuccessResult).drawable
    val bitmap = result.toBitmap().scale(360, 360)
    return bitmapToBase64(bitmap)
}


fun Bitmap.scale(width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, true)
}


fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)
}




