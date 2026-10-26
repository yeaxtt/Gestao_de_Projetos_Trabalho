package br.com.studyflow.ui.materias

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.studyflow.data.model.Materia
import br.com.studyflow.ui.components.navyButtonColors
import br.com.studyflow.ui.preview.SampleData
import br.com.studyflow.ui.theme.*

/** Cadastro de matéria; com [materia] preenchida, abre em modo de edição. */
@Composable
fun MateriaDialog(materia: Materia? = null, onDismiss: () -> Unit, onSave: (nome: String, professor: String) -> Unit) {
    var name by remember(materia) { mutableStateOf(materia?.nome.orEmpty()) }
    var professor by remember(materia) { mutableStateOf(materia?.professor.orEmpty()) }
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text(if (materia == null) "Nova matéria" else "Editar matéria", color = Ink) }, containerColor = CardColor,
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(name, { name = it }, placeholder = { Text("Nome *") }, singleLine = true, shape = FieldShape, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(professor, { professor = it }, placeholder = { Text("Professor (opcional)") }, singleLine = true, shape = FieldShape, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = { Button(onClick = { onSave(name.trim(), professor.trim()) }, enabled = name.isNotBlank(), colors = navyButtonColors()) { Text("Salvar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun ExcluirMateriaDialog(nome: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss, icon = { Icon(Icons.Default.Warning, null, tint = Navy) },
        title = { Text("Excluir $nome?", color = Ink) },
        text = { Text("As avaliações vinculadas a esta matéria também serão excluídas.", color = Muted) },
        containerColor = CardColor,
        confirmButton = { TextButton(onClick = onConfirm) { Text("Excluir") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Preview(name = "Diálogo – Nova matéria")
@Composable
private fun NovaMateriaDialogPreview() {
    StudyFlowTheme { MateriaDialog(onDismiss = {}, onSave = { _, _ -> }) }
}

@Preview(name = "Diálogo – Editar matéria")
@Composable
private fun EditarMateriaDialogPreview() {
    StudyFlowTheme { MateriaDialog(SampleData.materias[2], onDismiss = {}, onSave = { _, _ -> }) }
}

@Preview(name = "Diálogo – Excluir matéria")
@Composable
private fun ExcluirMateriaDialogPreview() {
    StudyFlowTheme { ExcluirMateriaDialog("Cálculo II", onDismiss = {}, onConfirm = {}) }
}
