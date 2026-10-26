package br.com.studyflow.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.studyflow.model.Avaliacao
import br.com.studyflow.ui.components.AvaliacaoCard
import br.com.studyflow.ui.components.CabecalhoPagina
import br.com.studyflow.ui.components.CardResumo
import br.com.studyflow.ui.components.EstadoVazio
import br.com.studyflow.ui.components.LogoStudyFlow
import br.com.studyflow.ui.preview.DadosExemplo
import br.com.studyflow.ui.state.PainelUiState
import br.com.studyflow.ui.theme.Background
import br.com.studyflow.ui.theme.DoneColor
import br.com.studyflow.ui.theme.Ink
import br.com.studyflow.ui.theme.LateColor
import br.com.studyflow.ui.theme.PendingColor
import br.com.studyflow.ui.theme.StudyFlowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PainelScreen(
    painelUiState: PainelUiState,
    onConcluirClick: (Avaliacao) -> Unit,
    onEditarClick: (Avaliacao) -> Unit,
    onTentarNovamente: () -> Unit
) {
    Scaffold(
        containerColor = Background,
        topBar = {
            CenterAlignedTopAppBar(
                title = { LogoStudyFlow() },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (painelUiState) {
                is PainelUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PainelUiState.Sucesso -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        item { CabecalhoPagina("Visão geral", "Organize o que precisa de atenção primeiro.") }
                        item {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                CardResumo("Pendentes", painelUiState.pendentes.toString(), Modifier.weight(1f), PendingColor)
                                CardResumo("Concluídas", painelUiState.concluidas.toString(), Modifier.weight(1f), DoneColor)
                                CardResumo("Atrasadas", painelUiState.atrasadas.toString(), Modifier.weight(1f), LateColor)
                            }
                        }
                        item {
                            Text("Prioridade de estudos", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                            Text("Cálculo: 60% urgência + 40% impacto", color = Ink, fontSize = 12.sp)
                        }
                        if (painelUiState.prioridades.isEmpty()) {
                            item { EstadoVazio("Tudo em dia!", "Cadastre uma avaliação para começar.") }
                        }
                        items(painelUiState.prioridades, key = { it.avaliacao.id }) { detalhes ->
                            AvaliacaoCard(
                                detalhes = detalhes,
                                onConcluirClick = { onConcluirClick(detalhes.avaliacao) },
                                onEditarClick = { onEditarClick(detalhes.avaliacao) }
                            )
                        }
                    }
                }

                is PainelUiState.Erro -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = painelUiState.mensagem,
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

@Preview(name = "Painel", showSystemUi = true)
@Composable
private fun PainelScreenPreview() {
    StudyFlowTheme {
        PainelScreen(
            painelUiState = PainelUiState.Sucesso(
                prioridades = DadosExemplo.avaliacoesDetalhes.filter { !it.avaliacao.concluida },
                pendentes = 3,
                concluidas = 1,
                atrasadas = 0
            ),
            onConcluirClick = {},
            onEditarClick = {},
            onTentarNovamente = {}
        )
    }
}

@Preview(name = "Painel – vazio", showSystemUi = true)
@Composable
private fun PainelScreenVazioPreview() {
    StudyFlowTheme {
        PainelScreen(
            painelUiState = PainelUiState.Sucesso(emptyList(), 0, 0, 0),
            onConcluirClick = {},
            onEditarClick = {},
            onTentarNovamente = {}
        )
    }
}
