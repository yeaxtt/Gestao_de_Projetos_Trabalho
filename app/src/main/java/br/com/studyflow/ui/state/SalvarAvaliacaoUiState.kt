package br.com.studyflow.ui.state

sealed interface SalvarAvaliacaoUiState {
    data object Ocioso : SalvarAvaliacaoUiState
    data object Salvando : SalvarAvaliacaoUiState
    data object Sucesso : SalvarAvaliacaoUiState
    data class Erro(val mensagem: String) : SalvarAvaliacaoUiState
}
