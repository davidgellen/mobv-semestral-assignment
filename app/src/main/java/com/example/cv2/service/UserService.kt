package com.example.cv2.service

import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.request.PubsRequestBody
import com.example.cv2.data.request.RegisterRequestBody
import com.example.cv2.data.response.RegisterResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://zadanie.mpage.sk/user/"
private const val API_KEY = "c95332ee022df8c953ce470261efc695ecf3e784"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface UserService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @POST("create.php")
    suspend fun create(@Body requestBody: RegisterRequestBody): RegisterResponseBody

}

object RetrofitUserApi {
    val RETROFIT_SERVICE: UserService by lazy{
        retrofit.create(UserService::class.java)
    }
}