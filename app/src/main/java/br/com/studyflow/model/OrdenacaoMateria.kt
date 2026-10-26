package br.com.studyflow.model

enum class OrdenacaoMateria(val titulo: String) {
    NOME_ASC("Nome (A-Z)"),
    NOME_DESC("Nome (Z-A)"),
    AVALIACOES_DESC("Mais avaliações"),
    AVALIACOES_ASC("Menos avaliações")
}
