package br.com.studyflow.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import br.com.studyflow.model.OrdenacaoAvaliacao
import br.com.studyflow.model.OrdenacaoMateria

class AppPreferencesManager(context: Context) {

    companion object {
        private const val PREFS_FILE = "studyflow_preferences"
        private const val KEY_ORDENACAO_MATERIA = "pref_ordenacao_materias"
        private const val KEY_ORDENACAO_AVALIACAO = "pref_ordenacao_avaliacoes"
    }

    private val sharedPreferences: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE)

    fun salvarOrdenacaoMateria(ordenacao: OrdenacaoMateria) {
        sharedPreferences.edit { putString(KEY_ORDENACAO_MATERIA, ordenacao.name) }
    }

    fun obterOrdenacaoMateria(): OrdenacaoMateria {
        val nomeEnum = sharedPreferences.getString(KEY_ORDENACAO_MATERIA, OrdenacaoMateria.NOME_ASC.name)
        return try {
            OrdenacaoMateria.valueOf(nomeEnum ?: OrdenacaoMateria.NOME_ASC.name)
        } catch (_: Exception) {
            OrdenacaoMateria.NOME_ASC
        }
    }

    fun salvarOrdenacaoAvaliacao(ordenacao: OrdenacaoAvaliacao) {
        sharedPreferences.edit { putString(KEY_ORDENACAO_AVALIACAO, ordenacao.name) }
    }

    fun obterOrdenacaoAvaliacao(): OrdenacaoAvaliacao {
        val nomeEnum = sharedPreferences.getString(KEY_ORDENACAO_AVALIACAO, OrdenacaoAvaliacao.PRIORIDADE_DESC.name)
        return try {
            OrdenacaoAvaliacao.valueOf(nomeEnum ?: OrdenacaoAvaliacao.PRIORIDADE_DESC.name)
        } catch (_: Exception) {
            OrdenacaoAvaliacao.PRIORIDADE_DESC
        }
    }
}
