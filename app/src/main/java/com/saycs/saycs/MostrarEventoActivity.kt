package com.saycs.saycs

import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.saycs.saycs.databinding.ActivityMostrarEventoBinding
import com.saycs.saycs.mundo.model.Evento
import java.util.Date

class MostrarEventoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMostrarEventoBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMostrarEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //"Fecha: 29 de diciembre, 2023
        //                        \nHora: 8pm\nLugar: Plaza Claro \nDireccion: calle x "
        binding.nombreEventotxt.text= intent.extras?.getString("nombre") ?: " Aqui va algo"
        val fecha= intent.extras?.getString("fecha")
        val hora= intent.extras?.getString("hora")
        val lugar= intent.extras?.getString("lugar")
        val mensaje="Fecha: ${fecha}\nHora: ${hora}\nLugar: ${lugar}\nDireccion: "
        binding.informacionEvento.text= mensaje
        binding.Descripcion2.text=intent.extras?.getString("descripcion")
        val url= intent.extras?.getString("urlPoster")
        if (url != null) {
            Log.i("Daniel",url)
        }
        val image=binding.imagen
        val uri= Uri.parse(url)
        Glide.with(this).load(uri).into(image)
    }
}