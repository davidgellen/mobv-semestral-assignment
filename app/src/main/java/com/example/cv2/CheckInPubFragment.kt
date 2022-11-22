package com.example.cv2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.databinding.FragmentCheckInPubBinding
import com.example.cv2.mapper.PubMapper
import com.example.cv2.service.RetrofitOverpassApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CheckInPubFragment : Fragment() {

    private lateinit var binding: FragmentCheckInPubBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckInPubBinding.inflate(inflater, container, false)
        val view: View = binding.root

        loadPubsInArea()

        return view
    }

    @DelicateCoroutinesApi
    fun loadPubsInArea() {
        GlobalScope.launch(Dispatchers.Main) {
            val lat = "48.143483"
            val lon = "17.108513"
            val data = "[out:json];node(around:250,48.143483, 17.108513);(node(around:250)[\"amenity\"~\"^pub\$|^bar\$|^restaurant\$|^cafe\$|^fast_food\$|^stripclub\$|^nightclub\$\"];);out body;>;out skel;"
            Log.i("S", "PRE REQ")

            val pubsResponse = RetrofitOverpassApi.RETROFIT_SERVICE
                .getPubsWithPeople(data)
            Log.i("S", "POST REQ")

            val pubs = PubMapper().entryListToPubList(pubsResponse.elements.toMutableList())
            // TODO: default sort by distance from me
        }
    }
}