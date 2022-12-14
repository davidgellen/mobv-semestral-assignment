package com.example.cv2.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cv2.adapter.CheckInPubAdapter
import com.example.cv2.PubApplication
import com.example.cv2.data.model.CheckInPubViewModel
import com.example.cv2.data.model.CheckInPubViewModelFactory
import com.example.cv2.data.request.CheckIntoPubRequestBody
import com.example.cv2.databinding.FragmentCheckInPubBinding
import com.example.cv2.service.RetrofitNewPubApi
import com.example.cv2.utils.ConnectivityUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        if (ConnectivityUtils().isOnline(context!!)) {
            checkInPubViewModel.fetchLocation(false)
        } else {
            Toast.makeText(context!!, "CHYBA PRISTUP K INTERNETU", Toast.LENGTH_LONG).show()
        }
        checkInPubViewModel.pubs.observe(viewLifecycleOwner) {
            recyclerView.adapter = CheckInPubAdapter(view, checkInPubViewModel)
            binding.checkInPubConfirmButton.setOnClickListener {
                val pub = (binding.checkInRecyclerView.adapter as CheckInPubAdapter).getPub()
                if (pub == null) {
                    activity?.runOnUiThread {
                        Toast.makeText( activity?.applicationContext, "ZIADNY PODNIK NIE JE ZVOLENY", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (ConnectivityUtils().isOnline(context!!)) {
                        binding.animationView4.playAnimation()
                        GlobalScope.launch(Dispatchers.Main) {
                            val sharedPreference =
                                activity?.applicationContext?.getSharedPreferences(
                                    "PREFERENCE_NAME", Context.MODE_PRIVATE
                                )
                            val accessToken =
                                "Bearer " + (sharedPreference?.getString("access", "defaultAccess")
                                    ?: "defaultAccess")
                            val uid =
                                sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
                            val body = CheckIntoPubRequestBody(
                                pub.importedId.toString(),
                                pub.name!!,
                                "node",
                                pub.lat!!,
                                pub.lon!!
                            )
                            val response = RetrofitNewPubApi.RETROFIT_SERVICE.checkIntoPub(
                                accessToken,
                                uid,
                                body
                            )
                        }
                    } else {
                        Toast.makeText(context!!, "CHYBA PRISTUP K INTERNETU", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return view
    }

}