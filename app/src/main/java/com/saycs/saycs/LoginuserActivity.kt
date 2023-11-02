package com.saycs.saycs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.parse.Parse
import com.parse.ParseException
import com.parse.ParseUser
import com.saycs.saycs.databinding.ActivityLoginusuarioBinding

class LoginuserActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginusuarioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginusuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Iniciar botones de usuario
        setupLoginButton()
        setupRegisterButton()

        //Iniciar botones de empresa
        setupLoginEmpresaButton()
        setupRegisterEmpresaButton()
    }

    //Funcion que revisa si los campos de texto tienen información
    private fun validateForm(): Boolean {
        val usuario = binding.username.text.toString()
        val contrasena = binding.password.text.toString()

        val todasLasCasillasTienenTexto = usuario.isNotEmpty() &&
                contrasena.isNotEmpty()

        return todasLasCasillasTienenTexto
    }

    //Funcion que se activa al tocar el botón de login
    private fun setupLoginButton() {
        binding.LogInButton.setOnClickListener {
            if (validateForm()) {
                val usuario = binding.username.text.toString()
                val contrasena = binding.password.text.toString()

                ParseUser.logInInBackground(usuario, contrasena) { user: ParseUser?, e: ParseException? ->
                    if (user != null) {
                        val intent = Intent(this, MapActivity::class.java)
                        startActivity(intent)
                    } else {
                        val toast = Toast.makeText(this, "El usuario no existe", Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            }
            else{
                val toast = Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }



    //Funcion que se activa al tocar el botón de register
    private fun setupRegisterButton (){
        binding.SignInButton.setOnClickListener{
            val intent = Intent (this, RegisteruserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRegisterEmpresaButton (){
        binding.RegisterEmpresa.setOnClickListener{
            val intent = Intent (this, RegisterempresaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupLoginEmpresaButton (){
        binding.LogInEmpresa.setOnClickListener{
            val intent = Intent (this, LoginempresaActivity::class.java)
            startActivity(intent)
        }
    }

}