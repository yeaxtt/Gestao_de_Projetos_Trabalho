package br.com.studyflow.notifications

import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.studyflow.model.Avaliacao
import java.util.concurrent.TimeUnit

// Programa lembretes locais 7, 3 e 1 dia antes do prazo de uma avaliação.
// Recebe o WorkManager pronto, assim a ViewModel não precisa de Context.
class AgendadorLembretes(private val workManager: WorkManager) {

    private val diasAntes = listOf(7, 3, 1)

    fun agendar(avaliacao: Avaliacao) {
        diasAntes.forEach { dias ->
            val disparoEm = avaliacao.dataMillis - TimeUnit.DAYS.toMillis(dias.toLong())
            val atraso = disparoEm - System.currentTimeMillis()
            if (atraso > 0) {
                val dados = Data.Builder()
                    .putString(LembreteWorker.CHAVE_TITULO, avaliacao.titulo)
                    .putInt(LembreteWorker.CHAVE_DIAS, dias)
                    .putInt(LembreteWorker.CHAVE_ID_NOTIFICACAO, "${avaliacao.id}-$dias".hashCode())
                    .build()
                val pedido = OneTimeWorkRequestBuilder<LembreteWorker>()
                    .setInitialDelay(atraso, TimeUnit.MILLISECONDS)
                    .setInputData(dados)
                    .addTag(tag(avaliacao.id))
                    .build()
                workManager.enqueue(pedido)
            }
        }
    }

    fun cancelar(avaliacaoId: Int) {
        workManager.cancelAllWorkByTag(tag(avaliacaoId))
    }

    // Prazo ou título podem ter mudado: refaz os lembretes; concluída não tem lembrete.
    fun reagendar(avaliacao: Avaliacao) {
        cancelar(avaliacao.id)
        if (!avaliacao.concluida) agendar(avaliacao)
    }

    private fun tag(id: Int) = "avaliacao-$id"
}
