package com.example.cv2.repository

import androidx.lifecycle.LiveData
import com.example.cv2.dao.PubDao
import com.example.cv2.data.entity.Pub

class PubRepository(
    private val pubDao: PubDao
) {

    val pubs: LiveData<List<Pub>> = pubDao.getAll()

}