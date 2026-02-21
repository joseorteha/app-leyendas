package com.example.casadehistorias.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.casadehistorias.ui.auth.AuthViewModel
import com.example.casadehistorias.ui.navigation.NavGraph
import com.example.casadehistorias.ui.navigation.Screen
import com.example.casadehistorias.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(
    onLogout: () -> Unit // Añadido el parámetro faltante
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authViewModel: AuthViewModel = hiltViewModel()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = FondoTarjeta,
                drawerContentColor = LunaLlena
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Casa de Historias",
                    modifier = Modifier.padding(24.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = AguaSagrada
                )
                NavigationDrawerItem(
                    label = { Text("📜 Lista de Historias") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.StoryList.route) {
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text("🗺️ Mapa de Leyendas") },
                    selected = false,
                    onClick = {
                        navController.navigate(Screen.StoryMap.route) {
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp), 
                    color = LunaLlena.copy(alpha = 0.1f)
                )
                NavigationDrawerItem(
                    label = { Text("🚪 Cerrar Sesión") },
                    selected = false,
                    onClick = {
                        authViewModel.signOut()
                        onLogout() // Llamada al callback de logout
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        // MUY IMPORTANTE: Aquí metemos el NavGraph para que el menú esté siempre disponible
        NavGraph(navController = navController)
    }
}
