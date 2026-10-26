package br.com.studyflow.ui.state

import br.com.studyflow.model.MateriaDetalhes

sealed interface ListaMateriasUiState {
    data object Carregando : ListaMateriasUiState
    data class Sucesso(val materias: List<MateriaDetalhes>) : ListaMateriasUiState
    data class Erro(val mensagem: String) : ListaMateriasUiState
}
