package com.example.cv2.data.model

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.cv2.PubApplication
import com.example.cv2.R
import com.example.cv2.adapter.PubAdapter
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.request.RefreshTokenRequestBody
import com.example.cv2.data.response.PubResponseBody
import com.example.cv2.data.response.RegisterResponseBody
import com.example.cv2.mapper.PubMapper
import com.example.cv2.database.repository.PubRepository
import com.example.cv2.service.RetrofitNewPubApi
import com.example.cv2.service.RetrofitUserApi
import com.example.cv2.utils.ConnectivityUtils
import com.example.cv2.utils.DistanceUtils
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.math.RoundingMode

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
                if (ConnectivityUtils().isOnline(context!!)) {

                    GlobalScope.launch(Dispatchers.Main) {
                    val resp = loadJsonFromServer()
                    val fetchedEntries = resp.toMutableList()
                    pubRepository.deleteAll()
                    pubRepository.insert(fetchedEntries)
                    for (pub in fetchedEntries) {
                        try {
                            val distance = DistanceUtils().distanceInKm(
                                lat.toDouble(),
                                lon.toDouble(),
                                pub.lat!!,
                                pub.lon!!
                            )
                            pub.distance =
                                distance.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()
                        } catch ( e: NumberFormatException) {

                        }
                    }
                    _entries.value = fetchedEntries

                }
            } else {
                Log.e("NO INTERNET", "FETCHING PUBS FROM DATABASE")
                val pubsFromDb = pubRepository.getAll()
                for (pub in pubsFromDb) {
                    try {
                        val distance = DistanceUtils().distanceInKm(lat.toDouble(), lon.toDouble(), pub.lat!!, pub.lon!!)
                        pub.distance = distance.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()
                    } catch (e: java.lang.NumberFormatException) {

                    }
                }
                _entries.value = pubsFromDb
            }
        }
    }

    private suspend fun loadJsonFromServer(): List<Pub> {
        val sharedPreference = context.getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE)
        val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
        val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
        val pubs: Response<MutableList<PubResponseBody>> = RetrofitNewPubApi.RETROFIT_SERVICE
            .getPubsWithPeople(accessToken, uid)

        try {
            if (pubs.isSuccessful) {
                return PubMapper().pubResponseListToPubEntityList(pubs.body()!!.toMutableList())
            } else {
                if (pubs.code() == 401) {
                    val body = RefreshTokenRequestBody(sharedPreference?.getString("refresh", "") ?: "")
                    val refreshResponse: Response<RegisterResponseBody> =
                        RetrofitUserApi.RETROFIT_SERVICE.refreshToken(uid, body)
                    val editor = sharedPreference?.edit()
                    editor?.putString("access", refreshResponse.body()?.access)
                    editor?.putString("refresh", refreshResponse.body()?.refresh)
                    editor?.apply()
                    val renewedPubs: Response<MutableList<PubResponseBody>> = RetrofitNewPubApi.RETROFIT_SERVICE
                        .getPubsWithPeople("Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess"), uid)
                    return PubMapper().pubResponseListToPubEntityList(renewedPubs.body()!!.toMutableList())
                }
                return mutableListOf()
            }
        } catch (e: HttpException) {
            Log.e("Exception", "${e.message}")
            // TODO if exception == 401 return this function after refresh token
            return mutableListOf()

        } catch (e: Throwable) {
            Log.e("Ooops:", "Something else went wrong")
            return mutableListOf()

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
//            (binding.enttriesRecycleView.adapter as PubAdapter).notifyDataSetChanged()
            currentSort = currentId.toString()
        } else {
            entries.value?.reverse()
//            (binding.enttriesRecycleView.adapter as PubAdapter).notifyDataSetChanged()
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