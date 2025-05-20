package cl.examenprogramacionii_pilarcofre

import android.app.Application
import androidx.room.Room
import cl.examenprogramacionii_pilarcofre.data.BaseDatos

class Aplicacion : Application () { //Generador de instancias
    val db by lazy { Room.databaseBuilder( this,BaseDatos::class.java, "medidor.db")
        .fallbackToDestructiveMigration(false)
        .build()
    }
    val medidorDao by lazy {  db.medidorDao() }
}