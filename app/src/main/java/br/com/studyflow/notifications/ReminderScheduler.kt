package br.com.studyflow.notifications

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import br.com.studyflow.data.model.Avaliacao
import java.util.concurrent.TimeUnit
import kotlin.math.max

object ReminderScheduler {
    private val reminderDays = listOf(7, 3, 1)

    fun schedule(context: Context, avaliacao: Avaliacao) {
        val manager = WorkManager.getInstance(context)
        reminderDays.forEach { daysBefore ->
            val triggerAt = avaliacao.dataMillis - TimeUnit.DAYS.toMillis(daysBefore.toLong())
            val delay = triggerAt - System.currentTimeMillis()
            if (delay > 0) {
                val data = Data.Builder()
                    .putString(ReminderWorker.KEY_TITLE, avaliacao.titulo)
                    .putInt(ReminderWorker.KEY_DAYS, daysBefore)
                    .putInt(ReminderWorker.KEY_NOTIFICATION_ID, "${avaliacao.id}-$daysBefore".hashCode())
                    .build()
                val request = OneTimeWorkRequestBuilder<ReminderWorker>()
                    .setInitialDelay(max(0, delay), TimeUnit.MILLISECONDS)
                    .setInputData(data)
                    .addTag(tag(avaliacao.id))
                    .build()
                manager.enqueue(request)
            }
        }
    }

    fun cancel(context: Context, avaliacaoId: Long) {
        WorkManager.getInstance(context).cancelAllWorkByTag(tag(avaliacaoId))
    }

    private fun tag(id: Long) = "avaliacao-$id"
}
