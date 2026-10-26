package br.com.studyflow.model

import com.google.gson.annotations.SerializedName

data class Materia(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("nome")
    val nome: String = "",
    @SerializedName("professor")
    val professor: String = ""
)
