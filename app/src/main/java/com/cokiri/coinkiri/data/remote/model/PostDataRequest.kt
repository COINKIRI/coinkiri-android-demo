package com.cokiri.coinkiri.data.remote.model

data class PostDataRequest(
    val title: String,
    val content: String,
    val images: List<ImageData>
)

data class ImageData(
    val position: Int,
    val base64: String
)
