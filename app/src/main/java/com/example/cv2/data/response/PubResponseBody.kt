package com.example.cv2.data.response

import com.google.gson.annotations.SerializedName
import java.util.*

data class PubResponseBody(
    @SerializedName("bar_id") val barId: String,
    @SerializedName("bar_name") val barName: String? = null,
    @SerializedName("lat") val lat: String? = null,
    @SerializedName("lon") val lon: String? = null,
    @SerializedName("bar_type") val barType: String? = null,
    @SerializedName("users") val users: Long,
    @SerializedName("last_update") val lastUpdate: String? = null
) {
}