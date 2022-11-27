package com.example.cv2.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.R
import com.example.cv2.adapter.PubAdapter
import com.example.cv2.application.PubApplication
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.model.PubViewModel
import com.example.cv2.data.model.PubViewModelFactory
import com.example.cv2.data.request.RefreshTokenRequestBody
import com.example.cv2.data.response.PubResponseBody
import com.example.cv2.data.response.RegisterResponseBody
import com.example.cv2.databinding.FragmentAllEntriesBinding
import com.example.cv2.databinding.FragmentCheckInPubBinding
import com.example.cv2.mapper.PubMapper
import com.example.cv2.service.RetrofitNewPubApi
import com.example.cv2.service.RetrofitUserApi
import com.example.cv2.utils.DistanceUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.GsonBuilder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.InputStream
import java.math.RoundingMode

class AllEntriesFragment : Fragment() {

    private lateinit var binding: FragmentAllEntriesBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lat : String
    private lateinit var lon : String
    private lateinit var currentSort: String

    private val pubViewModel: PubViewModel by activityViewModels() {
        PubViewModelFactory(
            (activity?.application as PubApplication).database.pubDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllEntriesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root
        fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }!!
        val recyclerView = binding.enttriesRecycleView
        recyclerView.adapter = pubViewModel.entries.value?.let { PubAdapter(view, it) }
        currentSort = "none"
        loadData(view, false)

        binding.toAddEntryButton.visibility = View.INVISIBLE
//        binding.toAddEntryButton.setOnClickListener {
//            findNavController().navigate(R.id.action_allEntriesFragment_to_addNewEntry)
//        }
        binding.swipeContainer.setOnRefreshListener {
            loadData(view, false)
        }
        binding.entriesToMenuButton.setOnClickListener {
            findNavController().navigate(R.id.action_allEntriesFragment_to_homeScreenFragment)
        }
        binding.entriesToCheckInButton.setOnClickListener {
            findNavController().navigate(R.id.action_allEntriesFragment_to_checkInPubFragment)
        }
        setMenuBar()
        return view
    }

    @DelicateCoroutinesApi
    private fun loadData(view: View, useFei: Boolean) {
        val task = fusedLocationProviderClient.lastLocation
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED &&
            context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101) }
        }
        task.addOnSuccessListener {
            if (useFei) {
                lat = "48.143483"
                lon = "17.108513"
            } else {
                lat = it.latitude.toString()
                lon = it.longitude.toString()
            }
            GlobalScope.launch(Dispatchers.Main) {
                val resp = loadJsonFromServer()
                val fetchedEntries = resp.toMutableList()
                for (pub in fetchedEntries) {
                    val distance = DistanceUtils().distanceInKm(lat.toDouble(), lon.toDouble(), pub.lat!!, pub.lon!!)
                    pub.distance = distance.toBigDecimal().setScale(3, RoundingMode.UP).toDouble()
                }
                activity?.runOnUiThread {
                    pubViewModel.setEntries(fetchedEntries)
                    binding.enttriesRecycleView.adapter =
                        pubViewModel.entries.value?.let { PubAdapter(view, it) }
                    binding.swipeContainer.isRefreshing = false
                }
            }
        }
    }

    private suspend fun loadJsonFromServer(): List<Pub> {
        val sharedPreference = activity?.applicationContext?.getSharedPreferences(
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
            // TODO if exception == 401 return this function after refresh token DO MATERINEJ PIZZZZZE
            return mutableListOf()

        } catch (e: Throwable) {
            Log.e("Ooops:", "Something else went wrong")
            return mutableListOf()

        }


    }

    private fun setMenuBar() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.pubs_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                sortByProperty(menuItem.itemId)
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun sortByProperty(
        currentId: Int
    ) {
        if (!currentSort.equals(currentId.toString())) {
            when (currentId) {
                R.id.menuSortPubName -> pubViewModel.entries.value?.sortBy { it.name }
                R.id.menuSortPubDistance -> pubViewModel.entries.value?.sortBy { it.distance }
                R.id.menuSortPubPeople -> pubViewModel.entries.value?.sortBy { it.users }
                else -> pubViewModel.entries.value?.sortBy { it.users }
            }
            (binding.enttriesRecycleView.adapter as PubAdapter).notifyDataSetChanged()
            currentSort = currentId.toString()
        } else {
            pubViewModel.entries.value?.reverse()
            (binding.enttriesRecycleView.adapter as PubAdapter).notifyDataSetChanged()
        }
    }

}