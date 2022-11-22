package com.example.cv2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cv2.R
import com.example.cv2.databinding.FragmentHomeScreenBinding

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        val view: View = binding.root

        bindButtons()

        return view
    }

    private fun bindButtons() {
        binding.homeScreenToContactListButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_allFriendsFragment)
        }

        binding.homeScreenToPubListButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_allEntriesFragment)
        }

        binding.homeScreenToCheckInButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_checkInPubFragment)
        }
    }
}