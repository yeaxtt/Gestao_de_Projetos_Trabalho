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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AvaliacaoViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: AvaliacaoRepository = AvaliacaoRepository(
        AppDatabase.obterInstancia(application).avaliacaoDao()
    ),
    private val materiaRepository: MateriaRepository = MateriaRepository(
        AppDatabase.obterInstancia(application).materiaDao()
    ),
    private val preferencesManager: AppPreferencesManager = AppPreferencesManager(application)
) : AndroidViewModel(application) {

    // ==========================================
    // Estados da tela de listagem
    // ==========================================
    var listaUiState: ListaAvaliacoesUiState by mutableStateOf(ListaAvaliacoesUiState.Carregando)
        private set

    var exclusaoUiState: ExclusaoAvaliacaoUiState by mutableStateOf(ExclusaoAvaliacaoUiState.Ocioso)
        private set

    var ordenacaoAtual: OrdenacaoAvaliacao by mutableStateOf(preferencesManager.obterOrdenacaoAvaliacao())
        private set

    var filtroStatusAtual: FiltroStatus by mutableStateOf(FiltroStatus.TODAS)
        private set

    var materiaFiltroAtual: Int? by mutableStateOf(null)
        private set

    private val ordenacaoFlow = MutableStateFlow(preferencesManager.obterOrdenacaoAvaliacao())
    private val filtroStatusFlow = MutableStateFlow(FiltroStatus.TODAS)
    private val materiaFiltroFlow = MutableStateFlow<Int?>(null)
    private var avaliacoesJob: Job? = null

    // ==========================================
    // Estados da tela de formulário
    // ==========================================
    var formularioUiState: FormularioAvaliacaoUiState by mutableStateOf(FormularioAvaliacaoUiState.Ocioso)
        private set

    var salvarUiState: SalvarAvaliacaoUiState by mutableStateOf(SalvarAvaliacaoUiState.Ocioso)
        private set

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
        ordenacaoAtual = novaOrdenacao
        preferencesManager.salvarOrdenacaoAvaliacao(novaOrdenacao)
        ordenacaoFlow.value = novaOrdenacao
    }

    fun alterarFiltroStatus(novoFiltro: FiltroStatus) {
        filtroStatusAtual = novoFiltro
        filtroStatusFlow.value = novoFiltro
    }

    fun alterarMateriaFiltro(materiaId: Int?) {
        materiaFiltroAtual = materiaId
        materiaFiltroFlow.value = materiaId
    }

    private fun observarAvaliacoes() {
        avaliacoesJob?.cancel()
        listaUiState = ListaAvaliacoesUiState.Carregando
        avaliacoesJob = viewModelScope.launch {
            combine(
                repository.avaliacoesEmTempoReal,
                materiaRepository.materiasEmTempoReal,
                ordenacaoFlow,
                filtroStatusFlow,
                materiaFiltroFlow
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
                    listaUiState = ListaAvaliacoesUiState.Erro(
                        "Erro ao carregar avaliações do banco local. Tente novamente."
                    )
                }
                .collect { estado ->
                    listaUiState = estado
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
                AgendadorLembretes.reagendar(getApplication(), atualizada)
            } catch (_: Exception) {
                // A lista não muda; o usuário pode tentar de novo
            }
        }
    }

    fun excluir(avaliacao: Avaliacao) {
        exclusaoUiState = ExclusaoAvaliacaoUiState.Excluindo
        viewModelScope.launch {
            try {
                AgendadorLembretes.cancelar(getApplication(), avaliacao.id)
                repository.removerAvaliacao(avaliacao)
                exclusaoUiState = ExclusaoAvaliacaoUiState.Sucesso
            } catch (_: Exception) {
                exclusaoUiState = ExclusaoAvaliacaoUiState.Erro(
                    "Erro ao excluir avaliação. Tente novamente."
                )
            }
        }
    }

    fun limparExclusaoUiState() {
        exclusaoUiState = ExclusaoAvaliacaoUiState.Ocioso
    }

    // ==========================================
    // Operações da tela de formulário
    // ==========================================

    fun prepararNovaAvaliacao() {
        formularioUiState = FormularioAvaliacaoUiState.Carregando
        salvarUiState = SalvarAvaliacaoUiState.Ocioso
        viewModelScope.launch {
            formularioUiState = try {
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
        formularioUiState = FormularioAvaliacaoUiState.Carregando
        salvarUiState = SalvarAvaliacaoUiState.Ocioso
        viewModelScope.launch {
            formularioUiState = try {
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
        salvarUiState = SalvarAvaliacaoUiState.Salvando
        viewModelScope.launch {
            try {
                val id = repository.salvarAvaliacao(avaliacao)
                AgendadorLembretes.reagendar(getApplication(), avaliacao.copy(id = id))
                salvarUiState = SalvarAvaliacaoUiState.Sucesso
            } catch (_: Exception) {
                salvarUiState = SalvarAvaliacaoUiState.Erro(
                    "Erro ao salvar avaliação no banco local. Tente novamente."
                )
            }
        }
    }

    fun limparSalvarUiState() {
        salvarUiState = SalvarAvaliacaoUiState.Ocioso
    }
}
