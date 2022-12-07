package com.example.cv2.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cv2.R
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.model.PubViewModel
import com.example.cv2.databinding.FragmentEntryDetailBinding
import com.example.cv2.mapper.PubMapper
import com.example.cv2.service.RetrofitOverpassApi
import com.example.cv2.utils.ConnectivityUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EntryDetailFragment : Fragment() {

    private lateinit var binding: FragmentEntryDetailBinding
    private val pubViewModel: PubViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEntryDetailBinding.inflate(inflater, container, false)
        binding.editGpsMapButton.visibility = View.INVISIBLE
//        val pub: Pub = arguments?.getSerializable("entry") as Pub

        if (ConnectivityUtils().isOnline(context!!)) {
            GlobalScope.launch(Dispatchers.Main) {
                val pubId = arguments?.getLong("pubApiId").toString()
                Log.i("RECEIVED PUB ID", pubId)
                val body = "[out:json];node(${pubId});out body;>;out skel;"
                val pubResponse: List<Entry> = RetrofitOverpassApi.RETROFIT_SERVICE
                    .getPubsInArea(body).elements
                if (pubResponse.size == 1) {
                    val res = PubMapper().entryToPub(pubResponse[0])
                    activity?.runOnUiThread {
                        binding.detailNameTextView.text = res.name
                        binding.detailOpeningHours.text = "Hodiny: " + mapNullStringToEmpty(res.openingHours)
//                    binding.detailCity.text = "Adresa: " + createAddress(pub.tags.street,pub.tags.houseNumber, pub.tags.city, pub.tags.country)
                        binding.editWebsite.text = "Stranka: " + mapNullStringToEmpty(res.website)
                        binding.editDetailUserCount.text = "Ludi :${arguments?.getLong("users").toString()}"
                        binding.editGps.text = "GPS: ${res.lat}, ${res.lon}"
                        binding.editGpsMapButton.setOnClickListener {
                            val coordinates: String = "geo:" + res.lat.toString() + "," + res.lon.toString()
                            val gmmIntentUri = Uri.parse(coordinates)
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            startActivity(mapIntent)
                        }
                        binding.editGpsMapButton.visibility = View.VISIBLE

                    }
                } else {
                    Toast.makeText( activity?.applicationContext, "DETAIL PRE ZVOLENY PODNIK NEEXISTUJE", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            activity?.runOnUiThread {
                val res = arguments?.getSerializable("entry") as Pub
                binding.detailNameTextView.text = res.name
                binding.detailOpeningHours.text = "Hodiny: " + mapNullStringToEmpty(res.openingHours)
//                    binding.detailCity.text = "Adresa: " + createAddress(pub.tags.street,pub.tags.houseNumber, pub.tags.city, pub.tags.country)
                binding.editWebsite.text = "Stranka: " + mapNullStringToEmpty(res.website)
                binding.editDetailUserCount.text = "Ludi :${arguments?.getLong("users").toString()}"
                binding.editGps.text = "GPS: ${res.lat}, ${res.lon}"
                binding.editGpsMapButton.setOnClickListener {
                    val coordinates: String = "geo:" + res.lat.toString() + "," + res.lon.toString()
                    val gmmIntentUri = Uri.parse(coordinates)
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }
                binding.editGpsMapButton.visibility = View.VISIBLE

            }
            Toast.makeText(context!!, "CHYBA INTERNETOVE PRIPOJENIE", Toast.LENGTH_LONG).show()
        }


//        binding.editDeleteCurrent.setOnClickListener {
//            findNavController().navigate(R.id.action_entryDetailFragment_to_allEntriesFragment)
//        }

        return binding.root
    }

    private fun createAddress(
        street: String?,
        houseNumber: String?,
        city: String?,
        country: String?
    ) : String {
        if (street == null && houseNumber == null && city == null && country == null) {
            return "Neznáma"
        }
        return mapNullStringToEmpty(street) + " " + mapNullStringToEmpty(houseNumber) + ", " +
                mapNullStringToEmpty(city) + ", " + mapNullStringToEmpty(country)
    }

    private fun mapNullStringToEmpty(
        string: String?
    ) : String {
        return string ?: ""
    }
}