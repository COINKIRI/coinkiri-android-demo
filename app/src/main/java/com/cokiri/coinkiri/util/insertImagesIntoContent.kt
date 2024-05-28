package com.cokiri.coinkiri.util

fun insertImagesIntoContent(content: String, images: List<Pair<Int, String>>): String {
    val paragraphs = content.split("(?=<p>)".toRegex()).toMutableList()
    val imagesMap = images.toMap().toSortedMap()
    var offset = 0
    for ((pos, imgSrc) in imagesMap) {
        val actualPos = pos + offset
        if (actualPos < paragraphs.size) {
            paragraphs.add(actualPos, imgSrc)
            offset++
        } else {
            paragraphs.add(imgSrc)
        }
    }
    return paragraphs.joinToString("")
}
