package com.example.cv2.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.cv2.R
import com.example.cv2.data.request.AddFriendRequestBody
import com.example.cv2.databinding.FragmentAddFriendBinding
import com.example.cv2.service.RetrofitFriendApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddFriendFragment : Fragment() {

    private lateinit var binding: FragmentAddFriendBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddFriendBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.addFriendButton.setOnClickListener {
            addFriend()
        }

        return view
    }

    @DelicateCoroutinesApi
    fun addFriend() {
        GlobalScope.launch(Dispatchers.Main) {
            val friendName = binding.addFriendNameTextField.text.toString()
            val requestBody = AddFriendRequestBody(friendName)
            val sharedPreference = activity?.applicationContext?.getSharedPreferences(
                "PREFERENCE_NAME", Context.MODE_PRIVATE)
            val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
            val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
            try {
                val response: String = RetrofitFriendApi.RETOROFIT_SERVICE
                    .addFriend(accessToken, uid, requestBody) // throws 200 or 500
                Log.i("add friend", "SUCCESS")
                findNavController().navigate(R.id.action_addFriendFragment_to_allFriendsFragment)
            } catch (e: Exception) {
                Log.i("add friend", "FAILURE")
                activity?.runOnUiThread {
                    Toast.makeText(
                        activity?.applicationContext,
                        "USER NEEXISTUJE",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}