package com.example.cv2.service

import com.example.cv2.data.response.PubResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://zadanie.mpage.sk/bar/"
private const val API_KEY = "c95332ee022df8c953ce470261efc695ecf3e784"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface NewPubService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @GET("list.php")
    suspend fun getPubsWithPeople(@Header("authorization") accessToken: String,
                                  @Header("x-user") uid: String): List<PubResponseBody>

}

object RetrofitNewPubApi {
    val RETROFIT_SERVICE: NewPubService by lazy {
        retrofit.create(NewPubService::class.java)
    }
}