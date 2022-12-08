package com.example.cv2.database.dao

import androidx.room.*
import com.example.cv2.data.entity.Contact

@Dao
interface ContactDao {

    @Query("SELECT * FROM contact")
    fun getAll() : MutableList<Contact>

    @Query("SELECT * FROM contact WHERE user_id = :userId")
    fun getAllByUserId(userId: Int): MutableList<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contacts: List<Contact>)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

}