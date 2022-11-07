package com.example.cv2.data.jsonmapper

import com.google.gson.annotations.SerializedName

class Tag(
    val name: String?,
    val opening_hours: String?,
    val phone: String?,
    val website: String?,
    @SerializedName("addr:city") val city: String?,
    @SerializedName("addr:housenumber") val houseNumber: String?,
    @SerializedName("addr:street") val street: String?,
    @SerializedName("addr:country") val country: String?
) {
}