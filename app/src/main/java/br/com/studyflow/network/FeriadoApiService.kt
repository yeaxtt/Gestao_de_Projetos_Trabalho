package br.com.studyflow.network

import br.com.studyflow.model.Feriado
import retrofit2.http.GET
import retrofit2.http.Path

// BrasilAPI: https://brasilapi.com.br/docs#tag/Feriados-Nacionais
interface FeriadoApiService {

    @GET("feriados/v1/{ano}")
    suspend fun listarFeriados(@Path("ano") ano: Int): List<Feriado>
}
