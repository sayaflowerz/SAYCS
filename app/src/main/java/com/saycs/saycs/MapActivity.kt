package com.saycs.saycs

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.parse.ParseAnonymousUtils
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser
import com.saycs.saycs.databinding.ActivityMapBinding
import com.saycs.saycs.mundo.controller.EventosController
import com.saycs.saycs.mundo.model.MyLocation
import com.saycs.saycs.mundo.services.FileUtils
import com.saycs.saycs.mundo.services.LocationService
import com.saycs.saycs.mundo.services.MapEventServices
import com.saycs.saycs.mundo.services.MapRenderingServices
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.util.Date

class MapActivity : AppCompatActivity(), LocationService.LocationUpdateListener {
    private lateinit var binding: ActivityMapBinding
    private lateinit var mapEventServices: MapEventServices
    private lateinit var mapRenderingServices: MapRenderingServices
    private lateinit var map: MapView
    private lateinit var locationService: LocationService
    private lateinit var eventosController: EventosController
    private val getSimplePermission= registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        esconderBotones()

        locationService= LocationService(this, this)
        map= binding.mapView
        mapRenderingServices= MapRenderingServices(this,map)
        mapEventServices = MapEventServices(map, mapRenderingServices)
        mapEventServices.createOverlayEvents()
        eventosController= EventosController(this)
        eventosController.cargarEventos()
        pintarEventosInteres()
        pintarUsuarios()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this, "Se necesita la ubicacion!!", Toast.LENGTH_LONG).show()
            }
            getSimplePermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        locationService.locationClient.lastLocation.addOnSuccessListener {
            if(it!=null){
                val geo= GeoPoint(it.latitude, it.longitude)
                mapRenderingServices.addMarker(geo, typeMarker = 'A', numPersonaje = 10)
                mapRenderingServices.currentLocation= MyLocation(Date(System.currentTimeMillis()),GeoPoint(it.latitude, it.longitude))
                mapRenderingServices.center(geo)
                updateUI(it)
            }else{
                //"Agregar cuando no se puede acceder a la funcion"
            }
        }
        binding.resgistrarUsuariobtn.setOnClickListener {
            startActivity(Intent(baseContext, LoginuserActivity::class.java))
        }

        binding.InteresesPefilbtn.setOnClickListener{
            startActivity(Intent(baseContext, InteresesActivity::class.java))
        }
        binding.locationbtn.setOnClickListener {
            locationService.locationClient.lastLocation.addOnSuccessListener {
                if (it!=null){
                    mapRenderingServices.center(GeoPoint(it.latitude,it.longitude))
                    updateUI(it)
                }
            }
        }

        binding.RegistrarEmpresa.setOnClickListener{
            startActivity(Intent(baseContext, RegisterempresaActivity::class.java))
        }
        binding.inscribirEventoButton.setOnClickListener{
            startActivity(Intent(baseContext, FormularioEventoActivity::class.java))
        }
        binding.resgistrarUsuariobtn.setOnClickListener {
            startActivity(Intent(baseContext, LoginuserActivity::class.java))
        }
        binding.logOutbtn.setOnClickListener{
            ParseUser.logOut()

            // Borrar el token de las preferencias compartidas
            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.remove("sessionToken") // Elimina el token
            editor.apply()
            val intent = Intent(baseContext, MapActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()
        }
    }

    private fun updateUI(location: Location){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Se necesita la ubicacion por favor!", Toast.LENGTH_LONG)
                    .show()
            }
            getSimplePermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        } else {
            mapRenderingServices.currentLocation.geoPoint= GeoPoint(location.latitude,location.longitude)
            mapRenderingServices.addMarker(mapRenderingServices.currentLocation.geoPoint, typeMarker = 'A', numPersonaje = 10)
        }
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        locationService.stopLocationUpdates()
    }

    override  fun onRestart(){
        super.onRestart()
        esconderBotones()
        Log.d("SEGUIMIENTO", "Entra a restart")
    }

    override  fun onStart(){
        super.onStart()
        esconderBotones()

        Log.d("SEGUIMIENTO", "Entra a start")
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        esconderBotones()
        map.controller.animateTo(mapRenderingServices.currentLocation.geoPoint)
        locationService.startLocationUpdates()

    }

    override fun onLocationUpdate(location: Location) {
        updateUI(location)
    }

    private fun pintarEventosInteres(){
        val eventos= eventosController.eventos
        for (i in eventos.indices){
            mapRenderingServices.addMarker(eventos[i])
            eventos[i].geoPoint
        }
    }


    private fun esconderBotones(){
        val usuarioActual = ParseUser.getCurrentUser()

// Verifica si el usuario está logueado
        if (usuarioActual != null) {
            // Verifica si el inicio de sesión es anónimo
            if (usuarioActual.email.isNullOrEmpty()) {
                binding.resgistrarUsuariobtn.visibility= View.VISIBLE
                binding.RegistrarEmpresa.visibility=View.VISIBLE

                binding.logOutbtn.visibility = View.GONE
                binding.inscribirEventoButton.visibility=View.GONE
                binding.InteresesPefilbtn.visibility=View.GONE

            } else {
                // Obtén el rol del usuario
                val rol = usuarioActual.getString("rol").toString()

                // Verifica el rol del usuario
                if (rol == "usuario") {
                    binding.resgistrarUsuariobtn.visibility= View.GONE
                    binding.RegistrarEmpresa.visibility=View.GONE

                    binding.logOutbtn.visibility = View.VISIBLE
                    binding.inscribirEventoButton.visibility=View.GONE
                    binding.InteresesPefilbtn.visibility=View.VISIBLE

                } else if (rol == "empresa") {
                    binding.resgistrarUsuariobtn.visibility= View.GONE
                    binding.RegistrarEmpresa.visibility=View.GONE

                    binding.logOutbtn.visibility = View.VISIBLE
                    binding.inscribirEventoButton.visibility=View.VISIBLE
                    binding.InteresesPefilbtn.visibility=View.GONE
                }
            }
        } else {
            // Maneja la situación cuando no hay un usuario logueado
        }

    }

    private fun pintarUsuarios(){
        val query = ParseQuery.getQuery(ParseUser::class.java)

        val listaPersonajes: MutableList<Int> = mutableListOf()


        query.findInBackground { users, e ->
            if (e == null) {
                // No hay error, procesa la lista de usuarios
                val locationList = users.mapNotNull { user ->
                    val longitud = user.getDouble("longitud")
                    val latitud = user.getDouble("latitud")
                    val numPersonaje = (user.getString("numPersonaje"))?.toInt()

                    val nombre = user.username
                    Log.d("userFetch",nombre)
                    if (numPersonaje != null) {
                        listaPersonajes.add(numPersonaje)
                    }
                    if (longitud != 0.0 && latitud != 0.0) Pair(longitud, latitud) else null
                }
                Log.d("numpersonajes", listaPersonajes.toString())

                for (i in listaPersonajes) {
                    val numPersonaje = listaPersonajes[i]
                    val (longitud, latitud) = locationList[i]


                    val geo = GeoPoint(latitud,longitud)
                    mapRenderingServices.addCharacter(geo, numPersonaje)

                }





                // Aquí tienes tu lista de pares de longitud y latitud
                // Haz algo con locationList
            } else {
                // Maneja el error
            }
        }
    }

}