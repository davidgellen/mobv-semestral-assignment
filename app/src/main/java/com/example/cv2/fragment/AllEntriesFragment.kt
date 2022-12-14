package com.example.cv2.fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.cv2.R
import com.example.cv2.adapter.PubAdapter
import com.example.cv2.PubApplication
import com.example.cv2.data.model.PubViewModel
import com.example.cv2.data.model.PubViewModelFactory
import com.example.cv2.databinding.FragmentAllEntriesBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class AllEntriesFragment : Fragment() {

    private var _binding: FragmentAllEntriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lat : String
    private lateinit var lon : String
    private lateinit var currentSort: String

    private val pubViewModel: PubViewModel by activityViewModels() {
        PubViewModelFactory(
            (activity?.application as PubApplication).pubRepository,
            activity?.application as PubApplication
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllEntriesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        val view = binding.root
        fusedLocationProviderClient = activity?.let {
            LocationServices.getFusedLocationProviderClient(it)
        }!!
        pubViewModel.fusedLocationProviderClient = fusedLocationProviderClient
        currentSort = "none"

        val recyclerView = binding.enttriesRecycleView
//        recyclerView.adapter = PubAdapter(view, pubViewModel)

        pubViewModel.loadData(false)
        pubViewModel.entries.observe(viewLifecycleOwner) {
            recyclerView.adapter = PubAdapter(view, pubViewModel)
        }

        binding.toAddEntryButton.visibility = View.INVISIBLE

        binding.entriesToMenuButton.setOnClickListener {
            findNavController().navigate(R.id.action_allEntriesFragment_to_homeScreenFragment)
        }
        binding.entriesToCheckInButton.setOnClickListener {
            findNavController().navigate(R.id.action_allEntriesFragment_to_checkInPubFragment)
        }
        binding.swipeContainer.setOnRefreshListener {
            pubViewModel.loadData(false)
            binding.swipeContainer.isRefreshing = false
        }

        setMenuBar()
        return view
    }

    private fun setMenuBar() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.pubs_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                pubViewModel.sortByProperty(menuItem.itemId)
                (binding.enttriesRecycleView.adapter as PubAdapter).notifyDataSetChanged()
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}