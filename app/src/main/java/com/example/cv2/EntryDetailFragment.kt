package com.example.cv2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.cv2.data.Entry

class EntryDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_entry_detail, container, false)
        val entry: Entry = arguments?.getSerializable("entry") as Entry
        view.findViewById<TextView>(R.id.detailNameTextView).text = entry.tags.name
        view.findViewById<TextView>(R.id.detailOpeningHours).text = "Hodiny: " + mapNullStringToEmpty(entry.tags.opening_hours)
        view.findViewById<TextView>(R.id.detailCity).text = "Adresa: " + createAddress(entry.tags.street,entry.tags.houseNumber, entry.tags.city, entry.tags.country)
        view.findViewById<TextView>(R.id.editWebsite).text = "Stranka: " + mapNullStringToEmpty(entry.tags.website)
        view.findViewById<ImageButton>(R.id.editDeleteCurrent).setOnClickListener {
            AllEntriesFragment.globalMutableEntries.remove(entry)
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