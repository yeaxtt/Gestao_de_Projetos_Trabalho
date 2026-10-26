package br.com.studyflow.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import br.com.studyflow.ui.avaliacoes.AvaliacoesScreen
import br.com.studyflow.ui.components.BrandBar
import br.com.studyflow.ui.home.HomeScreen
import br.com.studyflow.ui.materias.MateriasScreen
import br.com.studyflow.ui.theme.*

enum class Destination(val route: String, val label: String, val icon: ImageVector) {
    HOME("home", "Início", Icons.Default.Home),
    MATERIAS("materias", "Matérias", Icons.Default.Book),
    AVALIACOES("avaliacoes", "Avaliações", Icons.Default.Event)
}

/** Ponto de entrada: liga o ViewModel às telas (que não conhecem o ViewModel). */
@Composable
fun StudyFlowApp(vm: StudyViewModel) {
    val nav = rememberNavController()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route ?: Destination.HOME.route
    val state by vm.uiState.collectAsStateWithLifecycle()

    StudyFlowTheme {
        StudyFlowScaffold(route, onNavigate = { target ->
            nav.navigate(target.route) {
                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }) { modifier ->
            NavHost(nav, Destination.HOME.route, modifier) {
                composable(Destination.HOME.route) {
                    HomeScreen(state, onToggle = vm::toggle, onUpdate = vm::updateAvaliacao)
                }
                composable(Destination.MATERIAS.route) {
                    MateriasScreen(state, onAdd = vm::addMateria, onUpdate = vm::updateMateria, onDelete = vm::deleteMateria)
                }
                composable(Destination.AVALIACOES.route) {
                    AvaliacoesScreen(
                        state,
                        onStatusChange = vm::setStatusFilter,
                        onMateriaFilterChange = vm::setMateriaFilter,
                        onToggle = vm::toggle,
                        onDelete = vm::delete,
                        onAdd = vm::addAvaliacao,
                        onUpdate = vm::updateAvaliacao
                    )
                }
            }
        }
    }
}

/** Barra superior + barra de navegação. Reaproveitado pelos @Preview das telas. */
@Composable
fun StudyFlowScaffold(currentRoute: String, onNavigate: (Destination) -> Unit, content: @Composable (Modifier) -> Unit) {
    Scaffold(
        containerColor = Background,
        topBar = { BrandBar() },
        bottomBar = {
            NavigationBar(containerColor = NavBarColor) {
                Destination.entries.forEach { dest ->
                    NavigationBarItem(
                        selected = currentRoute == dest.route,
                        onClick = { onNavigate(dest) },
                        icon = { Icon(dest.icon, contentDescription = null) },
                        label = { Text(dest.label, fontSize = 12.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Ink, selectedTextColor = Ink, indicatorColor = Indicator,
                            unselectedIconColor = Muted, unselectedTextColor = Muted
                        )
                    )
                }
            }
        }
    ) { padding -> content(Modifier.padding(padding)) }
}
