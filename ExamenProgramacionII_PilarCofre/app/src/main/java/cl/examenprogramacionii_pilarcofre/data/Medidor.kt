package cl.examenprogramacionii_pilarcofre.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity()
data class Medidor(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tipoGasto: String,
    val montoLectura: Long,
    val fechaLectura: String


)