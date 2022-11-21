package com.example.cv2.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.R
import com.example.cv2.data.entity.Contact

class ContactAdapter(
    private val context: Context,
    private val contacts: MutableList<Contact>
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
        holder.itemView.setOnClickListener { view ->
            Log.i("TODO", "ZOBRAZ DETAIL PODNIKU")
        }
        holder.textView.text = contacts[position].contactName
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

}