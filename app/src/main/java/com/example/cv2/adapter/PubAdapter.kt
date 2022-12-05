package com.example.cv2.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.R
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.model.PubViewModel

class PubAdapter(
    private val context: View,
    private val pubViewModel: PubViewModel,
) : RecyclerView.Adapter<PubAdapter.PubViewHolder>() {

    class PubViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.entry_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PubViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.entry_item, parent, false)
        return PubViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PubViewHolder, position: Int) {
        holder.itemView.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putSerializable("entry", pubViewModel.entries.value?.get(position))
            bundle.putLong("pubApiId", pubViewModel.entries.value?.get(position)?.importedId!!)
            bundle.putLong("users", pubViewModel.entries.value?.get(position)?.users!!)
            Log.i("pubApiId", pubViewModel.entries.value?.get(position)?.importedId.toString())
            view.findNavController()
                .navigate(R.id.action_allEntriesFragment_to_entryDetailFragment, bundle)
        }
        val item = pubViewModel.entries.value?.get(position)
        holder.textView.text = "${item?.name} (${item?.users} ludi, ${item?.distance} km)"
    }

    override fun getItemCount(): Int {
        return pubViewModel.entries.value?.size!!
    }

}