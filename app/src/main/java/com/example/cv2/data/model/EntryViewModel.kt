package com.example.cv2.data.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cv2.data.jsonmapper.Entry
import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.request.PubsRequestBody
import com.example.cv2.service.RetrofitApi

class EntryViewModel : ViewModel() {

    private var _entries = MutableLiveData<MutableList<Entry>>()

    val entries: LiveData<MutableList<Entry>>
        get() = _entries

    fun setEntries(list: MutableList<Entry>) {
        _entries.value = list
    }



    init {
        Log.d("GameFragment", "GameViewModel created!")
//        GlobalScope.launch{
//            if (_entries.size == 0) {
//                _entries = loadJsonFromServer().toMutableList()
////                activity?.runOnUiThread {
////                    recyclerView.adapter = EntryAdapter(view, entryViewModel.entries)
////                }
//                Log.i("data", "loaded from POST REQUEST, size: " + entries.size)
//            }
//        }
    }

    private suspend fun loadJsonFromServer(): List<Entry> {
        val requestBody = PubsRequestBody("bars", "mobvapp", "Cluster0")
        val entries: EntryDatasourceWrapper = RetrofitApi.retrofitService.getData(requestBody)
        return entries.documents
    }

}