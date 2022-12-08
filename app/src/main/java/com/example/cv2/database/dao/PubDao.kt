package com.example.cv2.database.dao

import androidx.room.*
import com.example.cv2.data.entity.Pub

@Dao
interface PubDao {

    @Query("SELECT * FROM pub ORDER BY id ASC")
    fun getAll(): MutableList<Pub>

    @Query("SELECT * FROM pub WHERE imported_id = :id ORDER BY id ASC")
    fun getByImportedId(id: Long): Pub

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pub: Pub)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pubs: List<Pub>)

    @Update
    suspend fun update(pub: Pub)

    @Delete
    suspend fun delete(pub: Pub)

    @Query("DELETE FROM pub")
    suspend fun deleteAll()

}