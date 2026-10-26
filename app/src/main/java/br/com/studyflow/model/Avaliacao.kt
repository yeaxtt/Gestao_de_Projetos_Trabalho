package br.com.studyflow.model

import com.google.gson.annotations.SerializedName

data class Avaliacao(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("titulo")
    val titulo: String = "",
    @SerializedName("tipo")
    val tipo: TipoAvaliacao = TipoAvaliacao.PROVA,
    @SerializedName("materiaId")
    val materiaId: Int = 0,
    @SerializedName("dataMillis")
    val dataMillis: Long = 0L,
    @SerializedName("peso")
    val peso: Double = 0.0,
    @SerializedName("concluida")
    val concluida: Boolean = false
)
