package com.example.cv2.service

import com.example.cv2.data.request.AddFriendRequestBody
import com.example.cv2.data.response.AddFriendResponseBody
import com.example.cv2.data.response.ContactResponseBody
import com.example.cv2.data.response.RegisterResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://zadanie.mpage.sk/contact/"
private const val API_KEY = "c95332ee022df8c953ce470261efc695ecf3e784"

// TODO: global api key

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface FriendService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @GET("list.php")
    suspend fun allFriends(@Header("authorization") accessToken: String,
                           @Header("x-user") uid: String): List<ContactResponseBody>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @POST("message.php")
    suspend fun addFriend(@Header("authorization") accessToken: String,
                          @Header("x-user") uid: String,
                          @Body requestBody: AddFriendRequestBody): String

}

object RetrofitFriendApi {
    val RETOROFIT_SERVICE: FriendService by lazy {
        retrofit.create(FriendService::class.java)
    }
}