package com.example.cv2.service

import com.example.cv2.data.request.CheckIntoPubRequestBody
import com.example.cv2.data.request.RegisterRequestBody
import com.example.cv2.data.response.PubResponseBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://zadanie.mpage.sk/bar/"
private const val API_KEY = "c95332ee022df8c953ce470261efc695ecf3e784"

private val interceptor = HttpLoggingInterceptor()

private val client = OkHttpClient.Builder().apply {
    readTimeout(30, TimeUnit.SECONDS)
    writeTimeout(30, TimeUnit.SECONDS)
    connectTimeout(30, TimeUnit.SECONDS)
    addInterceptor(interceptor)
    addInterceptor { chain ->
        var request = chain.request()
        request = request.newBuilder()
            .build()
        val response = chain.proceed(request)
        response
    }
}


private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(client.build())
    .build()

interface NewPubService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @GET("list.php")
    suspend fun getPubsWithPeople(@Header("authorization") accessToken: String,
                                  @Header("x-user") uid: String): Response<MutableList<PubResponseBody>>

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @POST("message.php")
    suspend fun checkIntoPub(@Header("authorization") accessToken: String,
                             @Header("x-user") uid: String,
                             @Body requestBody: CheckIntoPubRequestBody): String
}

object RetrofitNewPubApi {
    val RETROFIT_SERVICE: NewPubService by lazy {
        retrofit.create(NewPubService::class.java)
    }
}