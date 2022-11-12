package com.example.cv2.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cv2.data.entity.Pub

@Dao
interface PubDao {

    @Query("SELECT * FROM pub ORDER BY id ASC")
    fun getAll(): List<Pub>

}