package br.com.studyflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.studyflow.data.local.AppDatabase
import br.com.studyflow.data.repository.AvaliacaoRepository
import br.com.studyflow.model.AvaliacaoDetalhes
import br.com.studyflow.model.Prioridade
import br.com.studyflow.ui.state.PainelUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar

class PainelViewModel(
    private val repository: AvaliacaoRepository
) : ViewModel() {

    private val _painelUiState = MutableStateFlow<PainelUiState>(PainelUiState.Carregando)
    val painelUiState: StateFlow<PainelUiState> = _painelUiState.asStateFlow()

    private var painelJob: Job? = null

    init {
        observarPainel()
    }

    fun carregarPainel() {
        observarPainel()
    }

    private fun observarPainel() {
        painelJob?.cancel()
        _painelUiState.value = PainelUiState.Carregando
        painelJob = viewModelScope.launch {
            repository.avaliacoesEmTempoReal
                .catch {
                    _painelUiState.value = PainelUiState.Erro(
                        "Erro ao carregar o painel do banco local. Tente novamente."
                    )
                }
                .collect { avaliacoes ->
                    _painelUiState.value = montarPainel(avaliacoes)
                }
        }
    }

    // Pendentes ordenadas pela regra 60% urgência + 40% impacto.
    private fun montarPainel(avaliacoes: List<AvaliacaoDetalhes>): PainelUiState.Sucesso {
        val inicioDeHoje = inicioDeHoje()
        val pendentes = avaliacoes.filter { !it.avaliacao.concluida }
        return PainelUiState.Sucesso(
            prioridades = pendentes.sortedByDescending { Prioridade.calcular(it.avaliacao) },
            pendentes = pendentes.size,
            concluidas = avaliacoes.count { it.avaliacao.concluida },
            atrasadas = pendentes.count { it.avaliacao.dataMillis < inicioDeHoje }
        )
    }

    private fun inicioDeHoje(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                PainelViewModel(AvaliacaoRepository(AppDatabase.obterInstancia(application).avaliacaoDao()))
            }
        }
    }
}
