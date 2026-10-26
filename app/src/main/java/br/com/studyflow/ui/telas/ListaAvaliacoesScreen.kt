package br.com.studyflow.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.studyflow.model.Avaliacao
import br.com.studyflow.model.FiltroStatus
import br.com.studyflow.model.OrdenacaoAvaliacao
import br.com.studyflow.ui.components.AvaliacaoCard
import br.com.studyflow.ui.components.CabecalhoPagina
import br.com.studyflow.ui.components.ChipSegmento
import br.com.studyflow.ui.components.EstadoVazio
import br.com.studyflow.ui.components.LogoStudyFlow
import br.com.studyflow.ui.components.MateriaDropdown
import br.com.studyflow.ui.preview.DadosExemplo
import br.com.studyflow.ui.state.ExclusaoAvaliacaoUiState
import br.com.studyflow.ui.state.ListaAvaliacoesUiState
import br.com.studyflow.ui.theme.Background
import br.com.studyflow.ui.theme.CardColor
import br.com.studyflow.ui.theme.CardShape
import br.com.studyflow.ui.theme.Ink
import br.com.studyflow.ui.theme.Muted
import br.com.studyflow.ui.theme.Navy
import br.com.studyflow.ui.theme.StudyFlowTheme

@Composable
fun ListaAvaliacoesScreen(
    listaUiState: ListaAvaliacoesUiState,
    exclusaoUiState: ExclusaoAvaliacaoUiState,
    ordenacaoAtual: OrdenacaoAvaliacao,
    filtroStatusAtual: FiltroStatus,
    materiaFiltroAtual: Int?,
    onOrdenacaoChange: (OrdenacaoAvaliacao) -> Unit,
    onFiltroStatusChange: (FiltroStatus) -> Unit,
    onMateriaFiltroChange: (Int?) -> Unit,
    onAdicionarClick: () -> Unit,
    onEditarClick: (Avaliacao) -> Unit,
    onConcluirClick: (Avaliacao) -> Unit,
    avaliacaoParaExcluir: Avaliacao?,
    onExcluirClick: (Avaliacao) -> Unit,
    onConfirmarExclusao: () -> Unit,
    onCancelarExclusao: () -> Unit,
    onTentarNovamente: () -> Unit,
    onExclusaoMensagemMostrada: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Reage aos estados da exclusão exibindo Snackbar
    LaunchedEffect(exclusaoUiState) {
        when (exclusaoUiState) {
            is ExclusaoAvaliacaoUiState.Sucesso -> {
                snackbarHostState.showSnackbar("Avaliação excluída com sucesso")
                onExclusaoMensagemMostrada()
            }
            is ExclusaoAvaliacaoUiState.Erro -> {
                snackbarHostState.showSnackbar(exclusaoUiState.mensagem)
                onExclusaoMensagemMostrada()
            }
            else -> {}
        }
    }

    // Diálogo de confirmação para exclusão: o item pendente fica na ViewModel
    avaliacaoParaExcluir?.let { avaliacao ->
        ConfirmacaoExclusaoAvaliacaoDialog(
            avaliacao = avaliacao,
            onConfirmar = onConfirmarExclusao,
            onDispensar = onCancelarExclusao
        )
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ListaAvaliacoesTopAppBar(
                ordenacaoAtual = ordenacaoAtual,
                onOrdenacaoChange = onOrdenacaoChange
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAdicionarClick,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nova avaliação") },
                containerColor = Navy,
                contentColor = Color.White,
                shape = CardShape
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Indicador de progresso linear durante a exclusão
            if (exclusaoUiState is ExclusaoAvaliacaoUiState.Excluindo) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (listaUiState) {
                is ListaAvaliacoesUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ListaAvaliacoesUiState.Sucesso -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp)
                    ) {
                        item {
                            CabecalhoPagina("Avaliações", "Filtre e acompanhe todos os prazos.")
                            Spacer(Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                FiltroStatus.entries.forEach { filtro ->
                                    ChipSegmento(filtro.titulo, filtroStatusAtual == filtro) {
                                        onFiltroStatusChange(filtro)
                                    }
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                            MateriaDropdown(
                                materias = listaUiState.materias,
                                selecionadaId = materiaFiltroAtual,
                                rotuloTodas = "Todas as matérias",
                                onSelecionar = onMateriaFiltroChange
                            )
                        }
                        if (listaUiState.avaliacoes.isEmpty()) {
                            item { EstadoVazio("Nenhum resultado", "Altere os filtros ou adicione uma avaliação.") }
                        }
                        items(listaUiState.avaliacoes, key = { it.avaliacao.id }) { detalhes ->
                            AvaliacaoCard(
                                detalhes = detalhes,
                                onConcluirClick = { onConcluirClick(detalhes.avaliacao) },
                                onEditarClick = { onEditarClick(detalhes.avaliacao) },
                                onExcluirClick = { onExcluirClick(detalhes.avaliacao) }
                            )
                        }
                    }
                }

                is ListaAvaliacoesUiState.Erro -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = listaUiState.mensagem,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(onClick = onTentarNovamente) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaAvaliacoesTopAppBar(
    ordenacaoAtual: OrdenacaoAvaliacao,
    onOrdenacaoChange: (OrdenacaoAvaliacao) -> Unit
) {
    CenterAlignedTopAppBar(
        title = { LogoStudyFlow() },
        actions = {
            var menuAberto by remember { mutableStateOf(false) }

            IconButton(onClick = { menuAberto = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "Ordenar avaliações"
                )
            }

            DropdownMenu(
                expanded = menuAberto,
                onDismissRequest = { menuAberto = false }
            ) {
                OrdenacaoAvaliacao.entries.forEach { opcao ->
                    val selecionado = opcao == ordenacaoAtual
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = opcao.titulo,
                                fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal,
                                color = if (selecionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        },
                        trailingIcon = {
                            if (selecionado) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selecionado",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        onClick = {
                            menuAberto = false
                            onOrdenacaoChange(opcao)
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Composable
fun ConfirmacaoExclusaoAvaliacaoDialog(
    avaliacao: Avaliacao,
    onConfirmar: () -> Unit,
    onDispensar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDispensar,
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(text = "Confirmar exclusão", color = Ink)
        },
        text = {
            Text(text = "Deseja realmente excluir a avaliação \"${avaliacao.titulo}\"?", color = Muted)
        },
        containerColor = CardColor,
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(
                    text = "Excluir",
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDispensar) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(name = "Avaliações", showSystemUi = true)
@Composable
private fun ListaAvaliacoesScreenPreview() {
    StudyFlowTheme {
        ListaAvaliacoesScreen(
            listaUiState = ListaAvaliacoesUiState.Sucesso(DadosExemplo.avaliacoesDetalhes, DadosExemplo.materias),
            exclusaoUiState = ExclusaoAvaliacaoUiState.Ocioso,
            ordenacaoAtual = OrdenacaoAvaliacao.PRIORIDADE_DESC,
            filtroStatusAtual = FiltroStatus.TODAS,
            materiaFiltroAtual = null,
            onOrdenacaoChange = {}, onFiltroStatusChange = {}, onMateriaFiltroChange = {},
            onAdicionarClick = {}, onEditarClick = {}, onConcluirClick = {},
            avaliacaoParaExcluir = null, onExcluirClick = {}, onConfirmarExclusao = {}, onCancelarExclusao = {},
            onTentarNovamente = {}, onExclusaoMensagemMostrada = {}
        )
    }
}
