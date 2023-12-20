package com.saycs.saycs.mundo.controller

import android.content.Context
import android.util.Log
import com.saycs.saycs.mundo.model.Evento
import com.saycs.saycs.mundo.services.FileUtils
import com.saycs.saycs.repository.EventosRepository

class EventosController (private val context: Context){
    var eventos : List<Evento> = mutableListOf()

    private val fileUtils : FileUtils = FileUtils(context)
    private val eventosRepository : EventosRepository = EventosRepository()
    fun cargarEventos(onEventosCargados: () -> Unit){
        //eventos=fileUtils.readEvents()
        eventosRepository.cargarEventos { eventosFetched ->
            eventos=eventosFetched
            Log.d("EventosCargado","$eventos")
            onEventosCargados()
        }
    }
}