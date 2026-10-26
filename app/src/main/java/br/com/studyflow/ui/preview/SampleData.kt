package br.com.studyflow.ui.preview

import br.com.studyflow.data.model.Avaliacao
import br.com.studyflow.data.model.Materia
import br.com.studyflow.ui.StudyUiState

/** Dados fictícios usados apenas pelos @Preview. */
object SampleData {
    private const val DAY = 86_400_000L
    private val now = System.currentTimeMillis()

    val materias = listOf(
        Materia(1, "Algoritmos", "Prof. Paulo Mendes"),
        Materia(2, "Banco de Dados", "Prof. Ricardo Souza"),
        Materia(3, "Cálculo II", "Prof. Helena Prado"),
        Materia(4, "Redes de Computadores", "Prof. Marcos Lima")
    )

    val avaliacoes = listOf(
        Avaliacao(1, "Prova de Cálculo II", "Prova", 3, now + 1 * DAY, 5.0),
        Avaliacao(2, "Trabalho de Banco de Dados", "Trabalho", 2, now + 9 * DAY, 3.5),
        Avaliacao(3, "Seminário de Redes", "Seminário", 4, now + 29 * DAY, 2.0),
        Avaliacao(4, "Lista de Algoritmos", "Trabalho", 1, now - 2 * DAY, 1.0, concluida = true)
    )

    val state = StudyUiState(materias, avaliacoes)
}
