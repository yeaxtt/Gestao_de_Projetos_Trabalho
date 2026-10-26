package br.com.studyflow.ui.state

import br.com.studyflow.model.AvaliacaoDetalhes

sealed interface PainelUiState {
    data object Carregando : PainelUiState
    data class Sucesso(
        val prioridades: List<AvaliacaoDetalhes>,
        val pendentes: Int,
        val concluidas: Int,
        val atrasadas: Int
    ) : PainelUiState
    data class Erro(val mensagem: String) : PainelUiState
}
