package com.example.cv2.data.jsonmapper

import java.io.Serializable

class Entry(
    val id: Long?,
    val lat: Double?,
    val lon: Double?,
    val tags: Tag
) : Serializable {
}