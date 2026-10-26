package br.com.studyflow.network

import br.com.studyflow.model.Materia
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MateriaApiService {

    @GET("materias")
    suspend fun listarMaterias(): List<Materia>

    @POST("materias")
    suspend fun criarMateria(@Body materia: Materia): Materia

    @DELETE("materias/{id}")
    suspend fun excluirMateria(@Path("id") id: Int)
}
