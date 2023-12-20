package com.saycs.saycs

import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.parse.ParseObject
import com.parse.ParseUser
import com.saycs.saycs.databinding.ActivityFormularioEventoBinding

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.TextView
import android.widget.Button
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FormularioEventoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormularioEventoBinding

    //Variables de imagen
    val getContentGallery = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            isImageSelected = true
            uriUpload=it!!
        }
    )
    private var isImageSelected = false
    private lateinit var uriUpload : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFormularioEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBotons()

    }


    fun setupBotons(){
        binding.SignInButton.setOnClickListener{
            if (validateForm()){
                signInEvento()
                super.onBackPressed()
            }
        }

        binding.SubirImagenButton.setOnClickListener{
            getContentGallery.launch("image/*")
        }

        binding.DevolverButton.setOnClickListener{
            super.onBackPressed()
        }

        binding.FechaEvento.setOnClickListener {
            showDatePicker()
        }

        binding.HoraEvento.setOnClickListener{
            showTimePicker()
        }


    }

    private fun signInEvento (){
        val nombreEvento = binding.NombreEvento.text.toString()
        val fechaEvento = binding.FechaEvento.text.toString()
        val direccionEvento = binding.DireccionEvento.text.toString()
        val tematicaEvento = binding.TematicaEvento.text.toString()
        val horaEvento = binding.HoraEvento.text.toString()

        val evento = ParseObject("Evento")

        evento.put("nombre", nombreEvento)
        evento.put("fecha", fechaEvento)
        evento.put("direccion", direccionEvento)
        //TOCA COLOCAR UN CAMPO DE DESCRIPCION
        //NO EXISTE EN EL LAYOUT
        evento.put("descripcion","uwu")
        evento.put("tematica", tematicaEvento)
        evento.put("longitud","0")
        evento.put("latitud","0")
        evento.put("hora",horaEvento)

        evento.saveInBackground { e ->
            if (e == null) {
                Log.i("PARSE", "Evento guardado con éxito en PARSE")
            } else {
                Log.e("PARSE", "Error al guardar: " + e.localizedMessage)
            }
        }

    }

    private fun validateForm(): Boolean {
        val nombreEvento = binding.NombreEvento.text.toString()
        val fechaEvento = binding.FechaEvento.text.toString()
        val direccionEvento = binding.DireccionEvento.text.toString()
        val tematicaEvento = binding.TematicaEvento.text.toString()

        val todasLasCasillasTienenTexto = nombreEvento.isNotEmpty() && fechaEvento.isNotEmpty() && direccionEvento.isNotEmpty() && tematicaEvento.isNotEmpty()

        if (!isImageSelected){
            binding.SubirImagenButton.backgroundTintList= ColorStateList.valueOf(ContextCompat.getColor(this,R.color.RedButton))
        }

        return todasLasCasillasTienenTexto && isImageSelected
    }

    fun uploadFirebaseImage(uriUpload: Uri) {
        // Obtén una referencia al lugar donde las fotos serán guardadas
        val currentUser = ParseUser.getCurrentUser()
        val objectId = currentUser?.objectId
        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("images/${objectId}.png")

        // Inicia la carga del archivo
        storageRef.putFile(uriUpload)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                // La carga fue exitosa, aquí puedes obtener, por ejemplo, la URL de la imagen
                val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl
                downloadUrl?.addOnSuccessListener { uri ->
                    println("Imagen cargada con éxito. URL: $uri")
                }
            }
            .addOnFailureListener { exception: Exception ->
                // La carga falló, maneja el error
                println("Error al cargar la imagen: ${exception.message}")
            }
    }

    private fun showDatePicker() {
        // Instancia el calendario para obtener la fecha actual
        val calendar = Calendar.getInstance()

        // Crea un DatePickerDialog y asigna un OnDateSetListener
        val datePickerDialog = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            // Formatea la fecha seleccionada y la muestra o la usa como prefieras
            val selectedDate = Calendar.getInstance()
            selectedDate.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)

            // Establece el texto del EditText con la fecha formateada
            binding.FechaEvento.setText(formattedDate)

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

        // Muestra el DatePickerDialog
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        // Instancia el calendario para obtener la hora actual
        val calendar = Calendar.getInstance()

        // Crea un TimePickerDialog con la hora actual
        val timePickerDialog = TimePickerDialog(this, { _, hourOfDay, minute ->
            // Formatea la hora seleccionada y la muestra o la usa como prefieras
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formattedTime = timeFormat.format(calendar.time)

            // Supongamos que tienes un EditText para mostrar la hora
            binding.HoraEvento.setText(formattedTime)

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true) // true for 24 hour time format

        // Muestra el TimePickerDialog
        timePickerDialog.show()
    }
}