package com.saycs.saycs

class PersistirData private constructor(){
    companion object{
        @Volatile
        private var instacia: PersistirData? = null

        //0 es anonimo, 1 es usuario, 2 es promotor
        var tipoLogin =0

        var personajeUsuario = 1
        fun getInstancia():PersistirData{
            if(instacia==null){
                synchronized(this){
                    if(instacia==null){
                        instacia= PersistirData()
                    }
                }
            }
            return instacia!!
        }
    }
}