package com.example.cv2.data.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cv2.application.PubApplication
import com.example.cv2.dao.PubDao
import com.example.cv2.data.entity.Pub
import com.example.cv2.mapper.PubMapper
import com.example.cv2.repository.PubRepository
import com.example.cv2.service.RetrofitOverpassApi
import com.example.cv2.utils.DistanceUtils
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.RoundingMode

class CheckInPubViewModel(
    private val pubRepository: PubRepository,
    application: PubApplication
) : ViewModel() {

    private val context by lazy { application.applicationContext }
    var fusedLocationProviderClient: FusedLocationProviderClient? = null


    private var _pubs = MutableLiveData<MutableList<Pub>>()
    private var lat = "0.0"
    private var lon = "0.0"

    val pubs: LiveData<MutableList<Pub>>
        get() = _pubs

    fun setPubs(
        list: MutableList<Pub>
    ) {
        _pubs.value = list
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @DelicateCoroutinesApi
    fun loadPubsInArea() {


//        // default for FEI
//        var lat = "48.143483"
//        var lon = "17.108513"

        GlobalScope.launch(Dispatchers.Main) {

            Log.e("LAT", lat)
            Log.e("LON", lon)

            val data = "[out:json];node(around:250, $lat, $lon);(node(around:250)[\"amenity\"~\"^pub\$|^bar\$|^restaurant\$|^cafe\$|^fast_food\$|^stripclub\$|^nightclub\$\"];);out body;>;out skel;"

            val pubsResponse = RetrofitOverpassApi.RETROFIT_SERVICE
                .getPubsInArea(data)
            Log.i("fetched pubs", pubsResponse.elements.size.toString())

            val pubs = PubMapper().entryListToPubList(pubsResponse.elements.toMutableList())
            for (pub in pubs) {
                val distance = DistanceUtils().distanceInKm(lat.toDouble(), lon.toDouble(), pub.lat!!, pub.lon!!)
                pub.distance = distance.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()
            }
            pubs.sortBy{ it.distance }
            setPubs(pubs)

        }
    }

    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.N)
    fun fetchLocation(
        useFei: Boolean
    ) {
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
            loadPubsInArea()
        }
    }

}

class CheckInPubViewModelFactory(
    private val pubRepository: PubRepository,
    private val application: PubApplication
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckInPubViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CheckInPubViewModel(pubRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}