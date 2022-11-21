package com.example.cv2.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cv2.R
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.model.PubViewModel

class EntryDetailFragment : Fragment() {

    private val pubViewModel: PubViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_entry_detail, container, false)
        val pub: Pub = arguments?.getSerializable("entry") as Pub
//        view.findViewById<TextView>(R.id.detailNameTextView).text = pub.name
//        view.findViewById<TextView>(R.id.detailOpeningHours).text = "Hodiny: " + mapNullStringToEmpty(pub.tags.opening_hours)
//        view.findViewById<TextView>(R.id.detailCity).text = "Adresa: " + createAddress(pub.tags.street,pub.tags.houseNumber, pub.tags.city, pub.tags.country)
//        view.findViewById<TextView>(R.id.editWebsite).text = "Stranka: " + mapNullStringToEmpty(pub.tags.website)
        view.findViewById<ImageButton>(R.id.editDeleteCurrent).setOnClickListener {
//            pubViewModel.entries.value?.remove(entry) TODO: fix deleting
            findNavController().navigate(R.id.action_entryDetailFragment_to_allEntriesFragment)
        }
        return view
    }

    private fun createAddress(
        street: String?,
        houseNumber: String?,
        city: String?,
        country: String?
    ) : String {
        if (street == null && houseNumber == null && city == null && country == null) {
            return "Neznáma"
        }
        return mapNullStringToEmpty(street) + " " + mapNullStringToEmpty(houseNumber) + ", " +
                mapNullStringToEmpty(city) + ", " + mapNullStringToEmpty(country)
    }

    private fun mapNullStringToEmpty(
        string: String?
    ) : String {
        return string ?: ""
    }
}