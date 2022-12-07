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
import com.example.cv2.data.model.ContactViewModel
import com.example.cv2.mapper.PubMapper

class ContactAdapter(
    private val context: View,
    private val contactViewModel: ContactViewModel
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.contactName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.friend_item, parent, false)
        return ContactViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        if (contactViewModel.entries.value?.get(position)?.barId != null) {
            holder.itemView.setOnClickListener { view ->
                val bundle = Bundle()
                bundle.putLong("pubApiId", contactViewModel.entries.value?.get(position)?.barId!!.toLong())
                val pub = contactViewModel.getApplication().pubRepository.getByImportedId(contactViewModel.entries.value?.get(position)?.barId!!.toLong())
                bundle.putSerializable("entry", pub)
                bundle.putSerializable("users", pub.users)
                Log.e("BINDED PICOVINA", "KOKOT")
                view.findNavController()
                    .navigate(R.id.action_allFriendsFragment_to_entryDetailFragment, bundle)
            }
            holder.textView.text = "${contactViewModel.entries.value?.get(position)?.contactName} + (${contactViewModel.entries.value?.get(position)?.barName})"
        } else {
            holder.textView.text = "${contactViewModel.entries.value?.get(position)?.contactName}"
        }
    }

    override fun getItemCount(): Int {
        return contactViewModel.entries.value?.size!!
    }

}