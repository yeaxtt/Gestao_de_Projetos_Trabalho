package br.com.studyflow.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.studyflow.Priority
import br.com.studyflow.R
import br.com.studyflow.data.model.Avaliacao
import br.com.studyflow.data.model.Materia
import br.com.studyflow.ui.preview.SampleData
import br.com.studyflow.ui.theme.*
import br.com.studyflow.ui.util.formatDate
import br.com.studyflow.ui.util.formatWeight

@Composable
fun BrandBar() {
    Box(Modifier.fillMaxWidth().background(Navy).statusBarsPadding().height(64.dp), contentAlignment = Alignment.Center) {
        Image(painterResource(R.drawable.logo_studyflow), contentDescription = "StudyFlow", modifier = Modifier.height(40.dp))
    }
}

@Composable
fun PageHeader(title: String, subtitle: String) {
    Column {
        Text(title, color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = Muted, fontSize = 14.sp)
    }
}

@Composable
fun StudyCard(modifier: Modifier = Modifier, color: Color = CardColor, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier.fillMaxWidth(), shape = CardShape, color = color, shadowElevation = 2.dp) {
        Column(content = content)
    }
}

@Composable
fun SegmentChip(label: String, selected: Boolean, modifier: Modifier = Modifier, horizontalPadding: Dp = 16.dp, onClick: () -> Unit) {
    val base = modifier.clip(ChipShape)
    Box(
        (if (selected) base.background(Navy, ChipShape) else base.border(1.dp, OutlineColor, ChipShape))
            .clickable(onClick = onClick).padding(horizontal = horizontalPadding, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = if (selected) OnNavy else Muted, fontSize = 14.sp, maxLines = 1)
    }
}

@Composable
fun SummaryCard(label: String, value: String, modifier: Modifier = Modifier, color: Color = CardColor) {
    StudyCard(modifier, color) {
        Column(Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(label, color = Ink, fontSize = 11.sp)
        }
    }
}

@Composable
fun MateriaCard(materia: Materia, totalAvaliacoes: Int, onEdit: () -> Unit, onDelete: () -> Unit) {
    StudyCard {
        ListItem(
            leadingContent = { Icon(Icons.Default.Book, null, tint = Muted) },
            headlineContent = { Text(materia.nome, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold) },
            supportingContent = {
                Text(listOfNotNull(materia.professor.takeIf { it.isNotBlank() }, "$totalAvaliacoes avaliação(ões)").joinToString(" • "), color = Muted, fontSize = 14.sp)
            },
            trailingContent = {
                Row {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Editar matéria", tint = Ink) }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Excluir matéria", tint = Ink) }
                }
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}

@Composable
fun EvaluationCard(
    evaluation: Avaliacao, materiaNome: String?, showDelete: Boolean,
    onToggle: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit = {}
) {
    val score = Priority.score(evaluation)
    val priorityLabel = when { score >= 80 -> "Alta"; score >= 55 -> "Média"; else -> "Baixa" }
    StudyCard {
        Column(Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(evaluation.titulo, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text("${materiaNome ?: "Sem matéria"} • ${evaluation.tipo}", color = Muted, fontSize = 14.sp)
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    "$priorityLabel · $score", color = OutlineColor, fontSize = 13.sp,
                    modifier = Modifier.border(1.dp, OutlineSoft, ChipShape).padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
            Text("Prazo: ${formatDate(evaluation.dataMillis)}  •  Peso: ${formatWeight(evaluation.peso)}", color = Ink, fontSize = 14.sp)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, "Editar avaliação", tint = Ink) }
                if (showDelete) IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Excluir avaliação", tint = Ink) }
                TextButton(onClick = onToggle) {
                    Icon(if (evaluation.concluida) Icons.Default.Refresh else Icons.Default.CheckCircle, null, Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp)); Text(if (evaluation.concluida) "Reabrir" else "Concluir")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaDropdown(materias: List<Materia>, selected: Long?, allLabel: String? = null, onSelected: (Long?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded, { expanded = it }, Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = materias.firstOrNull { it.id == selected }?.nome ?: allLabel ?: "", onValueChange = {}, readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }, shape = FieldShape,
            modifier = Modifier.menuAnchor().fillMaxWidth(), label = { Text("Matéria") }
        )
        ExposedDropdownMenu(expanded, { expanded = false }) {
            if (allLabel != null) DropdownMenuItem({ Text(allLabel) }, onClick = { onSelected(null); expanded = false })
            materias.forEach { m -> DropdownMenuItem({ Text(m.nome) }, onClick = { onSelected(m.id); expanded = false }) }
        }
    }
}

@Composable
fun EmptyState(title: String, description: String) {
    StudyCard {
        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.EventNote, null, tint = Primary)
            Spacer(Modifier.height(8.dp)); Text(title, color = Ink, fontWeight = FontWeight.SemiBold)
            Text(description, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
fun navyButtonColors() = ButtonDefaults.buttonColors(containerColor = Navy, contentColor = Color.White)

@Preview(name = "Componentes", showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
@Composable
private fun ComponentsPreview() {
    StudyFlowTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Componentes", color = Ink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            SummaryCard("Pendentes", "0", Modifier.width(100.dp))
            EvaluationCard(SampleData.avaliacoes[0], "Cálculo II", showDelete = true, onToggle = {}, onEdit = {})
            EvaluationCard(SampleData.avaliacoes[0].copy(concluida = true), "Cálculo II", showDelete = true, onToggle = {}, onEdit = {})
            MateriaCard(SampleData.materias[2], 1, onEdit = {}, onDelete = {})
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SegmentChip("Todas", selected = true) {}
                SegmentChip("Todas", selected = false) {}
            }
            EmptyState("Tudo em dia!", "Cadastre uma avaliação para começar.")
        }
    }
}
