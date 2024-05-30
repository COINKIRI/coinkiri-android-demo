package com.cokiri.coinkiri.util

fun insertImagesIntoContent(content: String?, images: List<Pair<Int, String>>?): String {
    val paragraphs = content?.split("(?=<p>)".toRegex())?.toMutableList()
    val imagesMap = images?.toMap()?.toSortedMap()
    var offset = 0
    if (imagesMap != null) {
        for ((pos, imgSrc) in imagesMap) {
            val actualPos = pos + offset
            if (paragraphs != null) {
                if (actualPos < paragraphs.size) {
                    paragraphs.add(actualPos, imgSrc)
                    offset++
                } else {
                    paragraphs.add(imgSrc)
                }
            }
        }
    }
    if (paragraphs != null) {
        return paragraphs.joinToString("")
    }
    return ""
}
