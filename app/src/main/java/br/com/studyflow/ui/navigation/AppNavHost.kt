package br.com.studyflow.ui.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import br.com.studyflow.ui.telas.FeriadosScreen
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
import br.com.studyflow.ui.viewmodel.FeriadoViewModel
import br.com.studyflow.ui.viewmodel.MateriaViewModel
import br.com.studyflow.ui.viewmodel.PainelViewModel

@Composable
fun AppNavHost(
    painelViewModel: PainelViewModel = viewModel(factory = PainelViewModel.Factory),
    materiaViewModel: MateriaViewModel = viewModel(factory = MateriaViewModel.Factory),
    avaliacaoViewModel: AvaliacaoViewModel = viewModel(factory = AvaliacaoViewModel.Factory),
    feriadoViewModel: FeriadoViewModel = viewModel(factory = FeriadoViewModel.Factory)
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // A BottomBar é exibida nas telas principais dos agrupamentos (painel e listas)
    val isTopLevelRoute = currentDestination?.let { dest ->
        dest.hasRoute<AppRoute.Painel>() ||
                dest.hasRoute<AppRoute.MateriasLista>() ||
                dest.hasRoute<AppRoute.AvaliacoesLista>() ||
                dest.hasRoute<AppRoute.Feriados>()
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
                    ItemNavegacao(
                        navController = navController,
                        rota = AppRoute.Feriados,
                        selecionado = currentDestination?.hasRoute<AppRoute.Feriados>() == true,
                        icone = Icons.Default.CalendarMonth,
                        rotulo = "Feriados"
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppRoute.Painel,
            // consumeWindowInsets: as telas não somam de novo a barra do sistema que o Scaffold daqui já descontou
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .consumeWindowInsets(innerPadding)
        ) {
            // ============================
            // Agrupamento: Painel
            // ============================
            composable<AppRoute.Painel> {
                val painelUiState by painelViewModel.painelUiState.collectAsStateWithLifecycle()

                PainelScreen(
                    painelUiState = painelUiState,
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
                val listaUiState by materiaViewModel.listaUiState.collectAsStateWithLifecycle()
                val exclusaoUiState by materiaViewModel.exclusaoUiState.collectAsStateWithLifecycle()
                val materiaParaExcluir by materiaViewModel.materiaParaExcluir.collectAsStateWithLifecycle()
                val ordenacaoAtual by materiaViewModel.ordenacaoAtual.collectAsStateWithLifecycle()

                ListaMateriasScreen(
                    listaUiState = listaUiState,
                    exclusaoUiState = exclusaoUiState,
                    ordenacaoAtual = ordenacaoAtual,
                    onOrdenacaoChange = { materiaViewModel.alterarOrdenacao(it) },
                    onAdicionarClick = {
                        navController.navigate(AppRoute.MateriaFormulario())
                    },
                    onEditarClick = { materia ->
                        navController.navigate(AppRoute.MateriaFormulario(materiaId = materia.id))
                    },
                    materiaParaExcluir = materiaParaExcluir,
                    onExcluirClick = { materia ->
                        materiaViewModel.solicitarExclusao(materia)
                    },
                    onConfirmarExclusao = {
                        materiaViewModel.confirmarExclusao()
                    },
                    onCancelarExclusao = {
                        materiaViewModel.cancelarExclusao()
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

                val formularioUiState by materiaViewModel.formularioUiState.collectAsStateWithLifecycle()
                val salvarUiState by materiaViewModel.salvarUiState.collectAsStateWithLifecycle()
                val descarteVisivel by materiaViewModel.descarteVisivel.collectAsStateWithLifecycle()

                FormularioMateriaScreen(
                    formularioUiState = formularioUiState,
                    salvarUiState = salvarUiState,
                    descarteVisivel = descarteVisivel,
                    onSalvarClick = { materia ->
                        materiaViewModel.salvar(materia)
                    },
                    onVoltarClick = {
                        navController.popBackStack()
                    },
                    onSolicitarDescarte = {
                        materiaViewModel.solicitarDescarte()
                    },
                    onCancelarDescarte = {
                        materiaViewModel.cancelarDescarte()
                    },
                    onConfirmarDescarte = {
                        materiaViewModel.confirmarDescarte()
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
                val listaUiState by avaliacaoViewModel.listaUiState.collectAsStateWithLifecycle()
                val exclusaoUiState by avaliacaoViewModel.exclusaoUiState.collectAsStateWithLifecycle()
                val avaliacaoParaExcluir by avaliacaoViewModel.avaliacaoParaExcluir.collectAsStateWithLifecycle()
                val ordenacaoAtual by avaliacaoViewModel.ordenacaoAtual.collectAsStateWithLifecycle()
                val filtroStatusAtual by avaliacaoViewModel.filtroStatusAtual.collectAsStateWithLifecycle()
                val materiaFiltroAtual by avaliacaoViewModel.materiaFiltroAtual.collectAsStateWithLifecycle()

                ListaAvaliacoesScreen(
                    listaUiState = listaUiState,
                    exclusaoUiState = exclusaoUiState,
                    ordenacaoAtual = ordenacaoAtual,
                    filtroStatusAtual = filtroStatusAtual,
                    materiaFiltroAtual = materiaFiltroAtual,
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
                    avaliacaoParaExcluir = avaliacaoParaExcluir,
                    onExcluirClick = { avaliacao ->
                        avaliacaoViewModel.solicitarExclusao(avaliacao)
                    },
                    onConfirmarExclusao = {
                        avaliacaoViewModel.confirmarExclusao()
                    },
                    onCancelarExclusao = {
                        avaliacaoViewModel.cancelarExclusao()
                    },
                    onTentarNovamente = {
                        avaliacaoViewModel.carregarAvaliacoes()
                    },
                    onExclusaoMensagemMostrada = {
                        avaliacaoViewModel.limparExclusaoUiState()
                    }
                )
            }

            // ============================
            // Agrupamento: Feriados
            // ============================
            composable<AppRoute.Feriados> {
                val feriadosUiState by feriadoViewModel.feriadosUiState.collectAsStateWithLifecycle()
                val anoSelecionado by feriadoViewModel.anoSelecionado.collectAsStateWithLifecycle()

                FeriadosScreen(
                    feriadosUiState = feriadosUiState,
                    anoAtual = feriadoViewModel.anoAtual,
                    anoSelecionado = anoSelecionado,
                    onAnoChange = { feriadoViewModel.alterarAno(it) },
                    onTentarNovamente = { feriadoViewModel.carregarFeriados() }
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

                val formularioUiState by avaliacaoViewModel.formularioUiState.collectAsStateWithLifecycle()
                val salvarUiState by avaliacaoViewModel.salvarUiState.collectAsStateWithLifecycle()
                val descarteVisivel by avaliacaoViewModel.descarteVisivel.collectAsStateWithLifecycle()

                FormularioAvaliacaoScreen(
                    formularioUiState = formularioUiState,
                    salvarUiState = salvarUiState,
                    descarteVisivel = descarteVisivel,
                    onSalvarClick = { avaliacao ->
                        avaliacaoViewModel.salvar(avaliacao)
                    },
                    onVoltarClick = {
                        navController.popBackStack()
                    },
                    onSolicitarDescarte = {
                        avaliacaoViewModel.solicitarDescarte()
                    },
                    onCancelarDescarte = {
                        avaliacaoViewModel.cancelarDescarte()
                    },
                    onConfirmarDescarte = {
                        avaliacaoViewModel.confirmarDescarte()
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
