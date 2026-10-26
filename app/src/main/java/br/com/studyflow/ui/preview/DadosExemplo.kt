package br.com.studyflow.ui.preview

import br.com.studyflow.model.Avaliacao
import br.com.studyflow.model.AvaliacaoDetalhes
import br.com.studyflow.model.Materia
import br.com.studyflow.model.MateriaDetalhes
import br.com.studyflow.model.TipoAvaliacao

// Dados fictícios usados apenas pelos @Preview.
object DadosExemplo {
    private const val DIA = 86_400_000L
    private val agora = System.currentTimeMillis()

    val materias = listOf(
        Materia(1, "Algoritmos", "Prof. Paulo Mendes"),
        Materia(2, "Banco de Dados", "Prof. Ricardo Souza"),
        Materia(3, "Cálculo II", "Prof. Helena Prado"),
        Materia(4, "Redes de Computadores", "Prof. Marcos Lima")
    )

    val avaliacoes = listOf(
        Avaliacao(1, "Prova de Cálculo II", TipoAvaliacao.PROVA, 3, agora + 1 * DIA, 5.0),
        Avaliacao(2, "Trabalho de Banco de Dados", TipoAvaliacao.TRABALHO, 2, agora + 9 * DIA, 3.5),
        Avaliacao(3, "Seminário de Redes", TipoAvaliacao.SEMINARIO, 4, agora + 29 * DIA, 2.0),
        Avaliacao(4, "Lista de Algoritmos", TipoAvaliacao.TRABALHO, 1, agora - 2 * DIA, 1.0, concluida = true)
    )

    val avaliacoesDetalhes = avaliacoes.map { a -> AvaliacaoDetalhes(a, materias.firstOrNull { it.id == a.materiaId }) }

    val materiasDetalhes = materias.map { m -> MateriaDetalhes(m, avaliacoes.filter { it.materiaId == m.id }) }
}
