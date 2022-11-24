package com.example.cv2.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.example.cv2.data.response.PubResponseBody
import com.example.cv2.databinding.FragmentAllEntriesBinding
import com.example.cv2.databinding.FragmentCheckInPubBinding
import com.example.cv2.mapper.PubMapper
import com.example.cv2.service.RetrofitNewPubApi
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class AllEntriesFragment : Fragment() {

    private lateinit var binding: FragmentAllEntriesBinding

    private val pubViewModel: PubViewModel by activityViewModels() {
        PubViewModelFactory(
            (activity?.application as PubApplication).database.pubDao()
        )
    }

//    private val dao = (activity?.application as PubApplication).database.pubDao()
//    private val entryViewModel(dao): EntryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllEntriesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root
        val recyclerView = binding.enttriesRecycleView
        recyclerView.adapter = pubViewModel.entries.value?.let { PubAdapter(view, it) }

        loadData(view)

        binding.toAddEntryButton.setOnClickListener {
            findNavController().navigate(R.id.action_allEntriesFragment_to_addNewEntry)
        }
        binding.sortEntriesButton.setOnClickListener {
            pubViewModel.entries.value?.sortBy { it.name }
            (recyclerView.adapter as PubAdapter).notifyDataSetChanged()
        }
        binding.swipeContainer.setOnRefreshListener {
            loadData(view)
        }
        return view
    }

    private fun loadData(view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            val fetchedEntries = loadJsonFromServer().toMutableList()
            activity?.runOnUiThread {
                pubViewModel.setEntries(fetchedEntries)
                binding.enttriesRecycleView.adapter =
                    pubViewModel.entries.value?.let { PubAdapter(view, it) }
                binding.swipeContainer.isRefreshing = false
            }
        }
    }

    private suspend fun loadJsonFromServer(): List<Pub> {
        val sharedPreference = activity?.applicationContext?.getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE)
        val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
        val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
        val pubs: MutableList<PubResponseBody> = RetrofitNewPubApi.RETROFIT_SERVICE
            .getPubsWithPeople(accessToken, uid)
        return PubMapper().pubResponseListToPubEntityList(pubs)
    }

//    private suspend fun loadJsonFromServer(): List<Entry> {
//        val requestBody = PubsRequestBody("bars", "mobvapp", "Cluster0")
//        val entries: EntryDatasourceWrapper = RetrofitPubApi.RETROFIT_SERVICE.getData(requestBody)
//        return entries.documents
//    }

    private fun loadJson(): List<Entry> {
        try {
            val inputStream: InputStream = context!!.assets.open("pubs.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            val allEntries = GsonBuilder().create()
                .fromJson(json, EntryDatasourceWrapper::class.java)
            return allEntries.elements
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listOf()
    }
}