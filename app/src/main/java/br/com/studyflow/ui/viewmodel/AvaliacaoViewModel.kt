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
import br.com.studyflow.model.Avaliacao
import br.com.studyflow.model.AvaliacaoDetalhes
import br.com.studyflow.model.FiltroStatus
import br.com.studyflow.model.OrdenacaoAvaliacao
import br.com.studyflow.model.Prioridade
import br.com.studyflow.notifications.AgendadorLembretes
import br.com.studyflow.ui.state.ExclusaoAvaliacaoUiState
import br.com.studyflow.ui.state.FormularioAvaliacaoUiState
import br.com.studyflow.ui.state.ListaAvaliacoesUiState
import br.com.studyflow.ui.state.SalvarAvaliacaoUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AvaliacaoViewModel(
    private val repository: AvaliacaoRepository,
    private val materiaRepository: MateriaRepository,
    private val preferencesManager: AppPreferencesManager,
    private val agendadorLembretes: AgendadorLembretes
) : ViewModel() {

    // ==========================================
    // Estados da tela de listagem
    // ==========================================
    private val _listaUiState = MutableStateFlow<ListaAvaliacoesUiState>(ListaAvaliacoesUiState.Carregando)
    val listaUiState: StateFlow<ListaAvaliacoesUiState> = _listaUiState.asStateFlow()

    // Item aguardando confirmação no AlertDialog de exclusão (null = diálogo fechado)
    private val _avaliacaoParaExcluir = MutableStateFlow<Avaliacao?>(null)
    val avaliacaoParaExcluir: StateFlow<Avaliacao?> = _avaliacaoParaExcluir.asStateFlow()

    private val _exclusaoUiState = MutableStateFlow<ExclusaoAvaliacaoUiState>(ExclusaoAvaliacaoUiState.Ocioso)
    val exclusaoUiState: StateFlow<ExclusaoAvaliacaoUiState> = _exclusaoUiState.asStateFlow()

    private val _ordenacaoAtual = MutableStateFlow(preferencesManager.obterOrdenacaoAvaliacao())
    val ordenacaoAtual: StateFlow<OrdenacaoAvaliacao> = _ordenacaoAtual.asStateFlow()

    private val _filtroStatusAtual = MutableStateFlow(FiltroStatus.TODAS)
    val filtroStatusAtual: StateFlow<FiltroStatus> = _filtroStatusAtual.asStateFlow()

    private val _materiaFiltroAtual = MutableStateFlow<Int?>(null)
    val materiaFiltroAtual: StateFlow<Int?> = _materiaFiltroAtual.asStateFlow()

    private var avaliacoesJob: Job? = null

    // ==========================================
    // Estados da tela de formulário
    // ==========================================
    private val _formularioUiState = MutableStateFlow<FormularioAvaliacaoUiState>(FormularioAvaliacaoUiState.Ocioso)
    val formularioUiState: StateFlow<FormularioAvaliacaoUiState> = _formularioUiState.asStateFlow()

    // AlertDialog de descartar alterações do formulário
    private val _descarteVisivel = MutableStateFlow(false)
    val descarteVisivel: StateFlow<Boolean> = _descarteVisivel.asStateFlow()

    private val _salvarUiState = MutableStateFlow<SalvarAvaliacaoUiState>(SalvarAvaliacaoUiState.Ocioso)
    val salvarUiState: StateFlow<SalvarAvaliacaoUiState> = _salvarUiState.asStateFlow()

    init {
        observarAvaliacoes()
    }

    // ==========================================
    // Operações da tela de listagem
    // ==========================================

    fun carregarAvaliacoes() {
        observarAvaliacoes()
    }

    fun alterarOrdenacao(novaOrdenacao: OrdenacaoAvaliacao) {
        preferencesManager.salvarOrdenacaoAvaliacao(novaOrdenacao)
        _ordenacaoAtual.value = novaOrdenacao
    }

    fun alterarFiltroStatus(novoFiltro: FiltroStatus) {
        _filtroStatusAtual.value = novoFiltro
    }

    fun alterarMateriaFiltro(materiaId: Int?) {
        _materiaFiltroAtual.value = materiaId
    }

    private fun observarAvaliacoes() {
        avaliacoesJob?.cancel()
        _listaUiState.value = ListaAvaliacoesUiState.Carregando
        avaliacoesJob = viewModelScope.launch {
            combine(
                repository.avaliacoesEmTempoReal,
                materiaRepository.materiasEmTempoReal,
                _ordenacaoAtual,
                _filtroStatusAtual,
                _materiaFiltroAtual
            ) { avaliacoes, materias, ordenacao, status, materiaId ->
                // Se a matéria do filtro foi excluída, volta a mostrar todas.
                val materiaValida = materiaId?.takeIf { id -> materias.any { it.id == id } }
                val filtradas = avaliacoes.filter {
                    (materiaValida == null || it.avaliacao.materiaId == materiaValida) && when (status) {
                        FiltroStatus.TODAS -> true
                        FiltroStatus.PENDENTES -> !it.avaliacao.concluida
                        FiltroStatus.CONCLUIDAS -> it.avaliacao.concluida
                    }
                }
                ListaAvaliacoesUiState.Sucesso(ordenarAvaliacoes(filtradas, ordenacao), materias)
            }
                .catch {
                    _listaUiState.value = ListaAvaliacoesUiState.Erro(
                        "Erro ao carregar avaliações do banco local. Tente novamente."
                    )
                }
                .collect { estado ->
                    _listaUiState.value = estado
                }
        }
    }

    private fun ordenarAvaliacoes(avaliacoes: List<AvaliacaoDetalhes>, ordenacao: OrdenacaoAvaliacao): List<AvaliacaoDetalhes> {
        return when (ordenacao) {
            OrdenacaoAvaliacao.PRIORIDADE_DESC -> avaliacoes.sortedByDescending { Prioridade.calcular(it.avaliacao) }
            OrdenacaoAvaliacao.PRAZO_ASC -> avaliacoes.sortedBy { it.avaliacao.dataMillis }
            OrdenacaoAvaliacao.PRAZO_DESC -> avaliacoes.sortedByDescending { it.avaliacao.dataMillis }
            OrdenacaoAvaliacao.TITULO_ASC -> avaliacoes.sortedBy { it.avaliacao.titulo.lowercase() }
            OrdenacaoAvaliacao.PESO_DESC -> avaliacoes.sortedByDescending { it.avaliacao.peso }
        }
    }

    fun alternarConclusao(avaliacao: Avaliacao) {
        viewModelScope.launch {
            try {
                val atualizada = repository.alternarConclusao(avaliacao)
                agendadorLembretes.reagendar(atualizada)
            } catch (_: Exception) {
                // A lista não muda; o usuário pode tentar de novo
            }
        }
    }

    fun solicitarExclusao(avaliacao: Avaliacao) {
        _avaliacaoParaExcluir.value = avaliacao
    }

    fun cancelarExclusao() {
        _avaliacaoParaExcluir.value = null
    }

    fun confirmarExclusao() {
        val avaliacao = _avaliacaoParaExcluir.value ?: return
        _avaliacaoParaExcluir.value = null
        excluir(avaliacao)
    }

    private fun excluir(avaliacao: Avaliacao) {
        _exclusaoUiState.value = ExclusaoAvaliacaoUiState.Excluindo
        viewModelScope.launch {
            try {
                agendadorLembretes.cancelar(avaliacao.id)
                repository.removerAvaliacao(avaliacao)
                _exclusaoUiState.value = ExclusaoAvaliacaoUiState.Sucesso
            } catch (_: Exception) {
                _exclusaoUiState.value = ExclusaoAvaliacaoUiState.Erro(
                    "Erro ao excluir avaliação. Tente novamente."
                )
            }
        }
    }

    fun limparExclusaoUiState() {
        _exclusaoUiState.value = ExclusaoAvaliacaoUiState.Ocioso
    }

    // ==========================================
    // Operações da tela de formulário
    // ==========================================

    fun prepararNovaAvaliacao() {
        _descarteVisivel.value = false
        _formularioUiState.value = FormularioAvaliacaoUiState.Carregando
        _salvarUiState.value = SalvarAvaliacaoUiState.Ocioso
        viewModelScope.launch {
            _formularioUiState.value = try {
                FormularioAvaliacaoUiState.Sucesso(
                    avaliacao = null,
                    materias = materiaRepository.materiasEmTempoReal.first()
                )
            } catch (_: Exception) {
                FormularioAvaliacaoUiState.Erro(
                    "Erro ao preparar formulário de avaliação."
                )
            }
        }
    }

    fun carregarAvaliacao(id: Int) {
        _descarteVisivel.value = false
        _formularioUiState.value = FormularioAvaliacaoUiState.Carregando
        _salvarUiState.value = SalvarAvaliacaoUiState.Ocioso
        viewModelScope.launch {
            _formularioUiState.value = try {
                val avaliacao = repository.obterPorId(id)
                if (avaliacao != null) {
                    FormularioAvaliacaoUiState.Sucesso(
                        avaliacao = avaliacao,
                        materias = materiaRepository.materiasEmTempoReal.first()
                    )
                } else {
                    FormularioAvaliacaoUiState.Erro(
                        "Avaliação não encontrada no banco local."
                    )
                }
            } catch (_: Exception) {
                FormularioAvaliacaoUiState.Erro(
                    "Erro ao carregar avaliação. Tente novamente."
                )
            }
        }
    }

    fun salvar(avaliacao: Avaliacao) {
        _salvarUiState.value = SalvarAvaliacaoUiState.Salvando
        viewModelScope.launch {
            try {
                val id = repository.salvarAvaliacao(avaliacao)
                agendadorLembretes.reagendar(avaliacao.copy(id = id))
                _salvarUiState.value = SalvarAvaliacaoUiState.Sucesso
            } catch (_: Exception) {
                _salvarUiState.value = SalvarAvaliacaoUiState.Erro(
                    "Erro ao salvar avaliação no banco local. Tente novamente."
                )
            }
        }
    }

    fun solicitarDescarte() {
        _descarteVisivel.value = true
    }

    fun cancelarDescarte() {
        _descarteVisivel.value = false
    }

    fun confirmarDescarte() {
        _descarteVisivel.value = false
    }

    fun limparSalvarUiState() {
        _salvarUiState.value = SalvarAvaliacaoUiState.Ocioso
    }

    companion object {
        // O Context só é usado aqui, para montar as dependências; a ViewModel não guarda nenhum.
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                val database = AppDatabase.obterInstancia(application)
                AvaliacaoViewModel(
                    repository = AvaliacaoRepository(database.avaliacaoDao()),
                    materiaRepository = MateriaRepository(database.materiaDao()),
                    preferencesManager = AppPreferencesManager(application),
                    agendadorLembretes = AgendadorLembretes(WorkManager.getInstance(application))
                )
            }
        }
    }
}
