package com.example.cv2.data

import java.io.Serializable

class Entry(
    val id: Long?,
    val lat: Float,
    val lon: Float,
    val tags: Tag
) : Serializable {
}