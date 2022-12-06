package com.example.cv2.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["user_id", "contact_id"])
data class Contact (

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "contact_id")
    val contactId: Long,

    @ColumnInfo(name = "contact_name")
    val contactName: String,

    @ColumnInfo(name = "bar_id")
    val barId: String? = null,

    @ColumnInfo(name = "bar_name")
    val barName: String? = null,

    val time: String? = null,

    @ColumnInfo(name = "bar_lat")
    val barLat: String? = null,

    @ColumnInfo(name = "bar_lon")
    val barLon: String? = null
) {
}