package com.example.cv2

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cv2.adapter.CheckInPubAdapter
import com.example.cv2.adapter.ContactAdapter
import com.example.cv2.application.PubApplication
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.model.CheckInPubViewModel
import com.example.cv2.data.model.CheckInPubViewModelFactory
import com.example.cv2.data.model.ContactViewModel
import com.example.cv2.data.model.ContactViewModelFactory
import com.example.cv2.data.request.CheckIntoPubRequestBody
import com.example.cv2.databinding.FragmentCheckInPubBinding
import com.example.cv2.mapper.PubMapper
import com.example.cv2.service.RetrofitNewPubApi
import com.example.cv2.service.RetrofitOverpassApi
import com.example.cv2.utils.DistanceUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Math.pow
import java.math.RoundingMode
import kotlin.math.*

class CheckInPubFragment : Fragment() {

    private lateinit var binding: FragmentCheckInPubBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lat : String
    private lateinit var lon : String

    private val checkInPubViewModel: CheckInPubViewModel by activityViewModels() {
        CheckInPubViewModelFactory(
            (activity?.application as PubApplication).pubRepository,
            (activity?.application as PubApplication)
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        checkInPubViewModel.fusedLocationProviderClient = fusedLocationProviderClient
        val recyclerView = binding.checkInRecyclerView
//        recyclerView.adapter =
//            activity?.applicationContext?.let { CheckInPubAdapter(view, mutableListOf()) }

        checkInPubViewModel.fetchLocation(false)
        checkInPubViewModel.pubs.observe(viewLifecycleOwner) {
            recyclerView.adapter = CheckInPubAdapter(view, checkInPubViewModel)
            binding.checkInPubConfirmButton.setOnClickListener {
                binding.animationView4.playAnimation()
                val pub = (binding.checkInRecyclerView.adapter as CheckInPubAdapter).getPub()
                if (pub == null) {
                    activity?.runOnUiThread {
                        Toast.makeText( activity?.applicationContext, "ZIADNY PODNIK NENI ZVOLENY", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    GlobalScope.launch(Dispatchers.Main) {
                        val sharedPreference = activity?.applicationContext?.getSharedPreferences(
                            "PREFERENCE_NAME", Context.MODE_PRIVATE)
                        val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
                        val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
                        val body = CheckIntoPubRequestBody(pub.importedId.toString(), pub.name!!, "node", pub.lat!!, pub.lon!!)
                        val response = RetrofitNewPubApi.RETROFIT_SERVICE
                            .checkIntoPub(accessToken, uid, body)
//                        findNavController().navigate(R.id.action_checkInPubFragment_to_allEntriesFragment)
                    }
                }
            }
        }

        return view
    }

}