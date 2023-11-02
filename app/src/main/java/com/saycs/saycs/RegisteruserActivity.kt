package com.saycs.saycs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.parse.ParseException
import com.parse.ParseUser
import com.saycs.saycs.databinding.ActivityRegisteruserBinding

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
                    val intent = Intent(baseContext, LoginuserActivity::class.java)
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
        var registroExitoso = false
        var userRegistro = ParseUser()
        userRegistro.username = binding.username.text.toString()
        userRegistro.setPassword (binding.password.text.toString())

        userRegistro.signUpInBackground{e: ParseException? ->
            if(e==null){
                registroExitoso=true
            }
            else{
                val errorMessage = e.message
                Log.e("Registro", "Código de error: ${e.code}")
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