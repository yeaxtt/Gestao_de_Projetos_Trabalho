package br.com.studyflow.model

enum class OrdenacaoAvaliacao(val titulo: String) {
    PRIORIDADE_DESC("Maior prioridade"),
    PRAZO_ASC("Prazo mais próximo"),
    PRAZO_DESC("Prazo mais distante"),
    TITULO_ASC("Título (A-Z)"),
    PESO_DESC("Maior peso")
}
