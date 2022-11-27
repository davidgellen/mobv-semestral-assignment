package com.example.cv2.service

import com.example.cv2.data.request.RefreshTokenRequestBody
import com.example.cv2.data.request.RegisterRequestBody
import com.example.cv2.data.response.RegisterResponseBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://zadanie.mpage.sk/user/"
private const val API_KEY = "c95332ee022df8c953ce470261efc695ecf3e784"

private val interceptor = HttpLoggingInterceptor()

private val client = OkHttpClient.Builder().apply {
    readTimeout(20, TimeUnit.SECONDS)
    writeTimeout(20, TimeUnit.SECONDS)
    connectTimeout(20, TimeUnit.SECONDS)
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


interface UserService {

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @POST("create.php")
    suspend fun register(@Body requestBody: RegisterRequestBody): RegisterResponseBody

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @POST("login.php")
    suspend fun login(@Body requestBody: RegisterRequestBody): RegisterResponseBody

    @Headers(
        "Accept: application/json",
        "Content-Type: application/json",
        "Cache-Control: no-cache",
        "x-apikey: $API_KEY")
    @POST("refresh.php")
    suspend fun refreshToken(@Header("x-user") uid: String,
                             @Body requestBody: RefreshTokenRequestBody): Response<RegisterResponseBody>

}

object RetrofitUserApi {
    val RETROFIT_SERVICE: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}