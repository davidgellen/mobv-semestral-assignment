package com.example.cv2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cv2.adapter.EntryAdapter
import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.request.RegisterRequestBody
import com.example.cv2.data.response.RegisterResponseBody
import com.example.cv2.databinding.FragmentRegisterBinding
import com.example.cv2.service.RetrofitPubApi
import com.example.cv2.service.RetrofitUserApi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view: View = binding.root

        binding.registerButtonRegister.setOnClickListener { register() }

        return view
    }

    @DelicateCoroutinesApi
    fun register() {
        GlobalScope.launch{
            val username = binding.registerName
            val password = binding.registerPassword
            val registerBody = RegisterRequestBody(username.toString(), password.toString())
            val response: RegisterResponseBody = RetrofitUserApi.RETROFIT_SERVICE.create(registerBody)
            Log.i("UID", response.uid)
            if (response.uid.toInt() == -1) {
                activity?.runOnUiThread {
                    Toast.makeText(activity?.applicationContext, "Zvolte ine meno", Toast.LENGTH_SHORT).show()
                }
            } else {
                val sharedPreference = activity?.applicationContext?.getSharedPreferences("PREFERENCE_NAME",
                    Context.MODE_PRIVATE)
                val editor = sharedPreference?.edit()
                editor?.putString("access", response.access)
                editor?.putString("refresh", response.refresh)
                editor?.apply()
            }

        }
    }


}