package com.example.trabalho_gerenciamento_de_projetos

data class Subject(
    val id: Long,
    val name: String,
    val teacher: String = "",
    val color: String = "#3F51B5"
)
