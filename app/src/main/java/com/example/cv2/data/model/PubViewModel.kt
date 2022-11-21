package com.example.cv2.data.model

import androidx.lifecycle.*
import com.example.cv2.dao.PubDao
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.jsonmapper.Entry
import kotlinx.coroutines.launch

class PubViewModel(
    private val pubDao: PubDao
) : ViewModel() {

    private var _entries = MutableLiveData<MutableList<Pub>>()

    val entries: LiveData<MutableList<Pub>>
        get() = _entries

    fun setEntries(
        list: MutableList<Pub>
    ) {
        _entries.value = list
    }

    fun insertPub(
        pub: Pub
    ) {
        viewModelScope.launch {
            pubDao.insert(pub)
        }
    }

    fun getAllEntries() {
        pubDao.getAll()
    }

//    private fun getNewPubEntry(
//        importedId: Long,
//        lat: Double,
//        lon: Double
//    ): Pub {
//        return Pub(
//            importedId = importedId,
//            lat = lat,
//            lon = lon
//        )
//    }

}

class PubViewModelFactory(
    private val pubDao: PubDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PubViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PubViewModel(pubDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}