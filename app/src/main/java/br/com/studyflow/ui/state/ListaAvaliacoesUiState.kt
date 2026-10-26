package br.com.studyflow.ui.state

import br.com.studyflow.model.AvaliacaoDetalhes
import br.com.studyflow.model.Materia

sealed interface ListaAvaliacoesUiState {
    data object Carregando : ListaAvaliacoesUiState
    data class Sucesso(
        val avaliacoes: List<AvaliacaoDetalhes>,
        val materias: List<Materia>
    ) : ListaAvaliacoesUiState
    data class Erro(val mensagem: String) : ListaAvaliacoesUiState
}
