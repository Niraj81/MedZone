package com.niraj.medzone.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.niraj.medzone.data.Post
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody


fun makeFindBody(Symptoms : List<String>, Address : String) : RequestBody {
        val jsonObject = JsonObject()
        val issuesArray = JsonArray()
        Symptoms.forEach { issuesArray.add(it) }
        jsonObject.add("Issues", issuesArray)
        jsonObject.addProperty("location", Address)
        val gson = Gson()
        val jsonString = gson.toJson(jsonObject)
        val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return requestBody
}

/*
{
    "DoctorName" : "Rajesh Jain" ,
    "UserName": "Manant",
    "Relief" : 80,
    "Age": 25,
    "Gender": "Female" ,
    "Number" : "7887273737",
    "Address": "Mumbai, •Maharashtra" ,
    "Symptomps" : ["hair", "leg","eye"] ,
    "Description": "kuchbhi"
}
 */
fun makePostBody(post: Post) : RequestBody {
        val jsonObject = JsonObject()
        jsonObject.addProperty("DoctorName", post.DoctorName)
        jsonObject.addProperty("UserName", post.UserName)
        jsonObject.addProperty("Relief", post.Relief)
        jsonObject.addProperty("Age", post.Age)
        jsonObject.addProperty("Contact", post.Contact)
        jsonObject.addProperty("Address", post.Address)
        jsonObject.addProperty("Description", post.Description)
        jsonObject.addProperty("Gender", post.Gender)
        val symptomsArray = JsonArray()
        post.Symptoms.forEach {
                symptomsArray.add(it)
        }
        jsonObject.add("Symptoms", symptomsArray)
        val gson = Gson()
        val jsonString = gson.toJson(jsonObject)
        Log.d("JSON", jsonString)

        val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        return requestBody
}


