package com.saycs.saycs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.parse.ParseUser

class UserprofileActivity : AppCompatActivity() {

    lateinit var nomDB : String
    lateinit var numPersonajeDB : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userprofile)
    }


    private fun fetchProfile (){
        val currentUser = ParseUser.getCurrentUser()
        nomDB = currentUser.getString("username").toString()
        numPersonajeDB = currentUser.getString("numPersonaje").toString()
    }
}