package com.example.casadehistorias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import com.example.casadehistorias.ui.MainContainer
import com.example.casadehistorias.ui.theme.CasaDeHistoriasTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CasaDeHistoriasTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Ahora el onLogout se maneja internamente en el MainContainer
                    MainContainer(onLogout = {
                        // Opcional: podrías poner lógica extra aquí
                    })
                }
            }
        }
    }
}
