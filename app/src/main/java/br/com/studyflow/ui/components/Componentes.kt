package br.com.studyflow.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import br.com.studyflow.R
import br.com.studyflow.model.AvaliacaoDetalhes
import br.com.studyflow.model.Materia
import br.com.studyflow.model.Prioridade
import br.com.studyflow.ui.preview.DadosExemplo
import br.com.studyflow.ui.theme.CardColor
import br.com.studyflow.ui.theme.CardShape
import br.com.studyflow.ui.theme.ChipShape
import br.com.studyflow.ui.theme.FieldShape
import br.com.studyflow.ui.theme.Ink
import br.com.studyflow.ui.util.descreverPrazo
import br.com.studyflow.ui.util.diasAtePrazo
import br.com.studyflow.ui.theme.Indicator
import br.com.studyflow.ui.theme.PendingColor
import br.com.studyflow.ui.theme.LateColor
import br.com.studyflow.ui.theme.DoneColor
import br.com.studyflow.ui.theme.Muted
import br.com.studyflow.ui.theme.Navy
import br.com.studyflow.ui.theme.OnNavy
import br.com.studyflow.ui.theme.OutlineColor
import br.com.studyflow.ui.theme.Primary
import br.com.studyflow.ui.theme.StudyFlowTheme
import br.com.studyflow.ui.util.formatarData
import br.com.studyflow.ui.util.formatarPeso

// Componentes visuais do protótipo no Figma, reaproveitados pelas telas.

@Composable
fun LogoStudyFlow() {
    Image(
        painter = painterResource(R.drawable.logo_studyflow),
        contentDescription = "StudyFlow",
        modifier = Modifier.height(36.dp)
    )
}

@Composable
fun CabecalhoPagina(titulo: String, subtitulo: String) {
    Column {
        Text(titulo, color = Ink, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(subtitulo, color = Muted, fontSize = 14.sp)
    }
}

@Composable
fun StudyCard(
    modifier: Modifier = Modifier,
    color: Color = CardColor,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(modifier.fillMaxWidth(), shape = CardShape, color = color, shadowElevation = 2.dp) {
        Column(content = content)
    }
}

@Composable
fun ChipSegmento(
    texto: String,
    selecionado: Boolean,
    modifier: Modifier = Modifier,
    paddingHorizontal: Dp = 16.dp,
    onClick: () -> Unit
) {
    val base = modifier.clip(ChipShape)
    Box(
        (if (selecionado) base.background(Navy, ChipShape) else base.border(1.dp, OutlineColor, ChipShape))
            .clickable(onClick = onClick)
            .padding(horizontal = paddingHorizontal, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(texto, color = if (selecionado) OnNavy else Muted, fontSize = 14.sp, maxLines = 1)
    }
}

@Composable
fun CardResumo(rotulo: String, valor: String, modifier: Modifier = Modifier, cor: Color = CardColor) {
    StudyCard(modifier, cor) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, color = Ink, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(rotulo, color = Ink, fontSize = 11.sp)
        }
    }
}

@Composable
fun AvaliacaoCard(
    detalhes: AvaliacaoDetalhes,
    onConcluirClick: () -> Unit,
    onEditarClick: () -> Unit,
    onExcluirClick: (() -> Unit)? = null
) {
    val avaliacao = detalhes.avaliacao
    val pontuacao = Prioridade.calcular(avaliacao)
    StudyCard {
        Column(
            Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(avaliacao.titulo, color = Ink, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        "${detalhes.materia?.nome ?: "Sem matéria"} • ${avaliacao.tipo.titulo}",
                        color = Muted, fontSize = 14.sp
                    )
                }
                Spacer(Modifier.width(8.dp))
                // Selo com cor por nível: Alta em vermelho claro, Média em amarelo, Baixa em azul
                val rotulo = if (avaliacao.concluida) "Concluída" else Prioridade.rotulo(pontuacao)
                val corSelo = when {
                    avaliacao.concluida -> DoneColor
                    rotulo == "Alta" -> LateColor
                    rotulo == "Média" -> PendingColor
                    else -> Indicator
                }
                Text(
                    if (avaliacao.concluida) rotulo else "$rotulo · $pontuacao",
                    color = Ink, fontSize = 13.sp, fontWeight = FontWeight.Medium,
                    modifier = Modifier.background(corSelo, ChipShape).padding(horizontal = 10.dp, vertical = 5.dp)
                )
            }
            Text(
                "Prazo: ${formatarData(avaliacao.dataMillis)}  •  Peso: ${formatarPeso(avaliacao.peso)}",
                color = Ink, fontSize = 14.sp
            )
            if (!avaliacao.concluida) {
                val dias = diasAtePrazo(avaliacao.dataMillis)
                Text(
                    descreverPrazo(dias),
                    color = if (dias < 0) MaterialTheme.colorScheme.error else Ink,
                    fontSize = 13.sp,
                    fontWeight = if (dias <= 3) FontWeight.SemiBold else FontWeight.Normal
                )
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditarClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar avaliação", tint = Ink)
                }
                if (onExcluirClick != null) {
                    IconButton(onClick = onExcluirClick) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir avaliação", tint = Ink)
                    }
                }
                TextButton(onClick = onConcluirClick) {
                    Icon(
                        if (avaliacao.concluida) Icons.Default.Refresh else Icons.Default.CheckCircle,
                        contentDescription = null, modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(if (avaliacao.concluida) "Reabrir" else "Concluir")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriaDropdown(
    materias: List<Materia>,
    selecionadaId: Int?,
    rotuloTodas: String? = null,
    enabled: Boolean = true,
    onSelecionar: (Int?) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { if (enabled) expandido = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = materias.firstOrNull { it.id == selecionadaId }?.nome ?: rotuloTodas ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text("Matéria") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandido) },
            shape = FieldShape,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            if (rotuloTodas != null) {
                DropdownMenuItem(
                    text = { Text(rotuloTodas) },
                    onClick = { onSelecionar(null); expandido = false }
                )
            }
            materias.forEach { materia ->
                DropdownMenuItem(
                    text = { Text(materia.nome) },
                    onClick = { onSelecionar(materia.id); expandido = false }
                )
            }
        }
    }
}

// AlertDialog usado pelos dois formulários ao sair com alterações não salvas
@Composable
fun DescartarAlteracoesDialog(
    onConfirmar: () -> Unit,
    onDispensar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDispensar,
        icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = Navy) },
        title = { Text("Descartar alterações?", color = Ink) },
        text = { Text("Os dados preenchidos neste formulário ainda não foram salvos e serão perdidos.", color = Muted) },
        containerColor = CardColor,
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Descartar", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDispensar) { Text("Continuar editando") }
        }
    )
}

@Composable
fun EstadoVazio(titulo: String, descricao: String) {
    StudyCard {
        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.AutoMirrored.Filled.EventNote, contentDescription = null, tint = Primary)
            Spacer(Modifier.height(8.dp))
            Text(titulo, color = Ink, fontWeight = FontWeight.SemiBold)
            Text(descricao, color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
fun navyButtonColors() = ButtonDefaults.buttonColors(containerColor = Navy, contentColor = Color.White)

@Preview(name = "Componentes", showBackground = true, backgroundColor = 0xFFFFFFFF, widthDp = 360)
@Composable
private fun ComponentesPreview() {
    StudyFlowTheme {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            CardResumo("Pendentes", "3", Modifier.width(100.dp))
            AvaliacaoCard(DadosExemplo.avaliacoesDetalhes[0], onConcluirClick = {}, onEditarClick = {}, onExcluirClick = {})
            AvaliacaoCard(DadosExemplo.avaliacoesDetalhes[3], onConcluirClick = {}, onEditarClick = {})
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ChipSegmento("Todas", selecionado = true) {}
                ChipSegmento("Pendentes", selecionado = false) {}
            }
            EstadoVazio("Tudo em dia!", "Cadastre uma avaliação para começar.")
        }
    }
}
