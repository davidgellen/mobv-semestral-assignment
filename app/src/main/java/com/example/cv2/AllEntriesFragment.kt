package com.example.cv2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.adapter.EntryAdapter
import com.example.cv2.data.Entry
import com.example.cv2.data.EntryDatasourceWrapper
import com.example.cv2.data.PubsRequestBody
import com.example.cv2.database.RetrofitApi
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class AllEntriesFragment : Fragment() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_entries, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.enttries_recycle_view)
        recyclerView.adapter = EntryAdapter(view, globalMutableEntries)
        GlobalScope.launch{
            if (globalMutableEntries.size == 0) {
                globalMutableEntries = loadJsonFromServer().toMutableList()
                activity?.runOnUiThread {
                    recyclerView.adapter = EntryAdapter(view, globalMutableEntries)
                }
                Log.i("data", "loaded from POST REQUEST")
            }
        }
        view.findViewById<ImageButton>(R.id.toAddEntryButton).setOnClickListener {
            findNavController().navigate(R.id.action_allEntriesFragment_to_addNewEntry)
        }
        view.findViewById<ImageButton>(R.id.sortEntriesButton).setOnClickListener {
            globalMutableEntries.sortBy { it.tags.name }
            (recyclerView.adapter as EntryAdapter).notifyDataSetChanged()
        }
        return view
    }

    companion object {
        var globalMutableEntries = mutableListOf<Entry>()
    }

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