package com.saycs.saycs

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.saycs.saycs.databinding.ActivityMapBinding
import com.saycs.saycs.mundo.model.MyLocation
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
    private val getSimplePermission= registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        locationService= LocationService(this, this)
        map= binding.mapView
        mapRenderingServices= MapRenderingServices(this,map)
        mapEventServices = MapEventServices(map, mapRenderingServices)
        mapEventServices.createOverlayEvents()

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
                TODO("Agregar cuando no se puede acceder a la funcion")
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
    }

    override fun onLocationUpdate(location: Location) {
        updateUI(location)
    }
}