package com.example.cv2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cv2.databinding.FragmentAllFriendsBinding
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
        fetchAllFriends()
        binding.addNewFriendButton.setOnClickListener {
            // TODO: action to add friend fragment
            val sharedPreference = activity?.applicationContext?.getSharedPreferences(
                "PREFERENCE_NAME", Context.MODE_PRIVATE)
            Log.i("TODO", "ACTION TO ADD FRIEND FRAGMENT")
            val accessToken = sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess"
            val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
            Log.i("uid", uid)
            Log.i("accessToken", accessToken)
        }

        return view
    }

    @DelicateCoroutinesApi
    fun fetchAllFriends() {

        // TODO: add all to recycler view

        GlobalScope.launch {
            val sharedPreference = activity?.applicationContext?.getSharedPreferences(
                "PREFERENCE_NAME", Context.MODE_PRIVATE)
            val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
            val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
            Log.i("accessToken", accessToken)
            Log.i("uid", uid)
            val response: String = RetrofitFriendApi.RETOROFIT_SERVICE.allFriends(accessToken , uid)
            Log.i("response", response)
        }
    }

    fun createHeadersForService() : Map<String, String> {
        val sharedPreference = activity?.applicationContext?.getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE)
        val accessToken = sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess"
        val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
        val map = mapOf(
            "Accept" to "application/json",
            "Content-Type" to "application/json",
            "Cache-Control" to "no-cache",
            "x-apikey" to "c95332ee022df8c953ce470261efc695ecf3e784",
            "x-user" to uid,
            "authorization" to "Bearer ")
//        map.
        return map
    }


}