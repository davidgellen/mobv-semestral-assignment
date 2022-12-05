package com.example.cv2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cv2.R
import com.example.cv2.adapter.ContactAdapter
import com.example.cv2.application.PubApplication
import com.example.cv2.data.model.ContactViewModel
import com.example.cv2.data.model.ContactViewModelFactory
import com.example.cv2.databinding.FragmentAllFriendsBinding

class AllFriendsFragment : Fragment() {

    private lateinit var binding: FragmentAllFriendsBinding

    private val contactViewModel: ContactViewModel by activityViewModels() {
        ContactViewModelFactory(
            (activity?.application as PubApplication).database.contactDao(),
            (activity?.application as PubApplication)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllFriendsBinding.inflate(inflater, container, false)
        val view: View = binding.root

        val contactsRecyclerView = binding.friendListRecyclerView

        contactViewModel.fetchAllFriends()
        contactViewModel.entries.observe(viewLifecycleOwner) {
            contactsRecyclerView.adapter = ContactAdapter(view, contactViewModel)
        }

        binding.addNewFriendButton.setOnClickListener {
            findNavController().navigate(R.id.action_allFriendsFragment_to_addFriendFragment)
        }

        return view
    }

}