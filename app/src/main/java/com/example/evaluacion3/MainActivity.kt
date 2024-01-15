package com.example.evaluacion3

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.evaluacion3.ui.theme.Evaluacion3Theme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.evaluacion3.db.BaseDatos
import com.example.evaluacion3.db.Usuario
import com.example.evaluacion3.db.UsuarioDao
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
        }
        setContent {
            App()
        }
    }
}
@Composable
fun App(){
    var currentPage by remember { mutableStateOf("Inicio")}
    when (currentPage){
        "Inicio" -> PaginaInicio(onNavigateToSecondPage = {currentPage="Segunda"})
        "Segunda" -> SegundaPagina()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaginaInicio(onNavigateToSecondPage: () -> Unit) {
    var username by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "$ IplaBank",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp)
        )
        OutlinedTextField(value = username,
                          onValueChange = {username = it},
                          label = { Text("Usuario")},
                          modifier = Modifier
                              .fillMaxWidth()
                              .padding(bottom = 8.dp)
        )
        OutlinedTextField(value = password,
                          onValueChange = {password = it},
                          label = { Text("Contraseña")},
                          visualTransformation = PasswordVisualTransformation(),
                          keyboardOptions = KeyboardOptions.Default.copy(
                              imeAction = ImeAction.Done
                          ),
                          modifier = Modifier
                              .fillMaxWidth()
                              .padding(bottom = 16.dp)
        )
        Button(onClick = {},
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(top = 8.dp)
        ) {
            Text("Ingresar")

        }
        Button(
            onClick = onNavigateToSecondPage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ){
            Text("Solicitar cuenta")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SegundaPagina(){

    val usuarioDao = BaseDatos.getInstance(LocalContext.current).usuarioDao()
    var nombre by remember { mutableStateOf("")}
    var RUT by remember { mutableStateOf("")}
    var nacimiento by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var telefono by remember { mutableStateOf("")}
    var imagenFrente by remember { mutableStateOf<String?>(null)}
    var imagenTrasera by remember { mutableStateOf<String?>(null)}
    var latitud by remember { mutableStateOf(0.0) }
    var longitud by remember { mutableStateOf(0.0) }
    var lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
    var mostrarCamara by remember { mutableStateOf(false)}
    var esCamaraFrontal by remember { mutableStateOf(false)}

    Ubicacion(
        contexto = LocalContext.current,
        onSuccess = {ubicacion ->
            latitud = ubicacion.latitude
            longitud = ubicacion.longitude
        }
    )
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Solicitud Cuenta",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp)
        )
        OutlinedTextField(value = nombre,
            onValueChange = {nombre = it},
            label = { Text("Nombre Completo")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(value = RUT,
            onValueChange = {RUT = it},
            label = { Text("RUT")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(value = nacimiento,
            onValueChange = {nacimiento = it},
            label = { Text("Fecha Nacimiento")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(value = email,
            onValueChange = {email = it},
            label = { Text("Email")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        OutlinedTextField(value = telefono,
            onValueChange = {telefono = it},
            label = { Text("Teléfono")},
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Button(onClick = {mostrarCamara=true
                         esCamaraFrontal=true},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("\uD83D\uDCF8 Cédula Frontal")

        }
        Button(onClick = { mostrarCamara=true
                         esCamaraFrontal=false},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("\uD83D\uDCF8 Cédula Trasera")

        }
        if (mostrarCamara){
            Camara(
                onImageTaken = {photoPath ->
                    if (esCamaraFrontal){
                        imagenFrente = photoPath
                    } else {
                        imagenTrasera = photoPath
                    }
                    mostrarCamara = false
                }
            )
        }
        LaunchedEffect(imagenFrente, imagenTrasera){
            if (imagenFrente != null || imagenTrasera != null) {
                mostrarCamara = false
            }
        }
        Button(onClick = {
                         val nuevoUsuario = Usuario(
                             nombre = nombre,
                             rut = RUT.toInt(),
                             email = email,
                             telefono = telefono.toInt(),
                             latitud = latitud,
                             longitud = longitud,
                             imagenFrente = imagenFrente,
                             imagenTrasera = imagenTrasera,
                             fecha = Calendar.getInstance().time.toString()
                         )
            lifecycleScope.launch(Dispatchers.IO){
                usuarioDao.insertar(nuevoUsuario)
            }
        },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Solicitar")

        }
    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Ubicacion(
    contexto:Context,
    onSuccess:(ubicacion:Location) -> Unit
){
    val permissionState = rememberPermissionState(permission = Manifest.permission.ACCESS_COARSE_LOCATION)
    LaunchedEffect(Unit){
        permissionState.launchPermissionRequest()
    }
  try {
      val service = LocationServices
          .getFusedLocationProviderClient(contexto)
      val tarea = service.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY,
          null)
      tarea.addOnSuccessListener { onSuccess(it) }
  } catch (exception:SecurityException){
      Log.e(
          "MainActivity::Ubicacion",
          "No tiene permisos para conseguir la ubicacion"
      )
  }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun Camara(onImageTaken:(String)->Unit){
    val permissionState2 = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    LaunchedEffect(Unit){
        permissionState2.launchPermissionRequest()
    }
    val context = LocalContext.current
    val cameraController = remember {
        LifecycleCameraController(context)
    }
    val lifecycle = LocalLifecycleOwner.current
    val imagenCaptura = remember {ImageCapture.Builder().build()}
    cameraController.bindToLifecycle(lifecycle)
    if(permissionState2.status.isGranted){
        Column(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = {
                    context ->
                    val previewView = PreviewView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                    }
                    previewView.controller = cameraController
                    previewView
                },
                onRelease = {
                    cameraController?.unbind()
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Button(onClick = {
                val photoFile = File(context.filesDir,"foto_${System.currentTimeMillis()}.jpg")
                val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                imagenCaptura.takePicture(
                    outputFileOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            onImageTaken(photoFile.absolutePath)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("Camera","Error al guardar la foto: ${exception.message}",exception)
                        }
                    }
                )
            },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
                Text("Tomar Foto")
            }
        }
    } else {
        Text("")
    }
}


