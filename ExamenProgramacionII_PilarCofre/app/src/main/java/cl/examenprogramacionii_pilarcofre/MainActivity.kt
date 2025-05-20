package cl.examenprogramacionii_pilarcofre

//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
//import androidx.compose.foundation.layout.BoxScopeInstance.align
/* import androidx.compose.foundation.layout.BoxScopeInstance.align */
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.examenprogramacionii_pilarcofre.data.Medidor
import cl.examenprogramacionii_pilarcofre.ui.ListaGastosViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


//import androidx.compose.foundation.layout.BoxScopeInstance as BoxScopeInstance1

//import androidx.compose.foundation.layout.BoxScopeInstance as BoxScopeInstance1

//import androidx.compose.foundation.layout.BoxScopeInstance as BoxScopeInstance1
//import androidx.compose.foundation.layout.BoxScopeInstance as BoxScopeInstance2


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
          AppGastosUI()


        }
    }
}// end class


@Composable
fun AppGastosUI(
    navController: NavHostController = rememberNavController(),
    vmListaGastos: ListaGastosViewModel = viewModel(factory = ListaGastosViewModel.Factory)

){
    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") {
            PantallaListaGastosUI(
            vmListaGastos = vmListaGastos,
            onAdd = { navController.navigate( "form")}
            )

        }
        composable ("form") {
            PantallaFormUI(navController = navController, vmListaGastos = vmListaGastos)

        }

    }
}
//@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormUI(navController: NavController, vmListaGastos: ListaGastosViewModel) {
    val contexto = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var montoLectura by rememberSaveable { mutableIntStateOf( 0 ) }
    var fechalectura by rememberSaveable { mutableStateOf( "" ) }
    var tipoGasto by rememberSaveable { mutableStateOf( "" ) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {

            TopAppBar(
                title = { Text("Registro de Medidor") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                }
            )
        }
    ) { innerPadding :PaddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .wrapContentHeight()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                 TextField(value = montoLectura.toString(),
                     onValueChange = { montoLectura = it.toIntOrNull() ?: 0 } ,
                     label = { Text("Monto") })

                 TextField(value = fechalectura,
                     onValueChange = { fechalectura = it },
                     label = { Text("Fecha") })

                 Text( "Medidor de: ", fontWeight = FontWeight.SemiBold)

                 opcionMedidorUI(
                     tipoGasto = tipoGasto,
                     onTipoGastoSeleccionado = { tipoGasto = it }

                 )
                val coroutineScope = rememberCoroutineScope()
                Button( onClick =  {
                    when{
                        montoLectura <= 0 || fechalectura.isBlank() || tipoGasto.isBlank() -> {
                            coroutineScope.launch {
                            snackbarHostState.showSnackbar("Todos los campos son obligatorios")
                        }
                    }
                        else -> {

                        val nuevo = Medidor(
                        tipoGasto = tipoGasto,
                        montoLectura = montoLectura.toLong(),
                        fechaLectura = fechalectura
                        )
                        vmListaGastos.insertarLectura(nuevo)

                        navController.popBackStack()
                    }
                }
            }) {
                    Text(
                    text =  contexto.getString(R.string.btn_RegistrarMedicion))

                }


            }

    }
}



@SuppressLint("ComposableNaming")
//@Preview(showSystemUi = true)
@Composable
fun opcionMedidorUI(
   tipoGasto: String,
   onTipoGastoSeleccionado: (String) -> Unit
){

    val medidores  = listOf("Agua", "Luz", "Gas")
    var medidorSeleccionado by rememberSaveable { mutableStateOf( medidores[0]) }

    Column(Modifier.selectableGroup()) {
        medidores.forEach { medidor ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (medidor == tipoGasto),
                        onClick = { onTipoGastoSeleccionado (medidor) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (medidor == tipoGasto),
                    onClick = null
                )
                Text(
                    text = medidor,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }

}

//@Preview(showSystemUi = true)
@Composable
fun PantallaListaGastosUI(
    vmListaGastos: ListaGastosViewModel,
    onAdd:() -> Unit
){

    val contexto = LocalContext.current
    val medidas by vmListaGastos.medida.collectAsState()

    Scaffold(
        floatingActionButton =  {
            FloatingActionButton( onClick = onAdd) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar Registro Medida")
             }

        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row( horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                Text(
                    text =  contexto.getString(R.string.titulo_listado),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp)
                }

        LazyColumn (
            Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            items(medidas) { medidor ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    // Imagen de ícono según tipo de gasto
                    val imageRes = when (medidor.tipoGasto.lowercase()) {
                        "agua" -> R.drawable.agua
                        "luz" -> R.drawable.ampolleta
                        "gas" -> R.drawable.gas
                        else -> { R.drawable.interrogacio}
                    }
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = medidor.tipoGasto,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 16.dp)

                    )
                    Column {
                        Text(
                            text = medidor.tipoGasto,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Fecha: ${medidor.fechaLectura}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        val montoFormateado = NumberFormat.getNumberInstance(Locale("es", "CL"))
                            .format(medidor.montoLectura)
                        Text(
                            text = "Monto: $$montoFormateado",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        IconButton(onClick = {
                            vmListaGastos.eliminarLectura(medidor)
                        }){
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar"
                            )
                        }

                    }
                }

            }
        }
    }
}


    }




































