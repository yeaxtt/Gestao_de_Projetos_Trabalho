package br.com.studyflow.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "materias")
data class Materia(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nome: String,
    val professor: String = ""
)

@Entity(tableName = "avaliacoes")
data class Avaliacao(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val titulo: String,
    val tipo: String,
    val materiaId: Long,
    val dataMillis: Long,
    val peso: Double,
    val concluida: Boolean = false
)
