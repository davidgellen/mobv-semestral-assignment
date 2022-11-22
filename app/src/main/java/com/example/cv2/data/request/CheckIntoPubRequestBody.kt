package com.example.cv2.data.request

data class CheckIntoPubRequestBody(
    val id: String,
    val name: String,
    val type: String,
    val lat: Double,
    val lon: Double
) {
}