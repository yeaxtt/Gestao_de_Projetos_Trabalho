package br.com.studyflow.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PrioridadeTest {

    private val agora = 1_700_000_000_000L
    private val umDia = 86_400_000L

    @Test
    fun avaliacaoMaisUrgenteDeveTerPrioridadeMaior() {
        val urgente = Avaliacao(1, "Hoje", TipoAvaliacao.PROVA, 1, agora, 5.0)
        val distante = Avaliacao(2, "Depois", TipoAvaliacao.PROVA, 1, agora + 20 * umDia, 5.0)
        assertTrue(Prioridade.calcular(urgente, agora) > Prioridade.calcular(distante, agora))
    }

    @Test
    fun pesoMaiorAumentaPrioridadeComMesmoPrazo() {
        val baixo = Avaliacao(1, "A", TipoAvaliacao.PROVA, 1, agora + 7 * umDia, 1.0)
        val alto = baixo.copy(peso = 5.0)
        assertTrue(Prioridade.calcular(alto, agora) > Prioridade.calcular(baixo, agora))
    }

    @Test
    fun pontuacaoPermaneceEntreZeroECem() {
        val avaliacao = Avaliacao(1, "A", TipoAvaliacao.PROVA, 1, agora, 5.0)
        assertTrue(Prioridade.calcular(avaliacao, agora) in 0..100)
    }

    @Test
    fun prazoVencidoComPesoMaximoDaCem() {
        val avaliacao = Avaliacao(1, "A", TipoAvaliacao.PROVA, 1, agora - umDia, 5.0)
        assertEquals(100, Prioridade.calcular(avaliacao, agora))
        assertEquals("Alta", Prioridade.rotulo(100))
    }

    @Test
    fun rotuloSegueAsFaixas() {
        assertEquals("Alta", Prioridade.rotulo(80))
        assertEquals("Média", Prioridade.rotulo(55))
        assertEquals("Baixa", Prioridade.rotulo(54))
    }
}
