package br.com.studyflow.ui.navigation

import kotlinx.serialization.Serializable

sealed interface AppRoute {

    // Agrupamento de Telas: Painel
    @Serializable
    data object Painel : AppRoute

    // Agrupamento de Telas: Matérias
    @Serializable
    data object MateriasLista : AppRoute

    @Serializable
    data class MateriaFormulario(val materiaId: Int = 0) : AppRoute

    // Agrupamento de Telas: Avaliações
    @Serializable
    data object AvaliacoesLista : AppRoute

    @Serializable
    data class AvaliacaoFormulario(val avaliacaoId: Int = 0) : AppRoute

    // Agrupamento de Telas: Feriados (consumo da BrasilAPI)
    @Serializable
    data object Feriados : AppRoute
}
