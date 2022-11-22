package com.example.cv2

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.cv2.databinding.FragmentCheckInPubBinding
import com.example.cv2.mapper.PubMapper
import com.example.cv2.service.RetrofitOverpassApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckInPubFragment : Fragment() {

    private lateinit var binding: FragmentCheckInPubBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lat : String
    private lateinit var lon : String;


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        fetchLocation()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckInPubBinding.inflate(inflater, container, false)
        val view: View = binding.root
        fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }!!

        fetchLocation()

        return view
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
                .getPubsWithPeople(data)
            Log.i("fetched pubs", pubsResponse.elements.size.toString())

            val pubs = PubMapper().entryListToPubList(pubsResponse.elements.toMutableList())


            // TODO: default sort by distance from me
        }
    }

    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.N)
    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED &&
            context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101) }
        }
        task.addOnSuccessListener {
            lat = it.latitude.toString()
            lon = it.longitude.toString()
            loadPubsInArea()
        }
    }

}