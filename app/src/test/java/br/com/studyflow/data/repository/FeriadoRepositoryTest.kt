package br.com.studyflow.data.repository

import br.com.studyflow.model.Feriado
import br.com.studyflow.network.FeriadoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class FeriadoRepositoryTest {

    // API falsa: devolve fora de ordem e registra o ano pedido
    private class ApiFalsa : FeriadoApiService {
        var anoPedido = 0
        override suspend fun listarFeriados(ano: Int): List<Feriado> {
            anoPedido = ano
            return listOf(
                Feriado("$ano-12-25", "Natal", "national"),
                Feriado("$ano-01-01", "Confraternização mundial", "national"),
                Feriado("$ano-11-02", "Finados", "national")
            )
        }
    }

    @Test
    fun feriadosVoltamOrdenadosPorData() = runBlocking {
        val api = ApiFalsa()
        val feriados = FeriadoRepository(api, Dispatchers.Unconfined).obterFeriados(2026)

        assertEquals(2026, api.anoPedido)
        assertEquals(listOf("Confraternização mundial", "Finados", "Natal"), feriados.map { it.nome })
    }
}
