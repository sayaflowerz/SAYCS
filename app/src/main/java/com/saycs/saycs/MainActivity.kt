package com.saycs.saycs

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.saycs.saycs.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val s= "https://i.gifer.com/fxcY.gif"
        val image= binding.entrada
        val uri = Uri.parse(s)
        Glide.with(this).load(uri).into(image)
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(baseContext, MapActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            finish()
        }, 3000)
    }
}