package com.example.trabalho_gerenciamento_de_projetos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.trabalho_gerenciamento_de_projetos.dashboard.DashboardScreen
import com.example.trabalho_gerenciamento_de_projetos.ui.theme.Trabalho_Gerenciamento_de_ProjetosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Trabalho_Gerenciamento_de_ProjetosTheme {
                Surface(modifier = Modifier.fillMaxSize()){
                    DashboardScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hi $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Trabalho_Gerenciamento_de_ProjetosTheme {
        Greeting("Android")
    }
}