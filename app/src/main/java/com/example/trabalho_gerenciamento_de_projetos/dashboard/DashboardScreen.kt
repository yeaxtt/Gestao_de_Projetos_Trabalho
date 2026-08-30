package com.example.trabalho_gerenciamento_de_projetos.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
// Imports corretos dos Ícones
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
// Imports do ViewModel e Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// Imports das suas classes de dados
import com.example.trabalho_gerenciamento_de_projetos.data.model.Assessment
import com.example.trabalho_gerenciamento_de_projetos.data.model.AssessmentType
import com.example.trabalho_gerenciamento_de_projetos.data.model.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onAddAssessment: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Painel de Estudos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.pendingCount} pendente(s) · ${uiState.urgentCount} urgente(s)",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddAssessment,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar avaliação")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                SummaryRow(uiState = uiState)
            }

            item {
                FilterRow(
                    selected = uiState.filter,
                    onSelect = { viewModel.setFilter(it) }
                )
            }

            // Destaque — avaliação mais urgente
            uiState.topPriority?.let { top ->
                item {
                    AnimatedVisibility(
                        visible = uiState.filter != FilterOption.CONCLUIDAS,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        HighlightCard(
                            assessment = top,
                            onToggle = { viewModel.toggleCompleted(top.id) }
                        )
                    }
                }
            }

            if (uiState.filtered.isEmpty()) {
                item { EmptyState(filter = uiState.filter) }
            } else {
                items(
                    items = uiState.filtered,
                    key = { it.id }
                ) { assessment ->
                    AssessmentCard(
                        assessment = assessment,
                        onToggle = { viewModel.toggleCompleted(assessment.id) }
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                }
            }
        }
    }
}

// ─── Summary Row ─────────────────────────────────────────────────────────────

@Composable
private fun SummaryRow(uiState: DashboardUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryChip(
            label = "Pendentes",
            value = uiState.pendingCount.toString(),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f)
        )
        SummaryChip(
            label = "Urgentes",
            value = uiState.urgentCount.toString(),
            containerColor = MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.weight(1f)
        )
        SummaryChip(
            label = "Concluídas",
            value = uiState.assessments.count { it.completed }.toString(),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryChip(
    label: String,
    value: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = containerColor
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─── Filter Row ───────────────────────────────────────────────────────────────

@Composable
private fun FilterRow(
    selected: FilterOption,
    onSelect: (FilterOption) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterOption.entries.forEach { option ->
            val label = when (option) {
                FilterOption.TODAS -> "Todas"
                FilterOption.PENDENTES -> "Pendentes"
                FilterOption.CONCLUIDAS -> "Concluídas"
            }
            FilterChip(
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(label) }
            )
        }
    }
    Spacer(Modifier.height(4.dp))
}

// ─── Highlight Card ───────────────────────────────────────────────────────────

@Composable
private fun HighlightCard(
    assessment: Assessment,
    onToggle: () -> Unit
) {
    val subjectColor = runCatching {
        Color(assessment.subjectColor.toColorInt())
    }.getOrElse { MaterialTheme.colorScheme.primary }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = subjectColor.copy(alpha = 0.12f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    PriorityBadge(priority = assessment.priority)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Mais urgente",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = assessment.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = assessment.subjectName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DaysChip(days = assessment.daysUntilDue)
                    WeightChip(weight = assessment.weight)
                }
            }
            Spacer(Modifier.width(12.dp))
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (assessment.completed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "Concluir",
                    tint = if (assessment.completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

// ─── Assessment Card ──────────────────────────────────────────────────────────

@Composable
private fun AssessmentCard(
    assessment: Assessment,
    onToggle: () -> Unit
) {
    val subjectColor = runCatching {
        Color(assessment.subjectColor.toColorInt())
    }.getOrElse { MaterialTheme.colorScheme.primary }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Color dot
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(subjectColor)
        )
        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = assessment.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (!assessment.completed) FontWeight.Medium else FontWeight.Normal,
                textDecoration = if (assessment.completed) TextDecoration.LineThrough else TextDecoration.None,
                color = if (assessment.completed)
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = assessment.subjectName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Text(
                    text = "·",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = assessment.type.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            if (!assessment.completed) {
                PriorityBadge(priority = assessment.priority)
                DaysChip(days = assessment.daysUntilDue)
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─── Empty State ──────────────────────────────────────────────────────────────

@Composable
private fun EmptyState(filter: FilterOption) {
    val message = when (filter) {
        FilterOption.PENDENTES -> "Nenhuma avaliação pendente.\nTudo em dia!"
        FilterOption.CONCLUIDAS -> "Nenhuma avaliação concluída ainda."
        FilterOption.TODAS -> "Nenhuma avaliação cadastrada."
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.School,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// ─── Small helpers ────────────────────────────────────────────────────────────

@Composable
private fun PriorityBadge(priority: Priority) {
    val (label, color) = when (priority) {
        Priority.URGENTE -> "Urgente" to MaterialTheme.colorScheme.error
        Priority.ALTA -> "Alta" to Color(0xFFE65100)
        Priority.MEDIA -> "Média" to Color(0xFF1565C0)
        Priority.BAIXA -> "Baixa" to MaterialTheme.colorScheme.outline
    }
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DaysChip(days: Long) {
    val (text, color) = when {
        days < 0 -> "Atrasado" to MaterialTheme.colorScheme.error
        days == 0L -> "Hoje" to MaterialTheme.colorScheme.error
        days == 1L -> "Amanhã" to Color(0xFFE65100)
        else -> "Em $days dias" to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontWeight = if (days <= 1) FontWeight.SemiBold else FontWeight.Normal
    )
}

@Composable
private fun WeightChip(weight: Double) {
    Text(
        text = "Peso ${weight.toInt()}%",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

private val AssessmentType.label: String
    get() = when (this) {
        AssessmentType.PROVA -> "Prova"
        AssessmentType.TRABALHO -> "Trabalho"
        AssessmentType.SEMINARIO -> "Seminário"
    }

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardScreen()
    }
}
