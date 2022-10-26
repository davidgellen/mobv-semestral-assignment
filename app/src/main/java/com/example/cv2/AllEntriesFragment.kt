package com.example.cv2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.adapter.EntryAdapter
import com.example.cv2.data.Entry
import com.example.cv2.data.EntryDatasource
import com.example.cv2.data.EntryDatasourceWrapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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
        if (globalMutableEntries.size == 0) { // so it doesn't reread from json on every onCreate
            globalMutableEntries = loadJson("pubs.json").toMutableList()
        }
        val recyclerView = view.findViewById<RecyclerView>(R.id.enttries_recycle_view)
        recyclerView.adapter = EntryAdapter(this, globalMutableEntries)
        view.findViewById<ImageButton>(R.id.toAddEntryButton).setOnClickListener {
            findNavController().navigate(R.id.action_allEntriesFragment_to_addNewEntry)
        }
        view.findViewById<ImageButton>(R.id.sortEntriesButton).setOnClickListener {
            globalMutableEntries.sortBy { it.tags.name }
            // updates the recycler view visually
            recyclerView.adapter = this.context?.let { EntryAdapter(this, globalMutableEntries) }
        }
        return view
    }

    companion object {
        var globalMutableEntries = mutableListOf<Entry>()
    }

    private fun loadJson(
        path: String
    ): List<Entry> {
        try {
            val inputStream: InputStream = context!!.assets.open(path)
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