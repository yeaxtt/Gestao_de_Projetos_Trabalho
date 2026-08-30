package com.example.trabalho_gerenciamento_de_projetos.data.model

import java.time.LocalDate

enum class AssessmentType { PROVA, TRABALHO, SEMINARIO }

enum class Priority { URGENTE, ALTA, MEDIA, BAIXA }

data class Assessment(
    val id: Long,
    val subjectId: Long,
    val subjectName: String,
    val subjectColor: String,
    val title: String,
    val type: AssessmentType,
    val dueDate: LocalDate,
    val weight: Double,
    val description: String = "",
    val completed: Boolean = false
) {
    val daysUntilDue: Long
        get() = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate)

    val priority: Priority
        get() = when {
            completed -> Priority.BAIXA
            daysUntilDue < 0 -> Priority.URGENTE
            daysUntilDue <= 2 && weight >= 25 -> Priority.URGENTE
            daysUntilDue <= 5 && weight >= 30 -> Priority.ALTA
            daysUntilDue <= 7 -> Priority.ALTA
            weight >= 40 -> Priority.ALTA
            else -> Priority.MEDIA
        }
}
