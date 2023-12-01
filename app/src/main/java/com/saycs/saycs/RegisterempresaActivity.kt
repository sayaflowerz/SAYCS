package com.saycs.saycs

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseUser
import com.saycs.saycs.databinding.ActivityRegisterempresaBinding
import java.util.UUID
class RegisterempresaActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterempresaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterempresaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRegisterButton()

        setupBackButton()

    }

    private fun validateForm(): Boolean {
        val usuario = binding.UsuarioEmpresa.text.toString()
        val nombreEmpresa = binding.NombreEmpresa.text.toString()
        val direccionEmpresa= binding.DireccionEmpresa.text.toString()
        val contrasena = binding.password.text.toString()
        val actividadEconomica = binding.TipoEmpresa.text.toString()


        val todasLasCasillasTienenTexto = usuario.isNotEmpty() &&
                contrasena.isNotEmpty() &&
                nombreEmpresa.isNotEmpty() &&
                direccionEmpresa.isNotEmpty() &&
                actividadEconomica.isNotEmpty()

        return todasLasCasillasTienenTexto
    }

    private fun setupRegisterButton(){
        binding.SignInButton.setOnClickListener{
            if (validateForm()){
                if(saveData()){
                    val intent = Intent(baseContext, MapActivity::class.java)
                    intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                }
            }
            else{
                val toast = Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun setupBackButton(){
        binding.DevolverButton.setOnClickListener{
            super.onBackPressed()
        }
    }

    fun saveData():Boolean {
        var registroExitoso = true
        Log.i("PARSE", "Intento de escritura en Parse")
        var objectRegistro = ParseUser()


        val usuario = binding.UsuarioEmpresa.text.toString()
        val nombreEmpresa = binding.NombreEmpresa.text.toString()
        val direccionEmpresa= binding.DireccionEmpresa.text.toString()
        val contrasena = binding.password.text.toString()
        val actividadEconomica = binding.TipoEmpresa.text.toString()

        objectRegistro.username = usuario
        objectRegistro.setPassword(contrasena)
        objectRegistro.put("rol","empresa")
        val uniqueID = UUID.randomUUID().toString()
        objectRegistro.email="$uniqueID@gmail.com"
        objectRegistro.put("nombre_empresa",nombreEmpresa)
        objectRegistro.put("direccion_empresa",direccionEmpresa)
        objectRegistro.put("actividad_economica",actividadEconomica)


        objectRegistro.signUpInBackground{e: ParseException? ->
            if(e==null){
                // Registro exitoso, guarda el token de sesión en SharedPreferences
                val sessionToken = objectRegistro.sessionToken
                val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("sessionToken", sessionToken)
                editor.apply()
                registroExitoso=true
                Log.e("PARSE", "Registro exitoso: $sessionToken")
            }
            else{
                val errorMessage = e.message
                Log.e("Registro", "Código de error: ${e.code}")
                registroExitoso=false
            }

        }

        return registroExitoso

    }

}

