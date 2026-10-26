package br.com.studyflow.ui.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class FormatadoresTest {

    private fun dia(ano: Int, mes: Int, dia: Int, hora: Int) = Calendar.getInstance().apply {
        set(ano, mes - 1, dia, hora, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    @Test
    fun contaDiasDeCalendarioIndependenteDaHora() {
        val agora = dia(2026, 10, 25, 23)
        assertEquals(1L, diasAtePrazo(dia(2026, 10, 26, 9), agora))
        assertEquals(0L, diasAtePrazo(dia(2026, 10, 25, 9), agora))
        assertEquals(-3L, diasAtePrazo(dia(2026, 10, 22, 9), agora))
    }

    @Test
    fun descreveOPrazoParaOEstudante() {
        assertEquals("Atrasada há 3 dias", descreverPrazo(-3))
        assertEquals("Atrasada há 1 dia", descreverPrazo(-1))
        assertEquals("Vence hoje", descreverPrazo(0))
        assertEquals("Vence amanhã", descreverPrazo(1))
        assertEquals("Faltam 7 dias", descreverPrazo(7))
    }
}
