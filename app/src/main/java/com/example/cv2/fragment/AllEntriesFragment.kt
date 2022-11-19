package com.example.cv2.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.R
import com.example.cv2.adapter.EntryAdapter
import com.example.cv2.application.PubApplication
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.model.EntryViewModel
import com.example.cv2.data.model.PubViewModelFactory
import com.example.cv2.data.request.PubsRequestBody
import com.example.cv2.mapper.EntryToPubMapper
import com.example.cv2.service.RetrofitPubApi
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class AllEntriesFragment : Fragment() {

    private val entryViewModel: EntryViewModel by activityViewModels() {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_entries, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.enttries_recycle_view)
        recyclerView.adapter = entryViewModel.entries.value?.let { EntryAdapter(view, it) }

//        val pub: Pub = Pub(0, 124, 20.3, 20.4)
//        val kktkot = entryViewModel.insertPub(pub)
        val stuff = entryViewModel.getAllEntries()
        val entryToPubMapper = EntryToPubMapper()

        GlobalScope.launch {
            if (entryViewModel.entries.value?.size ?: 0 == 0) {
                val fetchedEntries = loadJsonFromServer().toMutableList()
//                val pubs = entryToPubMapper.entryListToPubList(fetchedEntries)
//                for (pub in pubs) {
//                    entryViewModel.insertPub(pub)
//                }
                activity?.runOnUiThread {
                    entryViewModel.setEntries(fetchedEntries)
                    recyclerView.adapter =
                        entryViewModel.entries.value?.let { EntryAdapter(view, it) }
                }
            }
        }
        view.findViewById<ImageButton>(R.id.toAddEntryButton).setOnClickListener {
            findNavController().navigate(R.id.action_allEntriesFragment_to_addNewEntry)
        }
        view.findViewById<ImageButton>(R.id.sortEntriesButton).setOnClickListener {
            entryViewModel.entries.value?.sortBy { it.tags.name }
            (recyclerView.adapter as EntryAdapter).notifyDataSetChanged()
        }
        return view
    }

//    companion object {
//        var globalMutableEntries = mutableListOf<Entry>()
//    }

    private suspend fun loadJsonFromServer(): List<Entry> {
        val requestBody = PubsRequestBody("bars", "mobvapp", "Cluster0")
        val entries: EntryDatasourceWrapper = RetrofitPubApi.RETROFIT_SERVICE.getData(requestBody)
        return entries.documents
    }

    private fun loadJson(): List<Entry> {
        try {
            val inputStream: InputStream = context!!.assets.open("pubs.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            val allEntries = GsonBuilder().create()
                .fromJson(json, EntryDatasourceWrapper::class.java)
            return allEntries.documents
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return listOf()
    }
}