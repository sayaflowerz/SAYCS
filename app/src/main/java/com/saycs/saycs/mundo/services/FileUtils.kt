package com.saycs.saycs.mundo.services

import android.content.Context
import android.util.Log
import com.saycs.saycs.mundo.model.Evento
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.osmdroid.util.GeoPoint
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class FileUtils (private val context: Context){
    fun writeJsonObjects(locations: MutableList<JSONObject>) {
        val jsonArray = JSONArray(locations)
        val jsonObject = JSONObject()
        jsonObject.put("locations", jsonArray)

        val filename = "locations.json"
        val file = File(context.getExternalFilesDir(null), filename)

        try {
            val output = BufferedWriter(FileWriter(file))
            output.write(jsonObject.toString()) // Write the JSON object to the file
            output.close()
            Log.i("Location", "File written to: $file")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("Location", "Error writing to file: $file")
        }
    }
    fun readEvents(): List<Evento>{
        val eventsList = mutableListOf<Evento>()

        try {
            val inputStream = context.assets.open("events.JSON")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            val json = String(buffer, Charsets.UTF_8)

            val jsonObject = JSONObject(json)
            val eventosArray = jsonObject.optJSONArray("eventos")

            if(eventosArray!= null){
                for (i in 0 until eventosArray.length()){
                    val json = eventosArray.optJSONObject(i)

                    val nombreEvento = json.optString("nombreEvento")
                    val descripcion = json.optString("descripcion")
                    val lugar = json.optString("lugar")
                    val etiquetaId = json.optString("etiquetaId")
                    val urlPoster = json.optString("urlPoster")

                    val fechaStr = json.optString("fecha")
                    val fecha = parseFecha(fechaStr) // Utiliza una función para parsear la fecha

                    val geopointObject = json.optJSONObject("geopoint")
                    val latitude = geopointObject?.optString("latitude") ?: "0.0"
                    val longitude = geopointObject?.optString("longitude") ?: "0.0"
                    val geoPoint = GeoPoint(latitude.toDouble(), longitude.toDouble())

                    val evento = Evento(nombreEvento, descripcion, geoPoint, fecha, lugar, etiquetaId,urlPoster)
                    eventsList.add(evento)
                }
            }

        }catch (e: JSONException){
            e.printStackTrace()
        }
        return eventsList
    }
    fun readLocations(): List<GeoPoint> {
        val locationsList = mutableListOf<GeoPoint>()
        val filename = "locations.json"
        val file = File(context.getExternalFilesDir(null), filename)
        if (!file.exists()) {
            return locationsList
        }
        try {
            BufferedReader(FileReader(file)).use { reader ->
                val input = reader.readText()
                val jsonObject = JSONObject(input)
                val myLocationJsonArray = jsonObject.optJSONArray("locations")

                if (myLocationJsonArray != null) {
                    for (i in 0 until myLocationJsonArray.length()) {
                        val json = myLocationJsonArray.optJSONObject(i)
                        val lat = json?.optDouble("latitude", 0.0)
                        val lon = json?.optDouble("longitud", 0.0)
                        if (lat != null && lon != null) {
                            val geo = GeoPoint(lat, lon)
                            locationsList.add(geo)
                        }
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace() // Handle the JSON parsing exception.
        }

        return locationsList
    }
    fun parseFecha(fechaStr: String): Date {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a")
        return dateFormat.parse(fechaStr)
    }
}