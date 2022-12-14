package com.example.cv2.data.model

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.cv2.PubApplication
import com.example.cv2.R
import com.example.cv2.data.entity.Pub
import com.example.cv2.database.repository.PubRepository
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PubViewModel(
    private val pubRepository: PubRepository,
    application: PubApplication
) : AndroidViewModel(application) {

    private val context by lazy { application.applicationContext }
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var lat: String = "0.0"
    private var lon: String = "0.0"
    private var currentSort: String = "none"

    private var _entries = MutableLiveData<MutableList<Pub>>()

    val entries: LiveData<MutableList<Pub>>
        get() = _entries

    fun setEntries(
        list: MutableList<Pub>
    ) {
        _entries.value = list
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @DelicateCoroutinesApi
    fun loadData(useFei: Boolean) {
            val task = fusedLocationProviderClient?.lastLocation
            task?.addOnSuccessListener {
                if (useFei) {
                    lat = "48.143483"
                    lon = "17.108513"
                } else {
                    if (it == null) {
                        Toast.makeText(context!!, "NIE JE ZAPNUTE GPS, it == null", Toast.LENGTH_SHORT).show()
                        lat = ""
                        lon = ""
                    } else {
                        lat = it.latitude.toString()
                        lon = it.longitude.toString()
                    }
                }
                Log.e("lat", lat)
                Log.e("lon", lon)
                GlobalScope.launch(Dispatchers.Main) {

                    val resp = pubRepository.getAll()
                    _entries.value = resp as MutableList<Pub>

                }
        }
    }

    fun sortByProperty(
        currentId: Int
    ) {
        if (currentSort != currentId.toString()) {
            when (currentId) {
                R.id.menuSortPubName -> entries.value?.sortBy { it.name }
                R.id.menuSortPubDistance -> entries.value?.sortBy { it.distance }
                R.id.menuSortPubPeople -> entries.value?.sortBy { it.users }
                else -> entries.value?.sortBy { it.users }
            }
            currentSort = currentId.toString()
        } else {
            entries.value?.reverse()
        }
    }

}

class PubViewModelFactory(
    private val pubRepository: PubRepository,
    private val application: PubApplication
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PubViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PubViewModel(pubRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}