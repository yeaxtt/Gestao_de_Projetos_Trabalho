package br.com.studyflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import br.com.studyflow.model.Avaliacao
import br.com.studyflow.model.TipoAvaliacao

@Entity(
    tableName = "avaliacoes",
    foreignKeys = [
        ForeignKey(
            entity = MateriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["materia_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["materia_id"])]
)
data class AvaliacaoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "titulo")
    val titulo: String,

    @ColumnInfo(name = "tipo")
    val tipo: TipoAvaliacao,

    @ColumnInfo(name = "materia_id")
    val materiaId: Int,

    @ColumnInfo(name = "data_millis")
    val dataMillis: Long,

    @ColumnInfo(name = "peso")
    val peso: Double,

    @ColumnInfo(name = "concluida")
    val concluida: Boolean
) {
    fun toAvaliacao(): Avaliacao = Avaliacao(
        id = id,
        titulo = titulo,
        tipo = tipo,
        materiaId = materiaId,
        dataMillis = dataMillis,
        peso = peso,
        concluida = concluida
    )

    companion object {
        fun fromAvaliacao(avaliacao: Avaliacao): AvaliacaoEntity = AvaliacaoEntity(
            id = avaliacao.id,
            titulo = avaliacao.titulo,
            tipo = avaliacao.tipo,
            materiaId = avaliacao.materiaId,
            dataMillis = avaliacao.dataMillis,
            peso = avaliacao.peso,
            concluida = avaliacao.concluida
        )
    }
}
