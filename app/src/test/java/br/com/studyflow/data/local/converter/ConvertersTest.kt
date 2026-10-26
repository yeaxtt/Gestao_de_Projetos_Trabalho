package br.com.studyflow.data.local.converter

import br.com.studyflow.model.TipoAvaliacao
import org.junit.Assert.assertEquals
import org.junit.Test

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun gravaOTituloDoTipo() {
        assertEquals("Seminário", converters.fromTipoAvaliacao(TipoAvaliacao.SEMINARIO))
    }

    @Test
    fun leOTituloGravadoNaVersao1DoBanco() {
        assertEquals(TipoAvaliacao.TRABALHO, converters.toTipoAvaliacao("Trabalho"))
        assertEquals(TipoAvaliacao.SEMINARIO, converters.toTipoAvaliacao("Seminário"))
    }

    @Test
    fun todosOsTiposVoltamIguaisDepoisDeGravados() {
        TipoAvaliacao.entries.forEach { tipo ->
            assertEquals(tipo, converters.toTipoAvaliacao(converters.fromTipoAvaliacao(tipo)))
        }
    }

    @Test
    fun valorDesconhecidoViraProva() {
        assertEquals(TipoAvaliacao.PROVA, converters.toTipoAvaliacao("Exame"))
        assertEquals(TipoAvaliacao.PROVA, converters.toTipoAvaliacao(null))
    }
}
