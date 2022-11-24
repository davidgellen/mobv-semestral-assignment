package com.example.cv2.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.cv2.R
import com.example.cv2.data.entity.Pub
import com.example.cv2.databinding.CheckInPubItemBinding

class CheckInPubAdapter(
    private val context: Context,
    private val pubs: MutableList<Pub>
) : RecyclerView.Adapter<CheckInPubAdapter.PubViewHolder>() {

    private var currentPub: Pub? = null
    private var currentPosition = 0

    fun getPub() : Pub? {
        return currentPub
    }

    class PubViewHolder (private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.pubName)
        val lottieCheckBox: LottieAnimationView = view.findViewById(R.id.animationView2)
        val card: CardView = view.findViewById(R.id.card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PubViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.check_in_pub_item, parent, false)
        return PubViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PubViewHolder, position: Int) {
        val item = pubs[position]
        if (position == currentPosition) {
            holder.card.setBackgroundColor(Color.GREEN)
        } else {
            holder.card.setBackgroundColor(Color.RED)
        }
        holder.textView.text = "${item.name} (${item.distance} km)"
        holder.itemView.setOnClickListener {
            holder.lottieCheckBox.playAnimation()
            currentPub = pubs[position]
            notifyDataSetChanged()
            currentPosition = position
        }
    }

    override fun getItemCount(): Int {
        return pubs.size
    }

}