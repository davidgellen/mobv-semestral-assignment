package com.example.cv2.service

import com.example.cv2.data.jsonmapper.EntryDatasourceWrapper
import com.example.cv2.data.request.PubsRequestBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL =
    "https://data.mongodb-api.com"
//    "https://android-kotlin-fun-mars-server.appspot.com"

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
    .client(client.build())
    .baseUrl(BASE_URL)
    .build()

interface PubService {

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

object RetrofitPubApi {
    val RETROFIT_SERVICE: PubService by lazy{
        retrofit.create(PubService::class.java)
    }
}