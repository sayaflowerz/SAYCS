package com.saycs.saycs.mundo.services

import com.saycs.saycs.mundo.model.Evento
import com.saycs.saycs.repository.EventosRepository

class EventosServices {
    private var eventosRepository= EventosRepository()
    fun cargarEventos(): List<Evento>{
        return eventosRepository.cargarEventos()
    }
}