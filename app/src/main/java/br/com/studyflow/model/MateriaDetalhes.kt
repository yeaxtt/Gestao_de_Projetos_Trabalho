package br.com.studyflow.model

data class MateriaDetalhes(
    val materia: Materia,
    val avaliacoes: List<Avaliacao> = emptyList()
)
