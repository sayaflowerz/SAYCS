package com.saycs.saycs.mundo.controller

import android.content.Context
import com.saycs.saycs.mundo.model.Evento
import com.saycs.saycs.mundo.services.FileUtils

class EventosController (private val context: Context){
    var eventos : List<Evento> = mutableListOf()

    private val fileUtils : FileUtils = FileUtils(context)
    fun cargarEventos(){
        eventos=fileUtils.readEvents()
    }
}