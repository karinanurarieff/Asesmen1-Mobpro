package com.karina0088.gpacalculator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.karina0088.gpacalculator.ui.theme.GPACalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GPACalculatorTheme {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("result/{nilai}") { backStackEntry ->
            val nilai = backStackEntry.arguments?.getString("nilai") ?: "0"
            ResultScreen(navController, nilai)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var tugas by remember { mutableStateOf("") }
    var uts by remember { mutableStateOf("") }
    var uas by remember { mutableStateOf("") }
    var mode by remember { mutableStateOf("Angka") }
    var error by remember { mutableStateOf("") }

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("GPA Calculator") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            TextField(value = tugas, onValueChange = { tugas = it }, label = { Text("Nilai Tugas") })
            TextField(value = uts, onValueChange = { uts = it }, label = { Text("Nilai UTS") })
            TextField(value = uas, onValueChange = { uas = it }, label = { Text("Nilai UAS") })

            Spacer(modifier = Modifier.height(10.dp))

            Text("Mode:")
            Row {
                RadioButton(selected = mode == "Angka", onClick = { mode = "Angka" })
                Text("Angka")

                Spacer(modifier = Modifier.width(10.dp))

                RadioButton(selected = mode == "Huruf", onClick = { mode = "Huruf" })
                Text("Huruf")
            }

            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                if (tugas.isEmpty() || uts.isEmpty() || uas.isEmpty()) {
                    error = "Semua input harus diisi"
                } else {
                    val t = tugas.toFloat()
                    val u = uts.toFloat()
                    val ua = uas.toFloat()

                    val hasil = (0.3 * t) + (0.3 * u) + (0.4 * ua)

                    val output = if (mode == "Huruf") {
                        when {
                            hasil >= 85 -> "A"
                            hasil >= 70 -> "B"
                            hasil >= 60 -> "C"
                            else -> "D"
                        }
                    } else {
                        String.format("%.2f", hasil)
                    }

                    navController.navigate("result/$output")
                }
            }) {
                Text("Hitung")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.telkomuniversity.ac.id")
                )
                context.startActivity(intent)
            }) {
                Text("Buka Website Kampus")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(navController: NavController, nilai: String) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hasil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("<")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text("Nilai Akhir:")
            Text(
                text = nilai,
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.student),
                contentDescription = "Student"
            )
        }
    }
}