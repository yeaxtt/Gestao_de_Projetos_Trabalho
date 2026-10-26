package br.com.studyflow.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import br.com.studyflow.model.MateriaDetalhes

data class MateriaComAvaliacoes(
    @Embedded
    val materia: MateriaEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "materia_id"
    )
    val avaliacoes: List<AvaliacaoEntity> = emptyList()
) {
    fun toMateriaDetalhes(): MateriaDetalhes = MateriaDetalhes(
        materia = materia.toMateria(),
        avaliacoes = avaliacoes.map { it.toAvaliacao() }
    )
}
