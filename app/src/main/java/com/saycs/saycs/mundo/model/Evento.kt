package com.saycs.saycs.mundo.model

import org.osmdroid.util.GeoPoint
import java.util.Date

class Evento (val nombreEvento:String, val descripcion: String, val geoPoint: GeoPoint, val fecha: Date, val lugar:String, val urlPoster: String, val etiqueta: String ){

}