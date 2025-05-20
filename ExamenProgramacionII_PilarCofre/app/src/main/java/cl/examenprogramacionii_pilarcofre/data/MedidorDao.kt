package cl.examenprogramacionii_pilarcofre.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


//DEFINICION INTERFAZ DAO

@Dao
interface MedidorDao {
    @Query("SELECT * FROM Medidor")
    fun listarTodos(): Flow<List<Medidor>>

    @Query("SELECT * FROM Medidor WHERE id = :id")
    suspend fun  buscarPorId(id:Long): Medidor

    @Insert
    suspend fun insertarLectura(medidor: Medidor)

    @Update
    suspend fun modificarLectura(medidor: Medidor)

    @Delete
    suspend fun eliminarLectura(medidor: Medidor)



    @Query("DELETE FROM Medidor")
    suspend fun limpiarTodo()


}