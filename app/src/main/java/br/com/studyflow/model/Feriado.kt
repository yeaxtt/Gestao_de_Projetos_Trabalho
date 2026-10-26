package br.com.studyflow.model

import com.google.gson.annotations.SerializedName

// Feriado nacional retornado pela BrasilAPI (data no formato yyyy-MM-dd).
data class Feriado(
    @SerializedName("date")
    val data: String,
    @SerializedName("name")
    val nome: String,
    @SerializedName("type")
    val tipo: String
)
