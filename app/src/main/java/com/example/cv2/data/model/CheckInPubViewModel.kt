package com.example.cv2.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cv2.dao.PubDao
import com.example.cv2.data.entity.Pub

class CheckInPubViewModel(
    private val pubDao: PubDao
) : ViewModel() {

    private var _pubs = MutableLiveData<MutableList<Pub>>()

    val pubs: LiveData<MutableList<Pub>>
        get() = _pubs

    fun setPubs(
        list: MutableList<Pub>
    ) {
        _pubs.value = list
    }

}

class CheckInPubViewModelFactory(
    private val pubDao: PubDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckInPubViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckInPubViewModel(pubDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}