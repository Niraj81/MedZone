package com.niraj.medzone.network
import androidx.compose.ui.layout.ScaleFactor
import com.google.gson.JsonObject
import com.niraj.medzone.BuildConfig
import com.niraj.medzone.data.userPosts
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface ApiService {



//    @Headers("Content-Type: application/json")
//    @POST("doctors/get")
//    suspend fun getDoctors(@Body sympRequest: RequestBody) : userPosts


    @GET("doctors/get")
    suspend fun getDoctors(
        @Query("array", encoded = true) Symptoms : String,
        @Query("string") Location : String
    ) : userPosts

    @GET("doctors/get")
    suspend fun check(
        @Query("array", encoded = true) Symptoms : List<String>,
        @Query("string") Location : String
    ) : userPosts

    @Headers("Content-Type: application/json")
    @POST("doctors/post")
    suspend fun postDoctors(@Body sympRequest: RequestBody) : Unit

    companion object {
        var apiService: ApiService? = null
        fun getInstance() : ApiService {
            if(apiService == null){
                apiService = Retrofit.Builder()
                    .baseUrl(BuildConfig.API_LINK)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}