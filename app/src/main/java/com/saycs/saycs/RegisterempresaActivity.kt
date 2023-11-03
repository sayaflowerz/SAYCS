package com.saycs.saycs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.parse.ParseObject
import com.saycs.saycs.databinding.ActivityRegisterempresaBinding

class RegisterempresaActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegisterempresaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterempresaBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                saveData()
            }
            else{
                val toast = Toast.makeText(this, "Campos incompletos", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    fun saveData():Boolean {
        var registroExitoso = false
        Log.i("PARSE", "Intento de escritura en Parse")
        var firstObject = ParseObject("Empresa")

        val usuario = binding.UsuarioEmpresa.text.toString()
        val nombreEmpresa = binding.NombreEmpresa.text.toString()
        val direccionEmpresa= binding.DireccionEmpresa.text.toString()
        val contrasena = binding.password.text.toString()
        val actividadEconomica = binding.TipoEmpresa.text.toString()

        firstObject.put("usuario",usuario)
        firstObject.put("nombre_empresa",nombreEmpresa)
        firstObject.put("direccion_empresa",direccionEmpresa)
        firstObject.put("contrasena",contrasena)
        firstObject.put("actividad_economica",actividadEconomica)


        firstObject.saveInBackground { e ->
            if (e != null) {
                Log.e("PARSE", "error:",e)
                registroExitoso=false
            } else {
                Log.d("PARSE", "Objeto guardado.")
                firstObject.unpinInBackground()
                registroExitoso=true
            }
        }
    return  registroExitoso
    }

}

