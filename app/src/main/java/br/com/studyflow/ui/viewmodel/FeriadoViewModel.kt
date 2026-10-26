package br.com.studyflow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import br.com.studyflow.data.repository.FeriadoRepository
import br.com.studyflow.network.RetrofitClient
import br.com.studyflow.ui.state.FeriadosUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Calendar

class FeriadoViewModel(
    private val repository: FeriadoRepository
) : ViewModel() {

    private val _feriadosUiState = MutableStateFlow<FeriadosUiState>(FeriadosUiState.Carregando)
    val feriadosUiState: StateFlow<FeriadosUiState> = _feriadosUiState.asStateFlow()

    val anoAtual: Int = Calendar.getInstance().get(Calendar.YEAR)

    private val _anoSelecionado = MutableStateFlow(anoAtual)
    val anoSelecionado: StateFlow<Int> = _anoSelecionado.asStateFlow()

    private var feriadosJob: Job? = null

    init {
        carregarFeriados()
    }

    fun alterarAno(ano: Int) {
        if (ano == _anoSelecionado.value) return
        _anoSelecionado.value = ano
        carregarFeriados()
    }

    fun carregarFeriados() {
        feriadosJob?.cancel()
        _feriadosUiState.value = FeriadosUiState.Carregando
        feriadosJob = viewModelScope.launch {
            _feriadosUiState.value = try {
                // Próximos primeiro; os que já passaram ficam no fim da lista
                val hoje = LocalDate.now().toString()
                val (passados, proximos) = repository.obterFeriados(_anoSelecionado.value)
                    .partition { it.data < hoje }
                FeriadosUiState.Sucesso(proximos = proximos, passados = passados)
            } catch (e: CancellationException) {
                // Troca de ano no meio da busca: a requisição antiga é descartada
                throw e
            } catch (_: Exception) {
                FeriadosUiState.Erro(
                    "Não foi possível buscar os feriados. Verifique a conexão com a internet."
                )
            }
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                FeriadoViewModel(FeriadoRepository(RetrofitClient.feriadoApiService))
            }
        }
    }
}
