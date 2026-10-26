package br.com.studyflow.ui.avaliacoes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.studyflow.data.model.Avaliacao
import br.com.studyflow.ui.*
import br.com.studyflow.ui.components.*
import br.com.studyflow.ui.preview.SampleData
import br.com.studyflow.ui.theme.CardShape
import br.com.studyflow.ui.theme.Navy
import br.com.studyflow.ui.theme.StudyFlowTheme

@Composable
fun AvaliacoesScreen(
    state: StudyUiState,
    onStatusChange: (StatusFilter) -> Unit,
    onMateriaFilterChange: (Long?) -> Unit,
    onToggle: (Avaliacao) -> Unit,
    onDelete: (Avaliacao) -> Unit,
    onAdd: (Avaliacao) -> Unit,
    onUpdate: (Avaliacao) -> Unit,
    modifier: Modifier = Modifier
) {
    var showForm by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Avaliacao?>(null) }
    Box(modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item {
                PageHeader("Avaliações", "Filtre e acompanhe todos os prazos.")
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusFilter.entries.forEach { item -> SegmentChip(item.label, state.statusFilter == item) { onStatusChange(item) } }
                }
                Spacer(Modifier.height(8.dp))
                MateriaDropdown(state.materias, state.materiaFilter, allLabel = "Todas as matérias", onSelected = onMateriaFilterChange)
            }
            if (state.avaliacoesFiltradas.isEmpty()) item { EmptyState("Nenhum resultado", "Altere os filtros ou adicione uma avaliação.") }
            items(state.avaliacoesFiltradas, key = { it.id }) { evaluation ->
                EvaluationCard(
                    evaluation, state.materia(evaluation.materiaId)?.nome, showDelete = true,
                    onToggle = { onToggle(evaluation) }, onEdit = { editTarget = evaluation }, onDelete = { onDelete(evaluation) }
                )
            }
        }
        ExtendedFloatingActionButton(
            onClick = { showForm = true }, icon = { Icon(Icons.Default.Add, null) }, text = { Text("Nova avaliação") },
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp), containerColor = Navy, contentColor = Color.White, shape = CardShape
        )
    }
    if (showForm) AvaliacaoDialog(state.materias, onDismiss = { showForm = false }) { onAdd(it); showForm = false }
    editTarget?.let { avaliacao ->
        AvaliacaoDialog(state.materias, avaliacao, onDismiss = { editTarget = null }) { onUpdate(it); editTarget = null }
    }
}

@Preview(name = "Avaliações", showSystemUi = true)
@Composable
private fun AvaliacoesScreenPreview() {
    StudyFlowTheme {
        StudyFlowScaffold(Destination.AVALIACOES.route, onNavigate = {}) {
            AvaliacoesScreen(SampleData.state, {}, {}, {}, {}, {}, {}, modifier = it)
        }
    }
}
