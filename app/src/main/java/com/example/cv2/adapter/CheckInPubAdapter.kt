package com.example.cv2.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.cv2.R
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.model.CheckInPubViewModel
import com.google.android.material.card.MaterialCardView

class CheckInPubAdapter(
    private val context: View,
    private val checkInPubViewModel: CheckInPubViewModel
) : RecyclerView.Adapter<CheckInPubAdapter.PubViewHolder>() {

    private lateinit var currentPub: Pub
    private var currentPosition = 0

    fun getPub() : Pub? {
        return currentPub
    }

    class PubViewHolder (private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.pubName)
        val lottieCheckBox: LottieAnimationView = view.findViewById(R.id.animationView2)
        val card: MaterialCardView = view.findViewById(R.id.card_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PubViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.check_in_pub_item, parent, false)
        currentPub = checkInPubViewModel.pubs.value?.get(0)!!
        return PubViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: PubViewHolder, position: Int) {
        val item = checkInPubViewModel.pubs.value?.get(position)

        if (position == currentPosition) {
            holder.card.setBackgroundColor(Color.parseColor("#F9A825"))
            holder.card.strokeColor = Color.parseColor("#000000")
            holder.card.strokeWidth = 10
        } else {
            holder.card.setBackgroundColor(Color.parseColor("#EEEEEE"))
            holder.card.strokeColor = Color.parseColor("#F9A825")
            holder.card.strokeWidth = 5
        }
        holder.card.radius = 0.0F
        holder.textView.text = "${item?.name} (${item?.distance} km)"
        holder.itemView.setOnClickListener {
            currentPub = checkInPubViewModel.pubs.value?.get(position)!!
            notifyDataSetChanged()
            currentPosition = position
        }
    }

    override fun getItemCount(): Int {
        return checkInPubViewModel.pubs.value?.size!!
    }

}