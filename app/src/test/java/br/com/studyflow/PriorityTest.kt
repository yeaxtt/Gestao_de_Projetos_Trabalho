package br.com.studyflow

import br.com.studyflow.data.model.Avaliacao
import org.junit.Assert.assertTrue
import org.junit.Test

class PriorityTest {
    @Test fun avaliacaoMaisUrgenteDeveTerPrioridadeMaior() {
        val now = 1_000_000L
        val urgente = Avaliacao(1,"Hoje","Prova",1,now,5.0)
        val distante = Avaliacao(2,"Depois","Prova",1,now + 20*86_400_000L,5.0)
        assertTrue(Priority.score(urgente, now) > Priority.score(distante, now))
    }

    @Test fun pesoMaiorAumentaPrioridadeComMesmoPrazo() {
        val now = 1_700_000_000_000L
        val date = now + 7 * 86_400_000L
        val low = Avaliacao(1,"A","Prova",1,date,1.0)
        val high = low.copy(peso=5.0)
        assertTrue(Priority.score(high, now) > Priority.score(low, now))
    }

    @Test fun pontuacaoPermaneceEntreZeroECem() {
        val now = 1_700_000_000_000L
        val evaluation = Avaliacao(1,"A","Prova",1,now,5.0)
        assertTrue(Priority.score(evaluation, now) in 0..100)
    }
}
