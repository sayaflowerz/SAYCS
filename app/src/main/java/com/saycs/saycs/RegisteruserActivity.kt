package com.saycs.saycs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.parse.ParseACL
import com.parse.ParseException
import com.parse.ParseUser
import com.saycs.saycs.databinding.ActivityRegisteruserBinding
import java.util.UUID



class RegisteruserActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisteruserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisteruserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRegisterButton()

        setupBackButton()

    }

    private fun validateForm(): Boolean {
        val usuario = binding.username.text.toString()
        val contrasena = binding.password.text.toString()

        val todasLasCasillasTienenTexto = usuario.isNotEmpty() &&
                contrasena.isNotEmpty()

        return todasLasCasillasTienenTexto
    }

    private fun setupRegisterButton(){
        binding.SignInButton.setOnClickListener{
            if (validateForm()){
                if(guardarUsuario()){
                    val intent = Intent(baseContext, MapActivity::class.java)
                    intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
                else{
                    val toast = Toast.makeText(this, "El registro no fue exitoso", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            else{
                val toast = Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun guardarUsuario():Boolean {
        var registroExitoso = true

        var userRegistro = ParseUser()
        userRegistro.username = binding.username.text.toString()
        userRegistro.setPassword (binding.password.text.toString())
        userRegistro.put("rol","usuario")
        userRegistro.put("numPersonaje","1")
        val uniqueID = UUID.randomUUID().toString()

        userRegistro.email= "$uniqueID@gmail.com"

        val acl = ParseACL()
        acl.publicReadAccess = true
        userRegistro.acl=acl

        userRegistro.signUpInBackground{e: ParseException? ->
            if(e==null){
                // Registro exitoso, guarda el token de sesión en SharedPreferences
                val sessionToken = userRegistro.sessionToken
                val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("sessionToken", sessionToken)
                editor.apply()
                registroExitoso=true
                Log.e("PARSE", "Registro exitoso: $sessionToken")
            }
            else{
                val errorMessage = e.message
                Log.e("PARSE", "Código de error: ${e.code}")
                registroExitoso=false
            }

        }

        return registroExitoso

    }

    private fun setupBackButton(){
        binding.DevolverButton.setOnClickListener{
            super.onBackPressed()
        }
    }


}