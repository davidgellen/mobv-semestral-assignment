package com.example.cv2.data.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.cv2.application.PubApplication
import com.example.cv2.dao.PubDao
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.request.RefreshTokenRequestBody
import com.example.cv2.data.response.PubResponseBody
import com.example.cv2.data.response.RegisterResponseBody
import com.example.cv2.mapper.PubMapper
import com.example.cv2.service.RetrofitNewPubApi
import com.example.cv2.service.RetrofitUserApi
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
    private val pubDao: PubDao,
    application: PubApplication
) : AndroidViewModel(application) {

    private val context by lazy { application.applicationContext }
    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private var lat: String = "0.0"
    private var lon: String = "0.0"

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

    @DelicateCoroutinesApi
    fun loadData(useFei: Boolean) {
        val task = fusedLocationProviderClient?.lastLocation
//        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED &&
//            context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
//            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101) }
//        }
        task?.addOnSuccessListener {
            if (useFei) {
                lat = "48.143483"
                lon = "17.108513"
            } else {
                lat = it.latitude.toString()
                lon = it.longitude.toString()
            }
            Log.e("lat", lat)
            Log.e("lon", lon)
            GlobalScope.launch(Dispatchers.Main) {
                val resp = loadJsonFromServer()
                val fetchedEntries = resp.toMutableList()
                for (pub in fetchedEntries) {
                    val distance = DistanceUtils().distanceInKm(lat.toDouble(), lon.toDouble(), pub.lat!!, pub.lon!!)
                    pub.distance = distance.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()
                }
                _entries.value = fetchedEntries
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

}

class PubViewModelFactory(
    private val pubDao: PubDao,
    private val application: PubApplication
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PubViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PubViewModel(pubDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}