package br.com.studyflow.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.studyflow.model.Materia

@Entity(tableName = "materias")
data class MateriaEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "professor")
    val professor: String
) {
    fun toMateria(): Materia = Materia(
        id = id,
        nome = nome,
        professor = professor
    )

    companion object {
        fun fromMateria(materia: Materia): MateriaEntity = MateriaEntity(
            id = materia.id,
            nome = materia.nome,
            professor = materia.professor
        )
    }
}
