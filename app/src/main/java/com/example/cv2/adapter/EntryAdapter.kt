package com.example.cv2.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.R
import com.example.cv2.data.jsonmapper.Entry

class EntryAdapter(
    private val context: View,
    private val entries: MutableList<Entry>,
) : RecyclerView.Adapter<EntryAdapter.EntryViewHolder>(){

    class EntryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.entry_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.entry_item, parent, false)
        return EntryViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        holder.itemView.setOnClickListener{ view ->
            val bundle = Bundle()
            bundle.putSerializable("entry", entries[position])
            view.findNavController().navigate(R.id.action_allEntriesFragment_to_entryDetailFragment, bundle)
        }
        holder.textView.text = entries[position].tags.name
    }

    override fun getItemCount(): Int {
        return entries.size
    }

}