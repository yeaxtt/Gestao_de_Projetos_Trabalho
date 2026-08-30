package com.example.trabalho_gerenciamento_de_projetos.dashboard

import androidx.lifecycle.ViewModel
import com.example.trabalho_gerenciamento_de_projetos.data.model.Assessment
import com.example.trabalho_gerenciamento_de_projetos.data.model.AssessmentType
import com.example.trabalho_gerenciamento_de_projetos.data.model.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

enum class FilterOption { TODAS, PENDENTES, CONCLUIDAS }

data class DashboardUiState(
    val assessments: List<Assessment> = emptyList(),
    val filter: FilterOption = FilterOption.TODAS,
    val isLoading: Boolean = false
) {
    val filtered: List<Assessment>
        get() = when (filter) {
            FilterOption.TODAS -> assessments
            FilterOption.PENDENTES -> assessments.filter { !it.completed }
            FilterOption.CONCLUIDAS -> assessments.filter { it.completed }
        }.sortedWith(
            compareBy(
                { it.priority.ordinal },
                { it.daysUntilDue }
            )
        )

    val topPriority: Assessment?
        get() = filtered.firstOrNull { !it.completed }

    val pendingCount: Int get() = assessments.count { !it.completed }
    val urgentCount: Int get() = assessments.count { it.priority == Priority.URGENTE && !it.completed }
}

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState(assessments = fakeSeedData()))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun setFilter(filter: FilterOption) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun toggleCompleted(assessmentId: Long) {
        _uiState.update { state ->
            state.copy(
                assessments = state.assessments.map { a ->
                    if (a.id == assessmentId) a.copy(completed = !a.completed) else a
                }
            )
        }
    }
}

private fun fakeSeedData(): List<Assessment> {
    val today = LocalDate.now()
    return listOf(
        Assessment(
            id = 1,
            subjectId = 1,
            subjectName = "Programação para Dispositivos Móveis",
            subjectColor = "#3F51B5",
            title = "Trabalho de Compose",
            type = AssessmentType.TRABALHO,
            dueDate = today.plusDays(2),
            weight = 30.0,
            description = "Entregar pelo GitHub"
        ),
        Assessment(
            id = 2,
            subjectId = 2,
            subjectName = "Matemática",
            subjectColor = "#E91E63",
            title = "Prova P2",
            type = AssessmentType.PROVA,
            dueDate = today.plusDays(5),
            weight = 40.0
        ),
        Assessment(
            id = 3,
            subjectId = 3,
            subjectName = "Engenharia de Software",
            subjectColor = "#009688",
            title = "Seminário de Arquitetura",
            type = AssessmentType.SEMINARIO,
            dueDate = today.plusDays(10),
            weight = 20.0,
            description = "Apresentação em grupo"
        ),
        Assessment(
            id = 4,
            subjectId = 1,
            subjectName = "Programação para Dispositivos Móveis",
            subjectColor = "#3F51B5",
            title = "Lista de Exercícios 3",
            type = AssessmentType.TRABALHO,
            dueDate = today.plusDays(14),
            weight = 10.0,
            completed = true
        ),
        Assessment(
            id = 5,
            subjectId = 2,
            subjectName = "Matemática",
            subjectColor = "#E91E63",
            title = "Prova P1",
            type = AssessmentType.PROVA,
            dueDate = today.minusDays(3),
            weight = 40.0,
            completed = true
        )
    )
}
