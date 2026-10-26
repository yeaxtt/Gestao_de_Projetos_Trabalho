package br.com.studyflow.data.repository

import br.com.studyflow.model.Feriado
import br.com.studyflow.network.FeriadoApiService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Busca os feriados nacionais na BrasilAPI; a chamada de rede roda no Dispatchers.IO.
class FeriadoRepository(
    private val apiService: FeriadoApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun obterFeriados(ano: Int): List<Feriado> = withContext(dispatcher) {
        apiService.listarFeriados(ano).sortedBy { it.data }
    }
}
