package com.niraj.medzone.utils

fun stringToList(Symptoms : String) : List<String> {
    var retList = Symptoms.split(",").toMutableList()
    for (i in 0..retList.size-1){
        if(retList[i].startsWith(" ")){
            retList[i] = retList[i].trimStart()
        }
        if(retList[i].endsWith(" ")){
            retList[i] = retList[i].trimEnd()
        }
    }
    return retList
}