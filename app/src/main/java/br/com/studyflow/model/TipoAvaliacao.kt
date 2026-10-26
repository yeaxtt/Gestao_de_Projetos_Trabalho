package br.com.studyflow.model

import com.google.gson.annotations.SerializedName

enum class TipoAvaliacao(val titulo: String) {
    @SerializedName("Prova")
    PROVA("Prova"),

    @SerializedName("Trabalho")
    TRABALHO("Trabalho"),

    @SerializedName("Seminário")
    SEMINARIO("Seminário")
}
