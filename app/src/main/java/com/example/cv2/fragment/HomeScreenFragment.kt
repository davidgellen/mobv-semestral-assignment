package com.example.cv2.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.cv2.R
import com.example.cv2.databinding.FragmentHomeScreenBinding

class HomeScreenFragment : Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        val view: View = binding.root

        bindButtons()
        setMenuBar()
        return view
    }

    private fun setMenuBar() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_screen_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                logout()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun logout() {
        // TODO: delete shit from shared preferences
        val sharedPreference = activity?.getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE
        )

        val editor = sharedPreference?.edit()
        editor?.clear()
        editor?.apply()
        findNavController().navigate(R.id.action_homeScreenFragment_to_loginFragment)
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