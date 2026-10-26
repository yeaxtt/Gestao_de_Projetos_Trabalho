package br.com.studyflow.data.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.studyflow.data.dao.AvaliacaoDao
import br.com.studyflow.data.dao.MateriaDao
import br.com.studyflow.data.model.Avaliacao
import br.com.studyflow.data.model.Materia

@Database(entities = [Materia::class, Avaliacao::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun materiaDao(): MateriaDao
    abstract fun avaliacaoDao(): AvaliacaoDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun get(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studyflow.db"
                ).build().also { INSTANCE = it }
            }
    }
}
