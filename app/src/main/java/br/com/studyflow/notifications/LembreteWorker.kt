package br.com.studyflow.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class LembreteWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        criarCanal()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) return Result.success()

        val titulo = inputData.getString(CHAVE_TITULO) ?: return Result.failure()
        val dias = inputData.getInt(CHAVE_DIAS, 0)
        val mensagem = if (dias == 1) "$titulo vence amanhã" else "$titulo vence em $dias dias"
        val notificacao = NotificationCompat.Builder(applicationContext, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Lembrete do StudyFlow")
            .setContentText(mensagem)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(applicationContext)
            .notify(inputData.getInt(CHAVE_ID_NOTIFICACAO, titulo.hashCode()), notificacao)
        return Result.success()
    }

    private fun criarCanal() {
        val canal = NotificationChannel(
            CANAL_ID,
            "Prazos de avaliações",
            NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Lembretes de avaliações próximas" }
        applicationContext.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(canal)
    }

    companion object {
        const val CANAL_ID = "studyflow_deadlines"
        const val CHAVE_TITULO = "title"
        const val CHAVE_DIAS = "days"
        const val CHAVE_ID_NOTIFICACAO = "notification_id"
    }
}
