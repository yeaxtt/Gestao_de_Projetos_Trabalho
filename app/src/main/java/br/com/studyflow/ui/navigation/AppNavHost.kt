package br.com.studyflow.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.com.studyflow.ui.telas.FormularioAvaliacaoScreen
import br.com.studyflow.ui.telas.FormularioMateriaScreen
import br.com.studyflow.ui.telas.ListaAvaliacoesScreen
import br.com.studyflow.ui.telas.ListaMateriasScreen
import br.com.studyflow.ui.telas.PainelScreen
import br.com.studyflow.ui.theme.Indicator
import br.com.studyflow.ui.theme.Ink
import br.com.studyflow.ui.theme.Muted
import br.com.studyflow.ui.theme.NavBarColor
import br.com.studyflow.ui.viewmodel.AvaliacaoViewModel
import br.com.studyflow.ui.viewmodel.MateriaViewModel
import br.com.studyflow.ui.viewmodel.PainelViewModel

@Composable
fun AppNavHost(
    painelViewModel: PainelViewModel = viewModel(),
    materiaViewModel: MateriaViewModel = viewModel(),
    avaliacaoViewModel: AvaliacaoViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // A BottomBar é exibida nas telas principais dos agrupamentos (painel e listas)
    val isTopLevelRoute = currentDestination?.let { dest ->
        dest.hasRoute<AppRoute.Painel>() ||
                dest.hasRoute<AppRoute.MateriasLista>() ||
                dest.hasRoute<AppRoute.AvaliacoesLista>()
    } ?: true

    Scaffold(
        bottomBar = {
            if (isTopLevelRoute) {
                NavigationBar(containerColor = NavBarColor) {
                    ItemNavegacao(
                        navController = navController,
                        rota = AppRoute.Painel,
                        selecionado = currentDestination?.hasRoute<AppRoute.Painel>() == true,
                        icone = Icons.Default.Home,
                        rotulo = "Início"
                    )
                    ItemNavegacao(
                        navController = navController,
                        rota = AppRoute.MateriasLista,
                        selecionado = currentDestination?.hasRoute<AppRoute.MateriasLista>() == true,
                        icone = Icons.Default.Book,
                        rotulo = "Matérias"
                    )
                    ItemNavegacao(
                        navController = navController,
                        rota = AppRoute.AvaliacoesLista,
                        selecionado = currentDestination?.hasRoute<AppRoute.AvaliacoesLista>() == true,
                        icone = Icons.Default.Event,
                        rotulo = "Avaliações"
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Painel,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // ============================
            // Agrupamento: Painel
            // ============================
            composable<AppRoute.Painel> {
                PainelScreen(
                    painelUiState = painelViewModel.painelUiState,
                    onConcluirClick = { avaliacao ->
                        avaliacaoViewModel.alternarConclusao(avaliacao)
                    },
                    onEditarClick = { avaliacao ->
                        navController.navigate(AppRoute.AvaliacaoFormulario(avaliacaoId = avaliacao.id))
                    },
                    onTentarNovamente = {
                        painelViewModel.carregarPainel()
                    }
                )
            }

            // ============================
            // Agrupamento: Matérias
            // ============================
            composable<AppRoute.MateriasLista> {
                ListaMateriasScreen(
                    listaUiState = materiaViewModel.listaUiState,
                    exclusaoUiState = materiaViewModel.exclusaoUiState,
                    ordenacaoAtual = materiaViewModel.ordenacaoAtual,
                    onOrdenacaoChange = { materiaViewModel.alterarOrdenacao(it) },
                    onAdicionarClick = {
                        navController.navigate(AppRoute.MateriaFormulario())
                    },
                    onEditarClick = { materia ->
                        navController.navigate(AppRoute.MateriaFormulario(materiaId = materia.id))
                    },
                    onExcluirClick = { materia ->
                        materiaViewModel.excluir(materia)
                    },
                    onTentarNovamente = {
                        materiaViewModel.carregarMaterias()
                    },
                    onExclusaoMensagemMostrada = {
                        materiaViewModel.limparExclusaoUiState()
                    }
                )
            }

            composable<AppRoute.MateriaFormulario> { backStackEntry ->
                val args = backStackEntry.toRoute<AppRoute.MateriaFormulario>()
                val materiaId = args.materiaId

                LaunchedEffect(materiaId) {
                    if (materiaId == 0) {
                        materiaViewModel.prepararNovaMateria()
                    } else {
                        materiaViewModel.carregarMateria(materiaId)
                    }
                }

                FormularioMateriaScreen(
                    formularioUiState = materiaViewModel.formularioUiState,
                    salvarUiState = materiaViewModel.salvarUiState,
                    onSalvarClick = { materia ->
                        materiaViewModel.salvar(materia)
                    },
                    onVoltarClick = {
                        navController.popBackStack()
                    },
                    onTentarNovamente = {
                        if (materiaId == 0) {
                            materiaViewModel.prepararNovaMateria()
                        } else {
                            materiaViewModel.carregarMateria(materiaId)
                        }
                    },
                    onSalvoComSucesso = {
                        materiaViewModel.limparSalvarUiState()
                        navController.popBackStack()
                    },
                    onErroMostrado = {
                        materiaViewModel.limparSalvarUiState()
                    }
                )
            }

            // ============================
            // Agrupamento: Avaliações
            // ============================
            composable<AppRoute.AvaliacoesLista> {
                ListaAvaliacoesScreen(
                    listaUiState = avaliacaoViewModel.listaUiState,
                    exclusaoUiState = avaliacaoViewModel.exclusaoUiState,
                    ordenacaoAtual = avaliacaoViewModel.ordenacaoAtual,
                    filtroStatusAtual = avaliacaoViewModel.filtroStatusAtual,
                    materiaFiltroAtual = avaliacaoViewModel.materiaFiltroAtual,
                    onOrdenacaoChange = { avaliacaoViewModel.alterarOrdenacao(it) },
                    onFiltroStatusChange = { avaliacaoViewModel.alterarFiltroStatus(it) },
                    onMateriaFiltroChange = { avaliacaoViewModel.alterarMateriaFiltro(it) },
                    onAdicionarClick = {
                        navController.navigate(AppRoute.AvaliacaoFormulario())
                    },
                    onEditarClick = { avaliacao ->
                        navController.navigate(AppRoute.AvaliacaoFormulario(avaliacaoId = avaliacao.id))
                    },
                    onConcluirClick = { avaliacao ->
                        avaliacaoViewModel.alternarConclusao(avaliacao)
                    },
                    onExcluirClick = { avaliacao ->
                        avaliacaoViewModel.excluir(avaliacao)
                    },
                    onTentarNovamente = {
                        avaliacaoViewModel.carregarAvaliacoes()
                    },
                    onExclusaoMensagemMostrada = {
                        avaliacaoViewModel.limparExclusaoUiState()
                    }
                )
            }

            composable<AppRoute.AvaliacaoFormulario> { backStackEntry ->
                val args = backStackEntry.toRoute<AppRoute.AvaliacaoFormulario>()
                val avaliacaoId = args.avaliacaoId

                LaunchedEffect(avaliacaoId) {
                    if (avaliacaoId == 0) {
                        avaliacaoViewModel.prepararNovaAvaliacao()
                    } else {
                        avaliacaoViewModel.carregarAvaliacao(avaliacaoId)
                    }
                }

                FormularioAvaliacaoScreen(
                    formularioUiState = avaliacaoViewModel.formularioUiState,
                    salvarUiState = avaliacaoViewModel.salvarUiState,
                    onSalvarClick = { avaliacao ->
                        avaliacaoViewModel.salvar(avaliacao)
                    },
                    onVoltarClick = {
                        navController.popBackStack()
                    },
                    onCadastrarMateriaClick = {
                        navController.navigate(AppRoute.MateriaFormulario())
                    },
                    onTentarNovamente = {
                        if (avaliacaoId == 0) {
                            avaliacaoViewModel.prepararNovaAvaliacao()
                        } else {
                            avaliacaoViewModel.carregarAvaliacao(avaliacaoId)
                        }
                    },
                    onSalvoComSucesso = {
                        avaliacaoViewModel.limparSalvarUiState()
                        navController.popBackStack()
                    },
                    onErroMostrado = {
                        avaliacaoViewModel.limparSalvarUiState()
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.ItemNavegacao(
    navController: NavHostController,
    rota: AppRoute,
    selecionado: Boolean,
    icone: ImageVector,
    rotulo: String
) {
    NavigationBarItem(
        selected = selecionado,
        onClick = {
            navController.navigate(rota) {
                popUpTo(navController.graph.id) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        },
        icon = {
            Icon(
                imageVector = icone,
                contentDescription = rotulo
            )
        },
        label = { Text(rotulo) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Ink,
            selectedTextColor = Ink,
            indicatorColor = Indicator,
            unselectedIconColor = Muted,
            unselectedTextColor = Muted
        )
    )
}
