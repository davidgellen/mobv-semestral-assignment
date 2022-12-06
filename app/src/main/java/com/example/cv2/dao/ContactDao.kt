package com.example.cv2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cv2.data.entity.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact ORDER BY id ASC")
    fun getAll() : LiveData<List<Contact>>

    @Query("SELECT * FROM contact WHERE id = :id ORDER BY id ASC")
    fun getOne(id: Int): Contact

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

}