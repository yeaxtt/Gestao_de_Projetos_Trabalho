package br.com.studyflow

import br.com.studyflow.data.model.Avaliacao
import kotlin.math.max
import kotlin.math.min

object Priority {
    // Regra do TAP: 60% urgência + 40% impacto da nota.
    fun score(a: Avaliacao, now: Long = System.currentTimeMillis()): Int {
        val days = ((a.dataMillis - now).toDouble() / 86_400_000.0)
        val urgency = when {
            days <= 0 -> 100.0
            days <= 1 -> 95.0
            days <= 3 -> 85.0
            days <= 7 -> 70.0
            days <= 14 -> 50.0
            else -> max(10.0, 50.0 - (days - 14) * 1.5)
        }
        val impact = min(100.0, max(0.0, a.peso * 20.0))
        return (urgency * 0.60 + impact * 0.40).toInt()
    }
}
