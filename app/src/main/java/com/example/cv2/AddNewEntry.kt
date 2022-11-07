package com.example.cv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.jsonmapper.Tag
import com.example.cv2.data.model.EntryViewModel

class AddNewEntry : Fragment() {

    private val entryViewModel: EntryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_new_entry, container, false)
        val navButton = view.findViewById<Button>(R.id.AddNewButton)

        navButton.setOnClickListener {
            val name = (view.findViewById(R.id.nameTextField) as EditText).text.toString()
            val businessName = (view.findViewById(R.id.businessNameTextField) as EditText).text.toString()
            val lat = (view.findViewById(R.id.lattitudeTextField) as EditText).text.toString().toFloat()
            val lon = (view.findViewById(R.id.longtitudeTextField) as EditText).text.toString().toFloat()

            // adds to list of entries
            val newEntry = Entry(null, lat, lon,
                Tag(businessName, null, null, null, null, null, null, null))
            entryViewModel.entries.add(0, newEntry)

            // args for next fragment
            val bundle = Bundle()
            bundle.putString("name", name)
            bundle.putString("businessName", businessName)
            bundle.putFloat("lattitude", lat)
            bundle.putFloat("longtitude", lon)

            findNavController().navigate(R.id.action_addNewEntry_to_entryInfo, bundle)
        }
        return view
    }

}