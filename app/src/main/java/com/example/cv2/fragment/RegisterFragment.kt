package com.example.cv2.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.cv2.R
import com.example.cv2.data.request.RegisterRequestBody
import com.example.cv2.data.response.RegisterResponseBody
import com.example.cv2.databinding.FragmentRegisterBinding
import com.example.cv2.service.RetrofitUserApi
import com.example.cv2.utils.ConnectivityUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.registerButtonRegister.isEnabled = false

        binding.registerPassword.addTextChangedListener {
            disableButtonOnConflictingPassword()
        }

        binding.registerRepeatPassword.addTextChangedListener {
            disableButtonOnConflictingPassword()
        }

        binding.registerCancelButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun disableButtonOnConflictingPassword() {
        if (binding.registerPassword.text == null || binding.registerPassword.text.isEmpty() ||
            binding.registerRepeatPassword.text == null || binding.registerRepeatPassword.text.isEmpty()) {
            binding.registerButtonRegister.isClickable = false
        }
        binding.registerButtonRegister.isEnabled = binding.registerPassword.text.toString().equals(binding.registerRepeatPassword.text.toString())
        binding.registerButtonRegister.setOnClickListener { register() }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @DelicateCoroutinesApi
    fun register() {
        if (ConnectivityUtils().isOnline(context!!)) {
            GlobalScope.launch(Dispatchers.Main) {
                val username = binding.registerName
                val password = binding.registerPassword
                val registerBody =
                    RegisterRequestBody(username.text.toString(), password.text.toString())
                val response: RegisterResponseBody =
                    RetrofitUserApi.RETROFIT_SERVICE.register(registerBody)
                if (response.uid.toInt() == -1) {
                    activity?.runOnUiThread {
                        Toast
                            .makeText(
                                activity?.applicationContext,
                                "Zvolte ine meno",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                } else {
                    val sharedPreference = activity?.applicationContext?.getSharedPreferences(
                        "PREFERENCE_NAME", Context.MODE_PRIVATE
                    )
                    val editor = sharedPreference?.edit()
                    editor?.putString("uid", response.uid)
                    editor?.putString("access", response.access)
                    editor?.putString("refresh", response.refresh)
                    editor?.apply()
                    findNavController().navigate(R.id.action_registerFragment_to_homeScreenFragment)
                }

            }
        } else {
            Toast.makeText(context!!, "CHYBA INTERNETOVE PRIPOJENIE", Toast.LENGTH_LONG).show()
        }
    }


}