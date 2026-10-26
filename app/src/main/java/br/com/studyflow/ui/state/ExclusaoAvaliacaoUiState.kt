package br.com.studyflow.ui.state

sealed interface ExclusaoAvaliacaoUiState {
    data object Ocioso : ExclusaoAvaliacaoUiState
    data object Excluindo : ExclusaoAvaliacaoUiState
    data object Sucesso : ExclusaoAvaliacaoUiState
    data class Erro(val mensagem: String) : ExclusaoAvaliacaoUiState
}
