package br.com.studyflow.ui.materias

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.studyflow.data.model.Materia
import br.com.studyflow.ui.Destination
import br.com.studyflow.ui.StudyFlowScaffold
import br.com.studyflow.ui.StudyUiState
import br.com.studyflow.ui.components.EmptyState
import br.com.studyflow.ui.components.MateriaCard
import br.com.studyflow.ui.components.PageHeader
import br.com.studyflow.ui.preview.SampleData
import br.com.studyflow.ui.theme.CardShape
import br.com.studyflow.ui.theme.Navy
import br.com.studyflow.ui.theme.StudyFlowTheme

@Composable
fun MateriasScreen(
    state: StudyUiState,
    onAdd: (nome: String, professor: String) -> Unit,
    onUpdate: (Materia) -> Unit,
    onDelete: (materiaId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showForm by remember { mutableStateOf(false) }
    var editTarget by remember { mutableStateOf<Materia?>(null) }
    var deleteTarget by remember { mutableStateOf<Materia?>(null) }
    Box(modifier.fillMaxSize()) {
        LazyColumn(contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            item { PageHeader("Matérias", "${state.materias.size} cadastrada(s)") }
            if (state.materias.isEmpty()) item { EmptyState("Nenhuma matéria", "Use o botão + para cadastrar a primeira.") }
            items(state.materias, key = { it.id }) { materia ->
                MateriaCard(materia, state.totalAvaliacoes(materia.id), onEdit = { editTarget = materia }, onDelete = { deleteTarget = materia })
            }
        }
        FloatingActionButton(
            onClick = { showForm = true }, modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
            containerColor = Navy, contentColor = Color.White, shape = CardShape
        ) {
            Icon(Icons.Default.Add, "Adicionar matéria")
        }
    }
    if (showForm) MateriaDialog(onDismiss = { showForm = false }) { nome, professor -> onAdd(nome, professor); showForm = false }
    editTarget?.let { materia ->
        MateriaDialog(materia, onDismiss = { editTarget = null }) { nome, professor ->
            onUpdate(materia.copy(nome = nome, professor = professor)); editTarget = null
        }
    }
    deleteTarget?.let { materia ->
        ExcluirMateriaDialog(materia.nome, onDismiss = { deleteTarget = null }) { onDelete(materia.id); deleteTarget = null }
    }
}

@Preview(name = "Matérias", showSystemUi = true)
@Composable
private fun MateriasScreenPreview() {
    StudyFlowTheme {
        StudyFlowScaffold(Destination.MATERIAS.route, onNavigate = {}) {
            MateriasScreen(SampleData.state, onAdd = { _, _ -> }, onUpdate = {}, onDelete = {}, modifier = it)
        }
    }
}
