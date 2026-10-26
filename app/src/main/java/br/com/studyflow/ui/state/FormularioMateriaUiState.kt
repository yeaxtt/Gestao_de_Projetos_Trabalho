package br.com.studyflow.ui.state

import br.com.studyflow.model.Materia

sealed interface FormularioMateriaUiState {
    data object Ocioso : FormularioMateriaUiState
    data object Carregando : FormularioMateriaUiState
    data class Sucesso(val materia: Materia) : FormularioMateriaUiState
    data class Erro(val mensagem: String) : FormularioMateriaUiState
}
