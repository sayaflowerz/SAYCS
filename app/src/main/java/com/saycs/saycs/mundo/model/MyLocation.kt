package com.saycs.saycs.mundo.model

import org.json.JSONObject
import org.osmdroid.util.GeoPoint
import java.util.Date
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

class MyLocation (){
    lateinit var fecha: Date
    lateinit var geoPoint: GeoPoint
    constructor(fecha: Date,geo: GeoPoint) : this() {
        this.fecha=fecha
        this.geoPoint=geo
    }
    constructor(fecha: Date) : this() {
        this.geoPoint= GeoPoint(4.62,-74.07)
        this.fecha=fecha
    }
    fun toJson():JSONObject{
        val obj = JSONObject()
        obj.put("date", fecha)
        obj.put("latitude", geoPoint.latitude)
        obj.put("longitud", geoPoint.longitude)
        return obj
    }
    fun distance(geo: GeoPoint) : Double{
        val lat1 = this.geoPoint.latitude
        val long1 = this.geoPoint.longitude
        val lat2 = geo.latitude
        val long2= geo.longitude
        val latDistance = Math.toRadians(lat1 - lat2)
        val lngDistance = Math.toRadians(long1 - long2)
        val a = sin(latDistance / 2) * sin(latDistance / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(lngDistance / 2) * sin(lngDistance / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val result = (GeoPoint.RADIUS_EARTH_METERS) * c
        return (result * 100.0).roundToInt() /100.0
    }

}