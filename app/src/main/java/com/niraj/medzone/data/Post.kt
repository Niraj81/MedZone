package com.niraj.medzone.data

data class Post(
    var UserName: String,
    var DoctorName: String,
    var Symptoms: List<String>,
    var Description: String,
    var Age: Int,
    val Address: String,
    var Gender: String,
    val Contact: String,
    val Relief: Int,

    val Matched: Int,
    val Distance: Double,
)