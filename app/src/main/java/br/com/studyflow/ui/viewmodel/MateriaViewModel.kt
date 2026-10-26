package br.com.studyflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.WorkManager
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MateriaViewModel(
    private val repository: MateriaRepository,
    private val avaliacaoRepository: AvaliacaoRepository,
    private val preferencesManager: AppPreferencesManager,
    private val agendadorLembretes: AgendadorLembretes
) : ViewModel() {

    // Estados da tela de listagem
    private val _listaUiState = MutableStateFlow<ListaMateriasUiState>(ListaMateriasUiState.Carregando)
    val listaUiState: StateFlow<ListaMateriasUiState> = _listaUiState.asStateFlow()

    private val _exclusaoUiState = MutableStateFlow<ExclusaoMateriaUiState>(ExclusaoMateriaUiState.Ocioso)
    val exclusaoUiState: StateFlow<ExclusaoMateriaUiState> = _exclusaoUiState.asStateFlow()

    private val _ordenacaoAtual = MutableStateFlow(preferencesManager.obterOrdenacaoMateria())
    val ordenacaoAtual: StateFlow<OrdenacaoMateria> = _ordenacaoAtual.asStateFlow()

    // Estados da tela de formulário
    private val _formularioUiState = MutableStateFlow<FormularioMateriaUiState>(FormularioMateriaUiState.Ocioso)
    val formularioUiState: StateFlow<FormularioMateriaUiState> = _formularioUiState.asStateFlow()

    private val _salvarUiState = MutableStateFlow<SalvarMateriaUiState>(SalvarMateriaUiState.Ocioso)
    val salvarUiState: StateFlow<SalvarMateriaUiState> = _salvarUiState.asStateFlow()

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
        preferencesManager.salvarOrdenacaoMateria(novaOrdenacao)
        _ordenacaoAtual.value = novaOrdenacao
    }

    private fun observarMaterias() {
        materiasJob?.cancel()
        _listaUiState.value = ListaMateriasUiState.Carregando
        materiasJob = viewModelScope.launch {
            repository.materiasComAvaliacoesEmTempoReal
                .combine(_ordenacaoAtual) { materias, ordenacao ->
                    ordenarMaterias(materias, ordenacao)
                }
                .catch {
                    _listaUiState.value = ListaMateriasUiState.Erro(
                        "Erro ao carregar matérias do banco local. Tente novamente."
                    )
                }
                .collect { materiasOrdenadas ->
                    _listaUiState.value = ListaMateriasUiState.Sucesso(materiasOrdenadas)
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
        _exclusaoUiState.value = ExclusaoMateriaUiState.Excluindo
        viewModelScope.launch {
            try {
                // As avaliações saem junto (ON DELETE CASCADE); antes, cancela os lembretes delas.
                avaliacaoRepository.obterIdsPorMateria(materia.id).forEach {
                    agendadorLembretes.cancelar(it)
                }
                repository.removerMateria(materia)
                _exclusaoUiState.value = ExclusaoMateriaUiState.Sucesso
            } catch (_: Exception) {
                _exclusaoUiState.value = ExclusaoMateriaUiState.Erro(
                    "Erro ao excluir matéria. Tente novamente."
                )
            }
        }
    }

    fun limparExclusaoUiState() {
        _exclusaoUiState.value = ExclusaoMateriaUiState.Ocioso
    }

    // ================================
    // Operações da tela de formulário
    // ================================

    fun prepararNovaMateria() {
        _formularioUiState.value = FormularioMateriaUiState.Ocioso
        _salvarUiState.value = SalvarMateriaUiState.Ocioso
    }

    fun carregarMateria(id: Int) {
        _formularioUiState.value = FormularioMateriaUiState.Carregando
        _salvarUiState.value = SalvarMateriaUiState.Ocioso
        viewModelScope.launch {
            _formularioUiState.value = try {
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
        _salvarUiState.value = SalvarMateriaUiState.Salvando
        viewModelScope.launch {
            try {
                repository.salvarMateria(materia)
                _salvarUiState.value = SalvarMateriaUiState.Sucesso
            } catch (_: Exception) {
                _salvarUiState.value = SalvarMateriaUiState.Erro(
                    "Erro ao salvar matéria no banco local. Tente novamente."
                )
            }
        }
    }

    fun limparSalvarUiState() {
        _salvarUiState.value = SalvarMateriaUiState.Ocioso
    }

    companion object {
        // O Context só é usado aqui, para montar as dependências; a ViewModel não guarda nenhum.
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val database = AppDatabase.obterInstancia(application)
                MateriaViewModel(
                    repository = MateriaRepository(database.materiaDao()),
                    avaliacaoRepository = AvaliacaoRepository(database.avaliacaoDao()),
                    preferencesManager = AppPreferencesManager(application),
                    agendadorLembretes = AgendadorLembretes(WorkManager.getInstance(application))
                )
            }
        }
    }
}
