package br.com.studyflow.model

import kotlin.math.max
import kotlin.math.min

// Regra do TAP: 60% urgência do prazo + 40% impacto da nota.
object Prioridade {

    private const val UM_DIA = 86_400_000.0

    fun calcular(avaliacao: Avaliacao, agora: Long = System.currentTimeMillis()): Int {
        val dias = (avaliacao.dataMillis - agora) / UM_DIA
        val urgencia = when {
            dias <= 0 -> 100.0
            dias <= 1 -> 95.0
            dias <= 3 -> 85.0
            dias <= 7 -> 70.0
            dias <= 14 -> 50.0
            else -> max(10.0, 50.0 - (dias - 14) * 1.5)
        }
        val impacto = min(100.0, max(0.0, avaliacao.peso * 20.0))
        return (urgencia * 0.60 + impacto * 0.40).toInt()
    }

    fun rotulo(pontuacao: Int): String = when {
        pontuacao >= 80 -> "Alta"
        pontuacao >= 55 -> "Média"
        else -> "Baixa"
    }
}
