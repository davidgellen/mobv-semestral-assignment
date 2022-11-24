package com.example.cv2.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cv2.R
import com.example.cv2.adapter.ContactAdapter
import com.example.cv2.data.entity.Contact
import com.example.cv2.data.response.ContactResponseBody
import com.example.cv2.databinding.FragmentAllFriendsBinding
import com.example.cv2.mapper.ContactResponseToEntityMapper
import com.example.cv2.service.RetrofitFriendApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AllFriendsFragment : Fragment() {

    private lateinit var binding: FragmentAllFriendsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllFriendsBinding.inflate(inflater, container, false)
        val view: View = binding.root

        val contactsRecyclerView = binding.friendListRecyclerView
        contactsRecyclerView.adapter =
            activity?.applicationContext?.let { ContactAdapter(it, mutableListOf()) }

        fetchAllFriends(contactsRecyclerView)

        binding.addNewFriendButton.setOnClickListener {
            findNavController().navigate(R.id.action_allFriendsFragment_to_addFriendFragment)
        }

        return view
    }

    @DelicateCoroutinesApi
    fun fetchAllFriends(
        contactsRecyclerView: RecyclerView
    ) {

        // TODO: add all to recycler view

        GlobalScope.launch {
            val sharedPreference = activity?.applicationContext?.getSharedPreferences(
                "PREFERENCE_NAME", Context.MODE_PRIVATE)
            val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
            val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
            Log.i("accessToken", accessToken)
            Log.i("uid", uid)

            val response: List<ContactResponseBody> = RetrofitFriendApi.RETOROFIT_SERVICE
                .allFriends(accessToken , uid)
            val contacts: MutableList<Contact> = ContactResponseToEntityMapper().entryListToPubList(
                response.toMutableList(), uid.toLong())
            for (c in contacts) {
                Log.i(c.contactId.toString(), c.contactName)
            }

            activity?.runOnUiThread {
                contactsRecyclerView.adapter =
                    activity?.let { ContactAdapter(it.applicationContext, contacts) }
                Log.i("CONTACT ADAPTER", "LIST SET TO FETCHED DATA")

            }
        }
    }

}