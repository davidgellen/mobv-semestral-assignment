package com.example.cv2.database.repository

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.cv2.database.dao.PubDao
import com.example.cv2.data.entity.Pub
import com.example.cv2.data.request.RefreshTokenRequestBody
import com.example.cv2.data.response.PubResponseBody
import com.example.cv2.data.response.RegisterResponseBody
import com.example.cv2.mapper.PubMapper
import com.example.cv2.service.RetrofitNewPubApi
import com.example.cv2.service.RetrofitUserApi
import com.example.cv2.utils.ConnectivityUtils
import retrofit2.HttpException
import retrofit2.Response

class PubRepository(
    private val pubDao: PubDao,
    private val context: Context
) {

    private val connectivityUtils: ConnectivityUtils = ConnectivityUtils()

    @RequiresApi(Build.VERSION_CODES.M)
    suspend fun getAll() : List<Pub> {
        if (connectivityUtils.isOnline(context)) {
            Log.i("GET ALL PUBS", "FROM SERVICE")
            return loadJsonFromServer()
        } else {
            Log.i("GET ALL PUBS", "FROM DATABASE")
            return pubDao.getAll()
        }
    }

    fun getByImportedId(importedId: Long) : Pub {
        return pubDao.getByImportedId(importedId)
    }

    suspend fun insert(
        pubs: List<Pub>
    ) {
        return pubDao.insert(pubs)
    }

    suspend fun deleteAll() {
        pubDao.deleteAll()
    }

    private suspend fun loadJsonFromServer(): List<Pub> {
        val sharedPreference = context.getSharedPreferences(
            "PREFERENCE_NAME", Context.MODE_PRIVATE)
        val accessToken = "Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess")
        val uid = sharedPreference?.getString("uid", "defaultUid") ?: "defaultUid"
        val pubs: Response<MutableList<PubResponseBody>> = RetrofitNewPubApi.RETROFIT_SERVICE
            .getPubsWithPeople(accessToken, uid)

        try {
            if (pubs.isSuccessful) {
                return PubMapper().pubResponseListToPubEntityList(pubs.body()!!.toMutableList())
            } else {
                if (pubs.code() == 401) {
                    val body = RefreshTokenRequestBody(sharedPreference?.getString("refresh", "") ?: "")
                    val refreshResponse: Response<RegisterResponseBody> =
                        RetrofitUserApi.RETROFIT_SERVICE.refreshToken(uid, body)
                    val editor = sharedPreference?.edit()
                    editor?.putString("access", refreshResponse.body()?.access)
                    editor?.putString("refresh", refreshResponse.body()?.refresh)
                    editor?.apply()
                    val renewedPubs: Response<MutableList<PubResponseBody>> = RetrofitNewPubApi.RETROFIT_SERVICE
                        .getPubsWithPeople("Bearer " + (sharedPreference?.getString("access", "defaultAccess") ?: "defaultAccess"), uid)
                    return PubMapper().pubResponseListToPubEntityList(renewedPubs.body()!!.toMutableList())
                }
                return mutableListOf()
            }
        } catch (e: HttpException) {
            Log.e("Exception", "${e.message}")
            // TODO if exception == 401 return this function after refresh token
            return mutableListOf()

        } catch (e: Throwable) {
            Log.e("Ooops:", "Something else went wrong")
            return mutableListOf()

        }


    }

}