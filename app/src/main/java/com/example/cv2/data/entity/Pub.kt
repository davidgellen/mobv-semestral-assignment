package com.example.cv2.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Pub(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "imported_id") val importedId: Long?,
    @NonNull @ColumnInfo(name = "lat") val lat: Double?,
    @NonNull @ColumnInfo(name = "lon") val lon: Double?,
    @ColumnInfo(name = "phone") val phone: String?,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "openingHours") val openingHours: String?,
    @ColumnInfo(name = "website") val website: String?,
    @ColumnInfo(name = "users") val users: Long?
) : Serializable {
}