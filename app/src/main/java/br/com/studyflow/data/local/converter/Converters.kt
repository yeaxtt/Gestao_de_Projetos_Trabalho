package br.com.studyflow.data.local.converter

import androidx.room.TypeConverter
import br.com.studyflow.model.TipoAvaliacao

class Converters {

    // Grava o título ("Prova", "Trabalho", "Projeto"...), como na versão 1 do banco.
    @TypeConverter
    fun fromTipoAvaliacao(tipo: TipoAvaliacao): String {
        return tipo.titulo
    }

    @TypeConverter
    fun toTipoAvaliacao(value: String?): TipoAvaliacao {
        return TipoAvaliacao.entries.firstOrNull { it.titulo == value || it.name == value }
            ?: TipoAvaliacao.PROVA
    }
}
