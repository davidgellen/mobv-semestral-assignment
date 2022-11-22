package com.example.cv2.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.cv2.R
import com.example.cv2.data.entity.Pub
import com.example.cv2.databinding.CheckInPubItemBinding

class CheckInPubAdapter(
    private val context: Context,
    private val pubs: MutableList<Pub>,
    private val textView: TextView
) : RecyclerView.Adapter<CheckInPubAdapter.PubViewHolder>() {

    private var currentPub: Pub? = null

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
        holder.lottieCheckBox.setOnClickListener {
            holder.lottieCheckBox.playAnimation()
            currentPub = pubs[position]
            textView.text = "Vybrany bar: ${item.name}"
        }
        holder.textView.text = item.name
    }

    override fun getItemCount(): Int {
        return pubs.size
    }

}