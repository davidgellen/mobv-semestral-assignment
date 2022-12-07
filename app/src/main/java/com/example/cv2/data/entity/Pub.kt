package com.example.cv2.data.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Pub(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "imported_id") var importedId: Long?,
    @NonNull @ColumnInfo(name = "lat") var lat: Double?,
    @NonNull @ColumnInfo(name = "lon") var lon: Double?,
    @ColumnInfo(name = "phone") var phone: String?,
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "openingHours") var openingHours: String?,
    @ColumnInfo(name = "website") var website: String?,
    @ColumnInfo(name = "users") var users: Long?,
    var distance: Double? = null
) : Serializable {

}