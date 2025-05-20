package cl.examenprogramacionii_pilarcofre.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.examenprogramacionii_pilarcofre.Aplicacion
import cl.examenprogramacionii_pilarcofre.data.Medidor
import cl.examenprogramacionii_pilarcofre.data.MedidorDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListaGastosViewModel(val medidorDao: MedidorDao): ViewModel() {
    val medida: StateFlow<List<Medidor>> = medidorDao
       .listarTodos()
       .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun insertarLectura(medidor: Medidor) {
        viewModelScope.launch (Dispatchers.IO) {
           try {
                medidorDao.insertarLectura(medidor)
           } catch (e: Exception) {
           }
        }
    }
    fun eliminarLectura(medidor: Medidor){
        viewModelScope.launch {
            medidorDao.eliminarLectura(medidor)
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val aplicacion = (this[APPLICATION_KEY] as Aplicacion)
                ListaGastosViewModel(aplicacion.medidorDao)
            }
        }

    }
  }


