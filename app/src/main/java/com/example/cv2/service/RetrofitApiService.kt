package com.example.cv2.service

import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.request.PubsRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

private const val BASE_URL =
    "https://data.mongodb-api.com"
//    "https://android-kotlin-fun-mars-server.appspot.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface RetrofitApiService {

    @Headers(
        "api-key: KHUu1Fo8042UwzczKz9nNeuVOsg2T4ClIfhndD2Su0G0LHHCBf0LnUF05L231J0M",
        "Content-Type: application/json",
        "Access-Control-Request-Headers: *"
    )
    @POST("app/data-fswjp/endpoint/data/v1/action/find")
    suspend fun getData(@Body requestBody: PubsRequestBody): EntryDatasourceWrapper

//    @GET("photos")
    suspend fun getTestData(): String
}

object RetrofitApi {
    val retrofitService: RetrofitApiService by lazy{
        retrofit.create(RetrofitApiService::class.java)
    }
}