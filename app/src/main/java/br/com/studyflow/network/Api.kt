package br.com.studyflow.network
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

data class MateriaDto(val id: Long? = null, val nome: String, val professor: String = "")
data class AvaliacaoDto(
    val id: Long? = null, val titulo: String, val tipo: String,
    val materiaId: Long, val dataMillis: Long, val peso: Double, val concluida: Boolean = false
)
interface StudyApi {
    @GET("api/materias") suspend fun materias(): List<MateriaDto>
    @POST("api/materias") suspend fun criarMateria(@Body body: MateriaDto): MateriaDto
    @GET("api/avaliacoes") suspend fun avaliacoes(): List<AvaliacaoDto>
    @POST("api/avaliacoes") suspend fun criarAvaliacao(@Body body: AvaliacaoDto): AvaliacaoDto
    @PATCH("api/avaliacoes/{id}/concluida")
    suspend fun definirConcluida(@Path("id") id: Long, @Body body: Map<String, Boolean>)
    @DELETE("api/avaliacoes/{id}") suspend fun excluirAvaliacao(@Path("id") id: Long)
    @DELETE("api/materias/{id}") suspend fun excluirMateria(@Path("id") id: Long)
}
object ApiFactory {
    fun create(baseUrl: String): StudyApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(StudyApi::class.java)
}
