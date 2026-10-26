package br.com.studyflow.ui.state

sealed interface SalvarMateriaUiState {
    data object Ocioso : SalvarMateriaUiState
    data object Salvando : SalvarMateriaUiState
    data object Sucesso : SalvarMateriaUiState
    data class Erro(val mensagem: String) : SalvarMateriaUiState
}
