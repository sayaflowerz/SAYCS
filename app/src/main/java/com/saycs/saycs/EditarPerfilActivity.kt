package com.saycs.saycs

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.saycs.saycs.databinding.ActivityEditarPerfilBinding

class EditarPerfilActivity : AppCompatActivity() , AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityEditarPerfilBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.spinerImage.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val a = p0?.getItemAtPosition(p2)
        if(a == 1){
            binding.imageView2.setImageDrawable(<Drawable>(R.drawable.personaje1))
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}