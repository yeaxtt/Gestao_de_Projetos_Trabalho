package br.com.studyflow.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation
import br.com.studyflow.model.AvaliacaoDetalhes

data class AvaliacaoComMateria(
    @Embedded
    val avaliacao: AvaliacaoEntity,

    @Relation(
        parentColumn = "materia_id",
        entityColumn = "id"
    )
    val materia: MateriaEntity?
) {
    fun toAvaliacaoDetalhes(): AvaliacaoDetalhes = AvaliacaoDetalhes(
        avaliacao = avaliacao.toAvaliacao(),
        materia = materia?.toMateria()
    )
}
