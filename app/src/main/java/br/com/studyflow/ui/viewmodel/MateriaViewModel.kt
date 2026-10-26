package br.com.studyflow.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.studyflow.data.local.AppDatabase
import br.com.studyflow.data.local.preferences.AppPreferencesManager
import br.com.studyflow.data.repository.AvaliacaoRepository
import br.com.studyflow.data.repository.MateriaRepository
import br.com.studyflow.model.Materia
import br.com.studyflow.model.MateriaDetalhes
import br.com.studyflow.model.OrdenacaoMateria
import br.com.studyflow.notifications.AgendadorLembretes
import br.com.studyflow.ui.state.ExclusaoMateriaUiState
import br.com.studyflow.ui.state.FormularioMateriaUiState
import br.com.studyflow.ui.state.ListaMateriasUiState
import br.com.studyflow.ui.state.SalvarMateriaUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MateriaViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: MateriaRepository = MateriaRepository(
        AppDatabase.obterInstancia(application).materiaDao()
    ),
    private val avaliacaoRepository: AvaliacaoRepository = AvaliacaoRepository(
        AppDatabase.obterInstancia(application).avaliacaoDao()
    ),
    private val preferencesManager: AppPreferencesManager = AppPreferencesManager(application)
) : AndroidViewModel(application) {

    // Estados da tela de listagem
    var listaUiState: ListaMateriasUiState by mutableStateOf(ListaMateriasUiState.Carregando)
        private set

    var exclusaoUiState: ExclusaoMateriaUiState by mutableStateOf(ExclusaoMateriaUiState.Ocioso)
        private set

    var ordenacaoAtual: OrdenacaoMateria by mutableStateOf(preferencesManager.obterOrdenacaoMateria())
        private set

    private val ordenacaoFlow = MutableStateFlow(preferencesManager.obterOrdenacaoMateria())

    // Estados da tela de formulário
    var formularioUiState: FormularioMateriaUiState by mutableStateOf(FormularioMateriaUiState.Ocioso)
        private set

    var salvarUiState: SalvarMateriaUiState by mutableStateOf(SalvarMateriaUiState.Ocioso)
        private set

    private var materiasJob: Job? = null

    init {
        observarMaterias()
    }

    // ============================
    // Operações da tela de listagem
    // ============================

    fun carregarMaterias() {
        observarMaterias()
    }

    fun alterarOrdenacao(novaOrdenacao: OrdenacaoMateria) {
        ordenacaoAtual = novaOrdenacao
        preferencesManager.salvarOrdenacaoMateria(novaOrdenacao)
        ordenacaoFlow.value = novaOrdenacao
    }

    private fun observarMaterias() {
        materiasJob?.cancel()
        listaUiState = ListaMateriasUiState.Carregando
        materiasJob = viewModelScope.launch {
            repository.materiasComAvaliacoesEmTempoReal
                .combine(ordenacaoFlow) { materias, ordenacao ->
                    ordenarMaterias(materias, ordenacao)
                }
                .catch {
                    listaUiState = ListaMateriasUiState.Erro(
                        "Erro ao carregar matérias do banco local. Tente novamente."
                    )
                }
                .collect { materiasOrdenadas ->
                    listaUiState = ListaMateriasUiState.Sucesso(materiasOrdenadas)
                }
        }
    }

    private fun ordenarMaterias(materias: List<MateriaDetalhes>, ordenacao: OrdenacaoMateria): List<MateriaDetalhes> {
        return when (ordenacao) {
            OrdenacaoMateria.NOME_ASC -> materias.sortedBy { it.materia.nome.lowercase() }
            OrdenacaoMateria.NOME_DESC -> materias.sortedByDescending { it.materia.nome.lowercase() }
            OrdenacaoMateria.AVALIACOES_DESC -> materias.sortedByDescending { it.avaliacoes.size }
            OrdenacaoMateria.AVALIACOES_ASC -> materias.sortedBy { it.avaliacoes.size }
        }
    }

    fun excluir(materia: Materia) {
        exclusaoUiState = ExclusaoMateriaUiState.Excluindo
        viewModelScope.launch {
            try {
                // As avaliações saem junto (ON DELETE CASCADE); antes, cancela os lembretes delas.
                avaliacaoRepository.obterIdsPorMateria(materia.id).forEach {
                    AgendadorLembretes.cancelar(getApplication(), it)
                }
                repository.removerMateria(materia)
                exclusaoUiState = ExclusaoMateriaUiState.Sucesso
            } catch (_: Exception) {
                exclusaoUiState = ExclusaoMateriaUiState.Erro(
                    "Erro ao excluir matéria. Tente novamente."
                )
            }
        }
    }

    fun limparExclusaoUiState() {
        exclusaoUiState = ExclusaoMateriaUiState.Ocioso
    }

    // ================================
    // Operações da tela de formulário
    // ================================

    fun prepararNovaMateria() {
        formularioUiState = FormularioMateriaUiState.Ocioso
        salvarUiState = SalvarMateriaUiState.Ocioso
    }

    fun carregarMateria(id: Int) {
        formularioUiState = FormularioMateriaUiState.Carregando
        salvarUiState = SalvarMateriaUiState.Ocioso
        viewModelScope.launch {
            formularioUiState = try {
                val materia = repository.obterPorId(id)
                if (materia != null) {
                    FormularioMateriaUiState.Sucesso(materia)
                } else {
                    FormularioMateriaUiState.Erro(
                        "Matéria não encontrada no banco local."
                    )
                }
            } catch (_: Exception) {
                FormularioMateriaUiState.Erro(
                    "Erro ao carregar matéria. Tente novamente."
                )
            }
        }
    }

    fun salvar(materia: Materia) {
        salvarUiState = SalvarMateriaUiState.Salvando
        viewModelScope.launch {
            try {
                repository.salvarMateria(materia)
                salvarUiState = SalvarMateriaUiState.Sucesso
            } catch (_: Exception) {
                salvarUiState = SalvarMateriaUiState.Erro(
                    "Erro ao salvar matéria no banco local. Tente novamente."
                )
            }
        }
    }

    fun limparSalvarUiState() {
        salvarUiState = SalvarMateriaUiState.Ocioso
    }
}
