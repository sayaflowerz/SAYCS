package com.saycs.saycs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.saycs.saycs.databinding.ActivityLoginempresaBinding

class LoginempresaActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginempresaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupBackButton()

        setupLoginButton()

    }

    private fun validateForm(): Boolean {
        val usuario = binding.username.text.toString()
        val contrasena = binding.password.text.toString()

        val todasLasCasillasTienenTexto = usuario.isNotEmpty() &&
                contrasena.isNotEmpty()

        return todasLasCasillasTienenTexto
    }

    private fun setupBackButton(){
        binding.DevolverButton.setOnClickListener{
            super.onBackPressed()
        }
    }

    private fun setupLoginButton(){
        binding.LogInButton.setOnClickListener{
            val usuario = binding.username.text.toString()
            val contrasena = binding.password.text.toString()
            ParseUser.logInInBackground(usuario, contrasena) { user: ParseUser?, e: ParseException? ->
                if (user != null) {

                    val sessionToken = user.sessionToken
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("sessionToken", sessionToken)
                    editor.apply()
                    Log.e("PARSE", "Login exitoso: $sessionToken")

                    val intent = Intent(this, MapActivity::class.java)
                    startActivity(intent)
                } else {
                    val toast = Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }

        }
    }


}