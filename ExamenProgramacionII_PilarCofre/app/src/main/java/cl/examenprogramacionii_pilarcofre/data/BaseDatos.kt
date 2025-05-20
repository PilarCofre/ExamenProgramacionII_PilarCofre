package cl.examenprogramacionii_pilarcofre.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Medidor::class], version = 2)
@TypeConverters(LocalDateConverter::class)

abstract class BaseDatos :  RoomDatabase() {
    abstract fun medidorDao(): MedidorDao
}