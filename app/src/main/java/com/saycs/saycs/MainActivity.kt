package com.saycs.saycs

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import com.bumptech.glide.Glide
import com.parse.ParseAnonymousUtils
import com.parse.ParseException
import com.parse.ParseUser
import com.saycs.saycs.PersistirData.Companion.tipoLogin
import com.saycs.saycs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //loginParse()

        val s= "https://i.gifer.com/fxcY.gif"
        val image= binding.entrada
        val uri = Uri.parse(s)
        Glide.with(this).load(uri).into(image)
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(baseContext, MapActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }, 3000)
    }
    private fun loginParse(){
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("sessionToken", null)

        if (sessionToken != null) {
            // Iniciar sesión automáticamente con el token de sesión
            ParseUser.becomeInBackground(sessionToken) { user: ParseUser?, e: ParseException? ->
                if (user != null) {
                    Log.d("PARSE-LOGIN","Token de usuario recuperado: ${user.username}")
                } else {
                    Log.d("PARSE-LOGIN","Error al iniciar sesión automáticamente")
                }
            }
        }else{
            Log.d("PARSE-LOGIN","Token de usuario no recuperado")
        }
    }


}