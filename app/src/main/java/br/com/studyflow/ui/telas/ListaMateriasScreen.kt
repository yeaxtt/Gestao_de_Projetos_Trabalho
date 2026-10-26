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
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
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
import androidx.compose.ui.unit.sp
import br.com.studyflow.model.Materia
import br.com.studyflow.model.MateriaDetalhes
import br.com.studyflow.model.OrdenacaoMateria
import br.com.studyflow.ui.components.CabecalhoPagina
import br.com.studyflow.ui.components.EstadoVazio
import br.com.studyflow.ui.components.LogoStudyFlow
import br.com.studyflow.ui.components.StudyCard
import br.com.studyflow.ui.preview.DadosExemplo
import br.com.studyflow.ui.state.ExclusaoMateriaUiState
import br.com.studyflow.ui.state.ListaMateriasUiState
import br.com.studyflow.ui.theme.Background
import br.com.studyflow.ui.theme.CardColor
import br.com.studyflow.ui.theme.CardShape
import br.com.studyflow.ui.theme.Ink
import br.com.studyflow.ui.theme.Muted
import br.com.studyflow.ui.theme.Navy
import br.com.studyflow.ui.theme.StudyFlowTheme

@Composable
fun ListaMateriasScreen(
    listaUiState: ListaMateriasUiState,
    exclusaoUiState: ExclusaoMateriaUiState,
    ordenacaoAtual: OrdenacaoMateria,
    onOrdenacaoChange: (OrdenacaoMateria) -> Unit,
    onAdicionarClick: () -> Unit,
    onEditarClick: (Materia) -> Unit,
    materiaParaExcluir: Materia?,
    onExcluirClick: (Materia) -> Unit,
    onConfirmarExclusao: () -> Unit,
    onCancelarExclusao: () -> Unit,
    onTentarNovamente: () -> Unit,
    onExclusaoMensagemMostrada: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Reage aos estados da exclusão exibindo Snackbar
    LaunchedEffect(exclusaoUiState) {
        when (exclusaoUiState) {
            is ExclusaoMateriaUiState.Sucesso -> {
                snackbarHostState.showSnackbar("Matéria excluída com sucesso")
                onExclusaoMensagemMostrada()
            }
            is ExclusaoMateriaUiState.Erro -> {
                snackbarHostState.showSnackbar(exclusaoUiState.mensagem)
                onExclusaoMensagemMostrada()
            }
            else -> {}
        }
    }

    // Diálogo de confirmação para exclusão: o item pendente fica na ViewModel
    materiaParaExcluir?.let { materia ->
        ConfirmacaoExclusaoMateriaDialog(
            materia = materia,
            onConfirmar = onConfirmarExclusao,
            onDispensar = onCancelarExclusao
        )
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ListaMateriasTopAppBar(
                ordenacaoAtual = ordenacaoAtual,
                onOrdenacaoChange = onOrdenacaoChange
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAdicionarClick,
                containerColor = Navy,
                contentColor = Color.White,
                shape = CardShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar matéria")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Indicador de progresso linear durante a exclusão
            if (exclusaoUiState is ExclusaoMateriaUiState.Excluindo) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            when (listaUiState) {
                is ListaMateriasUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ListaMateriasUiState.Sucesso -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp)
                    ) {
                        item { CabecalhoPagina("Matérias", "${listaUiState.materias.size} cadastrada(s)") }
                        if (listaUiState.materias.isEmpty()) {
                            item { EstadoVazio("Nenhuma matéria", "Use o botão + para cadastrar a primeira.") }
                        }
                        items(listaUiState.materias, key = { it.materia.id }) { detalhes ->
                            MateriaCard(
                                detalhes = detalhes,
                                onEditarClick = { onEditarClick(detalhes.materia) },
                                onExcluirClick = { onExcluirClick(detalhes.materia) }
                            )
                        }
                    }
                }

                is ListaMateriasUiState.Erro -> {
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
fun ListaMateriasTopAppBar(
    ordenacaoAtual: OrdenacaoMateria,
    onOrdenacaoChange: (OrdenacaoMateria) -> Unit
) {
    CenterAlignedTopAppBar(
        title = { LogoStudyFlow() },
        actions = {
            var menuAberto by remember { mutableStateOf(false) }

            IconButton(onClick = { menuAberto = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "Ordenar matérias"
                )
            }

            DropdownMenu(
                expanded = menuAberto,
                onDismissRequest = { menuAberto = false }
            ) {
                OrdenacaoMateria.entries.forEach { opcao ->
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
fun ConfirmacaoExclusaoMateriaDialog(
    materia: Materia,
    onConfirmar: () -> Unit,
    onDispensar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDispensar,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Navy
            )
        },
        title = {
            Text(text = "Excluir ${materia.nome}?", color = Ink)
        },
        text = {
            Text(text = "As avaliações vinculadas a esta matéria também serão excluídas.", color = Muted)
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

@Composable
fun MateriaCard(
    detalhes: MateriaDetalhes,
    onEditarClick: () -> Unit,
    onExcluirClick: () -> Unit
) {
    val materia = detalhes.materia
    StudyCard {
        ListItem(
            leadingContent = { Icon(Icons.Default.Book, contentDescription = null, tint = Muted) },
            headlineContent = {
                Text(materia.nome, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            },
            supportingContent = {
                Text(
                    listOfNotNull(
                        materia.professor.takeIf { it.isNotBlank() },
                        "${detalhes.avaliacoes.size} avaliação(ões)"
                    ).joinToString(" • "),
                    color = Muted, fontSize = 14.sp
                )
            },
            trailingContent = {
                Row {
                    IconButton(onClick = onEditarClick) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar matéria", tint = Ink)
                    }
                    IconButton(onClick = onExcluirClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir matéria", tint = Ink)
                    }
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}

@Preview(name = "Matérias", showSystemUi = true)
@Composable
private fun ListaMateriasScreenPreview() {
    StudyFlowTheme {
        ListaMateriasScreen(
            listaUiState = ListaMateriasUiState.Sucesso(DadosExemplo.materiasDetalhes),
            exclusaoUiState = ExclusaoMateriaUiState.Ocioso,
            ordenacaoAtual = OrdenacaoMateria.NOME_ASC,
            onOrdenacaoChange = {},
            onAdicionarClick = {},
            onEditarClick = {},
            materiaParaExcluir = null,
            onExcluirClick = {},
            onConfirmarExclusao = {},
            onCancelarExclusao = {},
            onTentarNovamente = {},
            onExclusaoMensagemMostrada = {}
        )
    }
}

@Preview(name = "Diálogo – Excluir matéria")
@Composable
private fun ConfirmacaoExclusaoMateriaDialogPreview() {
    StudyFlowTheme {
        ConfirmacaoExclusaoMateriaDialog(DadosExemplo.materias[2], onConfirmar = {}, onDispensar = {})
    }
}
