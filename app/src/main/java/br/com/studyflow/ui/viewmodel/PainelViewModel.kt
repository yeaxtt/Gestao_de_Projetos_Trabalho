package br.com.studyflow.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import br.com.studyflow.data.local.AppDatabase
import br.com.studyflow.data.repository.AvaliacaoRepository
import br.com.studyflow.model.AvaliacaoDetalhes
import br.com.studyflow.model.Prioridade
import br.com.studyflow.ui.state.PainelUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.util.Calendar

class PainelViewModel @JvmOverloads constructor(
    application: Application,
    private val repository: AvaliacaoRepository = AvaliacaoRepository(
        AppDatabase.obterInstancia(application).avaliacaoDao()
    )
) : AndroidViewModel(application) {

    var painelUiState: PainelUiState by mutableStateOf(PainelUiState.Carregando)
        private set

    private var painelJob: Job? = null

    init {
        observarPainel()
    }

    fun carregarPainel() {
        observarPainel()
    }

    private fun observarPainel() {
        painelJob?.cancel()
        painelUiState = PainelUiState.Carregando
        painelJob = viewModelScope.launch {
            repository.avaliacoesEmTempoReal
                .catch {
                    painelUiState = PainelUiState.Erro(
                        "Erro ao carregar o painel do banco local. Tente novamente."
                    )
                }
                .collect { avaliacoes ->
                    painelUiState = montarPainel(avaliacoes)
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
}
