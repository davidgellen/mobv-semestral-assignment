package com.example.cv2.data.request

import com.google.gson.annotations.SerializedName

data class PubsRequestBody(
    @SerializedName("collection") val collection: String,
    @SerializedName("database") val database: String?,
    @SerializedName("dataSource") val dataSource: String?
) {
}