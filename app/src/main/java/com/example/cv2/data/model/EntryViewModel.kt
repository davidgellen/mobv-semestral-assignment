package com.example.cv2.data.model

import androidx.lifecycle.*
import com.example.cv2.dao.PubDao
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.request.PubsRequestBody
import com.example.cv2.service.RetrofitApi
import kotlinx.coroutines.launch

class EntryViewModel(
    private val pubDao: PubDao
) : ViewModel() {

    private var _entries = MutableLiveData<MutableList<Entry>>()

    val entries: LiveData<MutableList<Entry>>
        get() = _entries

    fun setEntries(
        list: MutableList<Entry>
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

    private fun getNewPubEntry(
        importedId: Long,
        lat: Double,
        lon: Double
    ): Pub {
        return Pub(
            importedId = importedId,
            lat = lat,
            lon = lon
        )
    }

}

class PubViewModelFactory(
    private val pubDao: PubDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EntryViewModel(pubDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}