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
import com.example.cv2.data.request.RegisterRequestBody
import com.example.cv2.data.response.RegisterResponseBody
import com.example.cv2.databinding.FragmentLoginBinding
import com.example.cv2.service.RetrofitUserApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.loginButton.setOnClickListener { login() }

        binding.loginToRegisterButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        return view
    }

    @DelicateCoroutinesApi
    fun login() {
        GlobalScope.launch(Dispatchers.Main) {
            val username = binding.loginName
            val password = binding.loginPassword
            val registerBody = RegisterRequestBody(username.text.toString(), password.text.toString())
            val response: RegisterResponseBody =
                RetrofitUserApi.RETROFIT_SERVICE.login(registerBody)
            if (response.uid.toInt() == -1) {
                activity?.runOnUiThread {
                    Toast.makeText(
                        activity?.applicationContext,
                        "ZLE MENO ALEBO HESLO",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Log.i("Login", "SUCCESS")
                val sharedPreference = activity?.applicationContext?.getSharedPreferences(
                    "PREFERENCE_NAME",
                    Context.MODE_PRIVATE
                )
                val editor = sharedPreference?.edit()
                editor?.putString("uid", response.uid)
                editor?.putString("access", response.access)
                editor?.putString("refresh", response.refresh)
                editor?.apply()
                findNavController().navigate(R.id.action_loginFragment_to_homeScreenFragment)
            }
        }
    }

}