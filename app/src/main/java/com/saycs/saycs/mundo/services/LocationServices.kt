package com.saycs.saycs.mundo.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.parse.ParseUser

class LocationService(private val context:Context, private val locationUpdateListener: LocationUpdateListener) {
    var locationClient:FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var locationRequest: LocationRequest
    private var locationCallback: LocationCallback

    init {
        locationRequest= createLocationRequest()
        locationCallback= createLocationCallback()
    }
    private fun createLocationRequest():LocationRequest{
        locationRequest= LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000).
        setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(5000)
            .build()
        return locationRequest
    }
    private fun createLocationCallback():LocationCallback{
        val locationCallback= object :LocationCallback(){
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                val location= result.lastLocation!!
                var userParse = ParseUser.getCurrentUser()
                userParse.put("latitud",location.latitude)
                userParse.put("longitud",location.longitude)
                userParse.saveInBackground { e ->
                    if (e == null) {
                        Log.d("PARSE","Localizacion actualizada a: "+location.latitude+","+location.longitude)
                    } else {
                        Log.d("PARSE","Error al actualizar Localizacion")
                    }
                }
                locationUpdateListener.onLocationUpdate(location)
            }
        }
        return locationCallback
    }
    fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)==
            PackageManager.PERMISSION_GRANTED){
            locationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
        }
    }
    fun stopLocationUpdates(){
        locationClient.removeLocationUpdates(locationCallback)
    }
    interface LocationUpdateListener {
        fun onLocationUpdate(location: Location)
    }
}


