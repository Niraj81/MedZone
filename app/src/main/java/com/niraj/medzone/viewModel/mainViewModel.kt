package com.niraj.medzone.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.niraj.medzone.data.Post
import com.niraj.medzone.data.userPosts
import com.niraj.medzone.network.ApiService
import com.niraj.medzone.network.makePostBody
import kotlinx.coroutines.launch


class mainViewModel : ViewModel() {

    var postList: userPosts by mutableStateOf(userPosts(Post = mutableListOf()))
    var loaded = MutableLiveData<Boolean>(false)

    fun getPosts(Symptoms : List<String>, Address : String){
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            loaded.value = false
            try {
                postList = apiService.getDoctors(Symptoms, Address)
                loaded.value = true
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

    fun sortList(Basis: String){
        var newList = postList.Post
        if(Basis == "Relief"){
            newList = newList.sortedBy {
                it.Relief
            }
            newList = newList.reversed()
        }else if(Basis == "Symptoms"){
            newList = newList.sortedBy {
                it.Matched
            }
        }else{
            // Distance
            newList = newList.sortedBy {
                    it.Distance
            }
        }
        postList.Post = newList
        loaded.value = true
    }
}