package com.example.carrental.api

import com.example.carrental.model.review.ReviewCarItem
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface APIservices {
    @GET
    suspend fun getAllReviews(@Url url: String): Response<List<ReviewCarItem>>

    companion object {
        private val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        private val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        operator fun invoke(basePath: String): APIservices {
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .baseUrl(basePath)
                .build()
                .create(APIservices::class.java)
        }
    }
}




