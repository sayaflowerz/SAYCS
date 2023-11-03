package com.saycs.saycs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.saycs.saycs.databinding.ActivityFormularioEventoBinding

class FormularioEventoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormularioEventoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFormularioEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}