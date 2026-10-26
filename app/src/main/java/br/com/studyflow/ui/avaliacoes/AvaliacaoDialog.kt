    package br.com.studyflow.ui.avaliacoes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.studyflow.data.model.Avaliacao
import br.com.studyflow.data.model.Materia
import br.com.studyflow.ui.components.MateriaDropdown
import br.com.studyflow.ui.components.SegmentChip
import br.com.studyflow.ui.components.navyButtonColors
import br.com.studyflow.ui.preview.SampleData
import br.com.studyflow.ui.theme.*
import br.com.studyflow.ui.util.formatDate
import br.com.studyflow.ui.util.formatWeight
import br.com.studyflow.ui.util.utcDateToLocalMidnight

private val TIPOS = listOf("Prova", "Trabalho", "Seminário")

/** Cadastro de avaliação; com [avaliacao] preenchida, abre em modo de edição. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvaliacaoDialog(materias: List<Materia>, avaliacao: Avaliacao? = null, onDismiss: () -> Unit, onSave: (Avaliacao) -> Unit) {
    var title by remember(avaliacao) { mutableStateOf(avaliacao?.titulo.orEmpty()) }
    var type by remember(avaliacao) { mutableStateOf(avaliacao?.tipo ?: TIPOS.first()) }
    var weight by remember(avaliacao) { mutableStateOf(avaliacao?.peso?.let(::formatWeight) ?: "5") }
    var materiaId by remember(avaliacao) { mutableStateOf(avaliacao?.materiaId ?: materias.firstOrNull()?.id) }
    var showDatePicker by remember { mutableStateOf(false) }
    var dateMillis by remember(avaliacao) { mutableLongStateOf(avaliacao?.dataMillis ?: (System.currentTimeMillis() + 3 * 86_400_000L)) }
    val weightValue = weight.replace(',', '.').toDoubleOrNull()
    val valid = title.isNotBlank() && materiaId != null && weightValue != null && weightValue in 0.0..5.0
    AlertDialog(
        onDismissRequest = onDismiss, title = { Text(if (avaliacao == null) "Nova avaliação" else "Editar avaliação", color = Ink) }, containerColor = CardColor,
        text = {
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (materias.isEmpty()) Text("Cadastre uma matéria antes de criar avaliações.", color = MaterialTheme.colorScheme.error)
                OutlinedTextField(title, { title = it }, placeholder = { Text("Título *") }, singleLine = true, shape = FieldShape, modifier = Modifier.fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    TIPOS.forEach { option -> SegmentChip(option, type == option, Modifier.weight(1f), horizontalPadding = 2.dp) { type = option } }
                }
                MateriaDropdown(materias, materiaId) { materiaId = it }
                OutlinedTextField(
                    weight, { weight = it }, label = { Text("Peso de 0 a 5 *") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true, shape = FieldShape, isError = weight.isNotBlank() && (weightValue == null || weightValue !in 0.0..5.0),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedButton(onClick = { showDatePicker = true }, Modifier.fillMaxWidth(), border = BorderStroke(1.dp, OutlineColor)) {
                    Icon(Icons.Default.DateRange, null, Modifier.size(18.dp)); Spacer(Modifier.width(8.dp)); Text("Prazo: ${formatDate(dateMillis)}")
                }
                Text("Lembretes serão programados para 7, 3 e 1 dia antes.", color = Ink, fontSize = 12.sp)
            }
        },
        confirmButton = {
            Button(onClick = {
                val base = avaliacao ?: Avaliacao(titulo = "", tipo = type, materiaId = 0, dataMillis = 0, peso = 0.0)
                onSave(base.copy(titulo = title.trim(), tipo = type, materiaId = materiaId!!, dataMillis = dateMillis, peso = weightValue!!))
            }, enabled = valid, colors = navyButtonColors()) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
    if (showDatePicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = dateMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = { TextButton(onClick = { state.selectedDateMillis?.let { dateMillis = utcDateToLocalMidnight(it) }; showDatePicker = false }) { Text("Confirmar") } },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") } }
        ) { DatePicker(state = state) }
    }
}

@Preview(name = "Diálogo – Nova avaliação")
@Composable
private fun NovaAvaliacaoDialogPreview() {
    StudyFlowTheme { AvaliacaoDialog(SampleData.materias, onDismiss = {}, onSave = {}) }
}

@Preview(name = "Diálogo – Editar avaliação")
@Composable
private fun EditarAvaliacaoDialogPreview() {
    StudyFlowTheme { AvaliacaoDialog(SampleData.materias, SampleData.avaliacoes[0], onDismiss = {}, onSave = {}) }
}
