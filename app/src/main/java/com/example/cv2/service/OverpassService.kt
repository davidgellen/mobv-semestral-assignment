package com.example.cv2.service

import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://overpass-api.de/api/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface OverpassService {

@GET("interpreter")
suspend fun getPubsInArea(
    @Query(value = "data") data: String): EntryDatasourceWrapper

}

object RetrofitOverpassApi {
    val RETROFIT_SERVICE: OverpassService by lazy {
        retrofit.create(OverpassService::class.java)
    }
}