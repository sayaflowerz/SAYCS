package com.saycs.saycs.repository

import android.util.Log
import com.parse.ParseObject
import com.parse.ParseQuery
import com.saycs.saycs.mundo.model.Evento
import org.osmdroid.util.GeoPoint
import java.util.Date


class EventosRepository {

    fun cargarEventos(callback: (List<Evento>) -> Unit) {
        val eventosLocales = mutableListOf<Evento>()

        val query = ParseQuery.getQuery<ParseObject>("Evento")
        query.findInBackground { eventosFetched, e ->
            if (e == null) {
                for (evento in eventosFetched){
                    val nombreEvento = evento.get("nombre").toString()
                    val descripcion =evento.get("descripcion").toString()
                    val fecha=evento.get("fecha").toString()
                    val hora=evento.get("hora").toString()
                    val direccion=evento.get("direccion").toString()
                    val etiqueta=evento.get("tematica").toString()
                    val urlPoster=evento.get("urlPoster").toString()
                    val longitud=evento.get("longitud").toString()
                    val latitud=evento.get("latitud").toString()

                    val geoPoint = GeoPoint(latitud.toDouble(),longitud.toDouble())

                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    //CAMBIAR LOGICA DE LA FECHA FALSA, HACER FETCH DEL FORMAT REAL
                    //NO SE DEBE INGRESAR LA HORA ACTUAL, SINO LA TRADUCCIÓN DEL CAMPO FECHA
                    val currentDate = Date()

                    eventosLocales.add(Evento(nombreEvento,
                        descripcion, geoPoint,currentDate,direccion,urlPoster,etiqueta))
                    Log.d("PARSE-DB","Éxito al hacer fetch de eventos desde la DB")
                }
                callback(eventosLocales) // Llamar al callback con los eventos
            } else {
                Log.d("PARSE-DB", "Error al hacer fetch de eventos desde la DB")
                callback(emptyList()) // Llamar al callback con lista vacía en caso de error
            }
        }
    }
}