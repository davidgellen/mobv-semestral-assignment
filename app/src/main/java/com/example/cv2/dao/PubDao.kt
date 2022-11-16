package com.example.cv2.dao

import androidx.room.*
import com.example.cv2.data.entity.Pub
import kotlinx.coroutines.flow.Flow

@Dao
interface PubDao {

    @Query("SELECT * FROM pub ORDER BY id ASC")
    fun getAll(): Flow<List<Pub>>

    @Query("SELECT * FROM pub WHERE id = :id ORDER BY id ASC")
    fun getOne(id: Int): Flow<Pub>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pub: Pub)

    @Update
    suspend fun update(pub: Pub)

    @Delete
    suspend fun delete(pub: Pub)

}