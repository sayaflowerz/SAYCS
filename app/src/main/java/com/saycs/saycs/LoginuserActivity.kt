package com.saycs.saycs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.saycs.saycs.databinding.ActivityLoginusuarioBinding

class LoginuserActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginusuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginusuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}