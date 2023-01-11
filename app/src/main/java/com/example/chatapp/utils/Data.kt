package com.example.chatapp.utils

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

object Data {
    fun online(online:String,auth: FirebaseAuth){
        val references = FirebaseDatabase.getInstance().getReference("users")
        val map = HashMap<String,Any>()
        map.put("online",online)
        references.child(auth.uid.toString()).updateChildren(map)
    }

    val isOnlineList =  MutableLiveData<Boolean>()

    fun add(isOnline:Boolean){
        isOnlineList.postValue(isOnline)
    }

    fun get():MutableLiveData<Boolean>{
        return isOnlineList
    }

    var color = "#312C50"
}