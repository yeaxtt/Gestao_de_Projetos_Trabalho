package br.com.studyflow.ui.state

import br.com.studyflow.model.Feriado

sealed interface FeriadosUiState {
    data object Carregando : FeriadosUiState
    data class Sucesso(
        val proximos: List<Feriado>,
        val passados: List<Feriado>
    ) : FeriadosUiState
    data class Erro(val mensagem: String) : FeriadosUiState
}
