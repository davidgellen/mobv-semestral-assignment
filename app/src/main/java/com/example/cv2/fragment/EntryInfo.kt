package com.example.cv2.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.cv2.R
import com.google.android.material.textview.MaterialTextView

class EntryInfo : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_entry_info, container, false)
        val navButton = view.findViewById<Button>(R.id.ToAddNewScreenButton)
        view.findViewById<MaterialTextView>(R.id.nameTextView).setText(arguments?.getString("name"))
        view.findViewById<MaterialTextView>(R.id.businessNameTextView).setText(arguments?.getString("businessName"))
        navButton.setOnClickListener {
            findNavController().navigate(R.id.action_entryInfo_to_allEntriesFragment)
        }
        val lottieAnimation: LottieAnimationView = view.findViewById(R.id.animationView)
        lottieAnimation.setOnClickListener {
            lottieAnimation.playAnimation()
        }
//        lottieAnimation.setFailureListener { t ->
//            //do on error
//        }
        view.findViewById<Button>(R.id.showOnMapButton).setOnClickListener {
            val coordinates: String = "geo:" + arguments?.getFloat("lattitude").toString() + "," + arguments?.getFloat("longtitude").toString()
//        val gmmIntentUri = Uri.parse("geo:37.7749,-122.4194")
            val gmmIntentUri = Uri.parse(coordinates)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        return view
    }

}