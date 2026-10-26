package br.com.studyflow.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // No emulador Android, 10.0.2.2 aponta para o localhost da máquina host (API em backend/).
    // Em dispositivo físico, substitua pelo IP da máquina na rede local.
    private const val BASE_URL = "http://10.0.2.2:3000/api/"

    // API pública usada pela tela de feriados
    private const val BRASIL_API_URL = "https://brasilapi.com.br/api/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val materiaApiService: MateriaApiService = retrofit.create(MateriaApiService::class.java)
    val avaliacaoApiService: AvaliacaoApiService = retrofit.create(AvaliacaoApiService::class.java)

    private val brasilApi = Retrofit.Builder()
        .baseUrl(BRASIL_API_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val feriadoApiService: FeriadoApiService = brasilApi.create(FeriadoApiService::class.java)
}
