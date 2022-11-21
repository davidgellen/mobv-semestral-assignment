package com.example.cv2.data.response

import com.google.gson.annotations.SerializedName

class ContactResponseBody (
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val username: String,
    @SerializedName("bar_id") val barId: String? = null,
    @SerializedName("bar_name") val barName: String? = null,
    @SerializedName("time") val time: String? = null,
    @SerializedName("bar_lat") val barLat: String? = null,
    @SerializedName("bar_lon") val barLon: String? = null
) {
}