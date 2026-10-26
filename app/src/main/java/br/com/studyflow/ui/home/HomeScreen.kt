package br.com.studyflow.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.studyflow.data.model.Avaliacao
import br.com.studyflow.ui.Destination
import br.com.studyflow.ui.StudyFlowScaffold
import br.com.studyflow.ui.StudyUiState
import br.com.studyflow.ui.avaliacoes.AvaliacaoDialog
import br.com.studyflow.ui.components.*
import br.com.studyflow.ui.preview.SampleData
import br.com.studyflow.ui.theme.*

@Composable
fun HomeScreen(state: StudyUiState, onToggle: (Avaliacao) -> Unit, onUpdate: (Avaliacao) -> Unit, modifier: Modifier = Modifier) {
    var editTarget by remember { mutableStateOf<Avaliacao?>(null) }
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { PageHeader("Visão geral", "Organize o que precisa de atenção primeiro.") }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SummaryCard("Pendentes", state.prioridades.size.toString(), Modifier.weight(1f), PendingColor)
                SummaryCard("Concluídas", state.concluidas.toString(), Modifier.weight(1f), DoneColor)
                SummaryCard("Atrasadas", state.atrasadas.toString(), Modifier.weight(1f), LateColor)
            }
        }
        item {
            Text("Prioridade de estudos", color = Ink, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Cálculo: 60% urgência + 40% impacto", color = Ink, fontSize = 12.sp)
        }
        if (state.prioridades.isEmpty()) item { EmptyState("Tudo em dia!", "Cadastre uma avaliação para começar.") }
        items(state.prioridades, key = { it.id }) { evaluation ->
            EvaluationCard(
                evaluation, state.materia(evaluation.materiaId)?.nome, showDelete = false,
                onToggle = { onToggle(evaluation) }, onEdit = { editTarget = evaluation }
            )
        }
    }
    editTarget?.let { avaliacao ->
        AvaliacaoDialog(state.materias, avaliacao, onDismiss = { editTarget = null }) { onUpdate(it); editTarget = null }
    }
}

@Preview(name = "Início", showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    StudyFlowTheme {
        StudyFlowScaffold(Destination.HOME.route, onNavigate = {}) { HomeScreen(SampleData.state, onToggle = {}, onUpdate = {}, modifier = it) }
    }
}

@Preview(name = "Início – vazio", showSystemUi = true)
@Composable
private fun HomeScreenEmptyPreview() {
    StudyFlowTheme {
        StudyFlowScaffold(Destination.HOME.route, onNavigate = {}) { HomeScreen(StudyUiState(), onToggle = {}, onUpdate = {}, modifier = it) }
    }
}
