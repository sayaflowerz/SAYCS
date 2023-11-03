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

        verificarToken()

        locationService= LocationService(this, this)
        map= binding.mapView
        mapRenderingServices= MapRenderingServices(this,map)
        mapEventServices = MapEventServices(map, mapRenderingServices)
        mapEventServices.createOverlayEvents()
        eventosController= EventosController(this)
        eventosController.cargarEventos()
        pintarEventosInteres()
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
                mapRenderingServices.addMarker(geo, typeMarker = 'A')
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
        binding.locationbtn.setOnClickListener {
            locationService.locationClient.lastLocation.addOnSuccessListener {
                if (it!=null){
                    mapRenderingServices.center(GeoPoint(it.latitude,it.longitude))
                    updateUI(it)
                }
            }
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
            mapRenderingServices.addMarker(mapRenderingServices.currentLocation.geoPoint, typeMarker = 'A')
            mapRenderingServices.center(GeoPoint(location.latitude,location.longitude))
        }
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
        locationService.stopLocationUpdates()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
        map.controller.animateTo(mapRenderingServices.currentLocation.geoPoint)
        locationService.startLocationUpdates()
        runOnUiThread {
            verificarToken()
        }
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

    private fun verificarToken(){
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("sessionToken", null)

        if (ParseUser.getCurrentUser() != null) {
            binding.resgistrarUsuariobtn.visibility= View.GONE
            binding.logOutbtn.visibility = View.VISIBLE

            binding.logOutbtn.setOnClickListener{
                ParseUser.logOut()

                // Borrar el token de las preferencias compartidas
                val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.remove("sessionToken") // Elimina el token
                editor.apply()

                startActivity(Intent(baseContext, MapActivity::class.java))
            }
        } else {
            binding.resgistrarUsuariobtn.visibility= View.VISIBLE
            binding.logOutbtn.visibility = View.GONE

            binding.resgistrarUsuariobtn.setOnClickListener {
                startActivity(Intent(baseContext, LoginuserActivity::class.java))
            }
        }
    }

}