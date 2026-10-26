package br.com.studyflow.ui.state

sealed interface ExclusaoMateriaUiState {
    data object Ocioso : ExclusaoMateriaUiState
    data object Excluindo : ExclusaoMateriaUiState
    data object Sucesso : ExclusaoMateriaUiState
    data class Erro(val mensagem: String) : ExclusaoMateriaUiState
}
