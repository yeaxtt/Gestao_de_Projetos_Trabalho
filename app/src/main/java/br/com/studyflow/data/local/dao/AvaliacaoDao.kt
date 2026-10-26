package br.com.studyflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.studyflow.data.local.entity.AvaliacaoComMateria
import br.com.studyflow.data.local.entity.AvaliacaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AvaliacaoDao {

    @Transaction
    @Query("SELECT * FROM avaliacoes ORDER BY data_millis ASC")
    fun observarTodasAvaliacoesCompletas(): Flow<List<AvaliacaoComMateria>>

    @Query("SELECT * FROM avaliacoes WHERE id = :id")
    suspend fun buscarPorId(id: Int): AvaliacaoEntity?

    @Query("SELECT id FROM avaliacoes WHERE materia_id = :materiaId")
    suspend fun buscarIdsPorMateria(materiaId: Int): List<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOuAtualizar(avaliacao: AvaliacaoEntity): Long

    @Update
    suspend fun atualizar(avaliacao: AvaliacaoEntity)

    @Delete
    suspend fun deletar(avaliacao: AvaliacaoEntity)
}
