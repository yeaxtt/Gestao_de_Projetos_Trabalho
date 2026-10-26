package br.com.studyflow.ui.state

import br.com.studyflow.model.Avaliacao
import br.com.studyflow.model.Materia

sealed interface FormularioAvaliacaoUiState {
    data object Ocioso : FormularioAvaliacaoUiState
    data object Carregando : FormularioAvaliacaoUiState
    data class Sucesso(
        val avaliacao: Avaliacao?,
        val materias: List<Materia>
    ) : FormularioAvaliacaoUiState
    data class Erro(val mensagem: String) : FormularioAvaliacaoUiState
}
