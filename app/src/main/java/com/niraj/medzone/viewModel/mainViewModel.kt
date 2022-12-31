package com.niraj.medzone.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niraj.medzone.data.Post
import com.niraj.medzone.network.ApiService
import com.niraj.medzone.network.makeFindBody
import com.niraj.medzone.network.makePostBody
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call


class mainViewModel : ViewModel() {

    fun List<String>.toQueryValue() = joinToString(separator = "&") { "array=$it" }

    fun getPosts(Symptoms : List<String>, Address : String){
        viewModelScope.launch {


            val apiService = ApiService.getInstance()

            try {
                val ret = apiService.check(Symptoms, "Bhopal, Madhya Pradesh")
                Log.d("JSON", ret.Post[0].DoctorName)
            }
            catch (e: Exception){
                Log.d("JSON", e.toString())
            }

        }
    }

    fun writePost(post: Post){
        viewModelScope.launch {
            val jsonRequestBody = makePostBody(post)

            val apiService = ApiService.getInstance()
            try {
                apiService.postDoctors(jsonRequestBody)
            }
            catch (e : Exception){
                Log.d("JSON", e.toString())
            }
        }
    }
}