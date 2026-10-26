package br.com.studyflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.studyflow.data.local.converter.Converters
import br.com.studyflow.data.local.dao.AvaliacaoDao
import br.com.studyflow.data.local.dao.MateriaDao
import br.com.studyflow.data.local.entity.AvaliacaoEntity
import br.com.studyflow.data.local.entity.MateriaEntity

@Database(
    entities = [MateriaEntity::class, AvaliacaoEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun materiaDao(): MateriaDao
    abstract fun avaliacaoDao(): AvaliacaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Versão 1 -> 2: colunas de avaliacoes em snake_case e chave estrangeira para materias,
        // para que excluir uma matéria exclua as avaliações dela (ON DELETE CASCADE).
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `avaliacoes_nova` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `titulo` TEXT NOT NULL,
                        `tipo` TEXT NOT NULL,
                        `materia_id` INTEGER NOT NULL,
                        `data_millis` INTEGER NOT NULL,
                        `peso` REAL NOT NULL,
                        `concluida` INTEGER NOT NULL,
                        FOREIGN KEY(`materia_id`) REFERENCES `materias`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                    INSERT INTO `avaliacoes_nova` (`id`, `titulo`, `tipo`, `materia_id`, `data_millis`, `peso`, `concluida`)
                    SELECT `id`, `titulo`, `tipo`, `materiaId`, `dataMillis`, `peso`, `concluida` FROM `avaliacoes`
                    WHERE `materiaId` IN (SELECT `id` FROM `materias`)
                    """.trimIndent()
                )
                db.execSQL("DROP TABLE `avaliacoes`")
                db.execSQL("ALTER TABLE `avaliacoes_nova` RENAME TO `avaliacoes`")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_avaliacoes_materia_id` ON `avaliacoes` (`materia_id`)")
            }
        }

        fun obterInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "studyflow.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration(false)
                    .build()
                INSTANCE = instancia
                return instancia
            }
        }
    }
}
