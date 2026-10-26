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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.studyflow.model.Feriado
import br.com.studyflow.ui.components.CabecalhoPagina
import br.com.studyflow.ui.components.ChipSegmento
import br.com.studyflow.ui.components.EstadoVazio
import br.com.studyflow.ui.components.LogoStudyFlow
import br.com.studyflow.ui.components.StudyCard
import br.com.studyflow.ui.state.FeriadosUiState
import br.com.studyflow.ui.theme.Background
import br.com.studyflow.ui.theme.Indicator
import br.com.studyflow.ui.theme.Ink
import br.com.studyflow.ui.theme.Muted
import br.com.studyflow.ui.theme.StudyFlowTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeriadosScreen(
    feriadosUiState: FeriadosUiState,
    anoAtual: Int,
    anoSelecionado: Int,
    onAnoChange: (Int) -> Unit,
    onTentarNovamente: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Erro de rede também aparece em Snackbar, com a ação de tentar de novo
    LaunchedEffect(feriadosUiState) {
        if (feriadosUiState is FeriadosUiState.Erro) {
            val resultado = snackbarHostState.showSnackbar(
                message = "Falha ao consultar a BrasilAPI",
                actionLabel = "Tentar novamente"
            )
            if (resultado == SnackbarResult.ActionPerformed) onTentarNovamente()
        }
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)) {
                CabecalhoPagina("Feriados nacionais", "Planeje as entregas considerando os dias sem aula.")
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(anoAtual, anoAtual + 1).forEach { ano ->
                        ChipSegmento(ano.toString(), ano == anoSelecionado) { onAnoChange(ano) }
                    }
                }
            }

            when (feriadosUiState) {
                is FeriadosUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is FeriadosUiState.Sucesso -> {
                    val hoje = LocalDate.now()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        if (feriadosUiState.proximos.isEmpty() && feriadosUiState.passados.isEmpty()) {
                            item { EstadoVazio("Nenhum feriado", "A BrasilAPI não retornou feriados para este ano.") }
                        }
                        if (feriadosUiState.proximos.isNotEmpty()) {
                            item { TituloSecao("Próximos") }
                        }
                        items(feriadosUiState.proximos, key = { it.data + it.nome }) { feriado ->
                            FeriadoCard(feriado = feriado, hoje = hoje)
                        }
                        if (feriadosUiState.passados.isNotEmpty()) {
                            item { TituloSecao("Já passaram") }
                        }
                        items(feriadosUiState.passados, key = { it.data + it.nome }) { feriado ->
                            FeriadoCard(feriado = feriado, hoje = hoje)
                        }
                    }
                }

                is FeriadosUiState.Erro -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CloudOff, contentDescription = null, tint = Muted)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = feriadosUiState.mensagem,
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

@Composable
private fun TituloSecao(texto: String) {
    Text(texto, color = Ink, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun FeriadoCard(feriado: Feriado, hoje: LocalDate) {
    val data = LocalDate.parse(feriado.data)
    val dias = ChronoUnit.DAYS.between(hoje, data)
    val passou = dias < 0
    val formato = DateTimeFormatter.ofPattern("dd/MM · EEEE", Locale.forLanguageTag("pt-BR"))

    StudyCard(color = if (!passou && dias <= 30) Indicator else MaterialTheme.colorScheme.surfaceContainerLow) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    feriado.nome,
                    color = if (passou) Muted else Ink,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(data.format(formato), color = Muted, fontSize = 14.sp)
            }
            Spacer(Modifier.width(8.dp))
            Text(
                when {
                    passou -> "Já passou"
                    dias == 0L -> "Hoje"
                    dias == 1L -> "Amanhã"
                    else -> "Em $dias dias"
                },
                color = if (passou) Muted else Ink,
                fontSize = 13.sp
            )
        }
    }
}

@Preview(name = "Feriados", showSystemUi = true)
@Composable
private fun FeriadosScreenPreview() {
    StudyFlowTheme {
        FeriadosScreen(
            feriadosUiState = FeriadosUiState.Sucesso(
                proximos = listOf(
                    Feriado("2026-11-02", "Finados", "national"),
                    Feriado("2026-11-15", "Proclamação da República", "national"),
                    Feriado("2026-12-25", "Natal", "national")
                ),
                passados = listOf(Feriado("2026-10-12", "Nossa Senhora Aparecida", "national"))
            ),
            anoAtual = 2026,
            anoSelecionado = 2026,
            onAnoChange = {},
            onTentarNovamente = {}
        )
    }
}

@Preview(name = "Feriados – erro", showSystemUi = true)
@Composable
private fun FeriadosScreenErroPreview() {
    StudyFlowTheme {
        FeriadosScreen(
            feriadosUiState = FeriadosUiState.Erro("Não foi possível buscar os feriados. Verifique a conexão com a internet."),
            anoAtual = 2026,
            anoSelecionado = 2026,
            onAnoChange = {},
            onTentarNovamente = {}
        )
    }
}
