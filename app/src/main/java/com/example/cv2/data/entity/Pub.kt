package com.example.cv2.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Pub(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "lat") val lat: Double?,
    @NonNull @ColumnInfo(name = "lon") val lon: Double?
) {
}