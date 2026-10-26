package br.com.studyflow.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.studyflow.Priority
import br.com.studyflow.data.db.AppDatabase
import br.com.studyflow.data.model.Avaliacao
import br.com.studyflow.data.model.Materia
import br.com.studyflow.data.repository.StudyRepository
import br.com.studyflow.notifications.ReminderScheduler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class StatusFilter(val label: String) { TODAS("Todas"), PENDENTES("Pendentes"), CONCLUIDAS("Concluídas") }

/** Estado único da interface, observado pelas telas. */
data class StudyUiState(
    val materias: List<Materia> = emptyList(),
    val avaliacoes: List<Avaliacao> = emptyList(),
    val statusFilter: StatusFilter = StatusFilter.TODAS,
    val materiaFilter: Long? = null
) {
    val prioridades: List<Avaliacao> = avaliacoes.filter { !it.concluida }.sortedByDescending { Priority.score(it) }
    val concluidas: Int = avaliacoes.count { it.concluida }
    /** Não concluídas cujo prazo é anterior a hoje. */
    val atrasadas: Int = avaliacoes.count { !it.concluida && it.dataMillis < inicioDeHoje() }
    val avaliacoesFiltradas: List<Avaliacao> = avaliacoes.filter {
        (materiaFilter == null || it.materiaId == materiaFilter) && when (statusFilter) {
            StatusFilter.TODAS -> true
            StatusFilter.PENDENTES -> !it.concluida
            StatusFilter.CONCLUIDAS -> it.concluida
        }
    }

    fun materia(id: Long): Materia? = materias.firstOrNull { it.id == id }
    fun totalAvaliacoes(materiaId: Long): Int = avaliacoes.count { it.materiaId == materiaId }
}

class StudyViewModel(app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.get(app)
    private val repo = StudyRepository(db.materiaDao(), db.avaliacaoDao())

    private val statusFilter = MutableStateFlow(StatusFilter.TODAS)
    private val materiaFilter = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<StudyUiState> =
        combine(repo.materias, repo.avaliacoes, statusFilter, materiaFilter) { materias, avaliacoes, status, materia ->
            StudyUiState(materias, avaliacoes, status, materia)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StudyUiState())

    fun setStatusFilter(filter: StatusFilter) { statusFilter.value = filter }
    fun setMateriaFilter(id: Long?) { materiaFilter.value = id }

    fun addMateria(nome: String, professor: String) {
        if (nome.isBlank()) return
        viewModelScope.launch { repo.inserirMateria(nome.trim(), professor.trim()) }
    }

    fun updateMateria(m: Materia) {
        if (m.nome.isBlank()) return
        viewModelScope.launch { repo.atualizarMateria(m.copy(nome = m.nome.trim(), professor = m.professor.trim())) }
    }

    fun deleteMateria(id: Long) {
        val state = uiState.value
        if (materiaFilter.value == id) materiaFilter.value = null
        viewModelScope.launch {
            state.avaliacoes.filter { it.materiaId == id }.forEach { ReminderScheduler.cancel(getApplication(), it.id) }
            state.materia(id)?.let { repo.excluirMateria(it) }
        }
    }

    fun addAvaliacao(a: Avaliacao) {
        viewModelScope.launch {
            val id = repo.inserirAvaliacao(a)
            ReminderScheduler.schedule(getApplication(), a.copy(id = id))
        }
    }

    fun updateAvaliacao(a: Avaliacao) {
        viewModelScope.launch {
            repo.atualizarAvaliacao(a)
            // Prazo ou título podem ter mudado: refaz os lembretes
            ReminderScheduler.cancel(getApplication(), a.id)
            if (!a.concluida) ReminderScheduler.schedule(getApplication(), a)
        }
    }

    fun toggle(a: Avaliacao) {
        viewModelScope.launch {
            repo.concluir(a)
            if (a.concluida) ReminderScheduler.schedule(getApplication(), a)
            else ReminderScheduler.cancel(getApplication(), a.id)
        }
    }

    fun delete(a: Avaliacao) {
        viewModelScope.launch {
            ReminderScheduler.cancel(getApplication(), a.id)
            repo.excluirAvaliacao(a)
        }
    }
}

private fun inicioDeHoje(): Long = java.util.Calendar.getInstance().apply {
    set(java.util.Calendar.HOUR_OF_DAY, 0); set(java.util.Calendar.MINUTE, 0)
    set(java.util.Calendar.SECOND, 0); set(java.util.Calendar.MILLISECOND, 0)
}.timeInMillis
