package com.niraj.medzone.data

import androidx.compose.runtime.*

@Stable
class postState (
    UserName: String,
    DoctorName: String,
    Symptoms: List<String>,
    Description: String,
    Age: Int,
    Address: String,
    Gender: String,
    Number: String,
    Relief: Float
) {
    var UserName by mutableStateOf(UserName)
    var DoctorName by mutableStateOf(DoctorName)
    var Symptoms by mutableStateOf(Symptoms)
    var Description by mutableStateOf(Description)
    var Age by mutableStateOf(Age)
    var Address by mutableStateOf(Address)
    var Gender by mutableStateOf(Gender)
    var Number by mutableStateOf(Number)
    var Relief by mutableStateOf(Relief)
}

@Composable
fun rememberPostState(
    UserName: String,
    DoctorName: String,
    Symptoms: List<String>,
    Description: String,
    Age: Int,
    Address: String,
    Gender: String,
    Number: String,
    Relief: Float
) : postState {
    return remember {
        postState(
            UserName = UserName,
            DoctorName = DoctorName,
            Symptoms = Symptoms,
            Description = Description,
            Age = Age,
            Address = Address,
            Gender = Gender,
            Number = Number,
            Relief = Relief
        )
    }
}