package br.com.studyflow.network

import br.com.studyflow.model.Avaliacao
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface AvaliacaoApiService {

    @GET("avaliacoes")
    suspend fun listarAvaliacoes(): List<Avaliacao>

    @POST("avaliacoes")
    suspend fun criarAvaliacao(@Body avaliacao: Avaliacao): Avaliacao

    @PATCH("avaliacoes/{id}/concluida")
    suspend fun definirConcluida(@Path("id") id: Int, @Body corpo: Map<String, Boolean>)

    @DELETE("avaliacoes/{id}")
    suspend fun excluirAvaliacao(@Path("id") id: Int)
}
