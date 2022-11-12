package com.example.cv2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.adapter.EntryAdapter
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.model.EntryViewModel
import com.example.cv2.data.request.PubsRequestBody
import com.example.cv2.service.RetrofitApi
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.InputStream

class AllEntriesFragment : Fragment() {

    private val entryViewModel: EntryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_entries, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.enttries_recycle_view)
        recyclerView.adapter = entryViewModel.entries.value?.let { EntryAdapter(view, it) }
        GlobalScope.launch{
            if (entryViewModel.entries.value?.size ?: 0 == 0) {
                val fetchedEntries = loadJsonFromServer().toMutableList()
                activity?.runOnUiThread {
                    entryViewModel.setEntries(fetchedEntries)
                    recyclerView.adapter = entryViewModel.entries.value?.let { EntryAdapter(view, it) }
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
        val entries: EntryDatasourceWrapper = RetrofitApi.retrofitService.getData(requestBody)
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