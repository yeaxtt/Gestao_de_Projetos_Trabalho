package br.com.studyflow.data.dao
import androidx.room.*
import br.com.studyflow.data.model.Avaliacao
import kotlinx.coroutines.flow.Flow

@Dao
interface AvaliacaoDao {
    @Query("SELECT * FROM avaliacoes ORDER BY dataMillis")
    fun observar(): Flow<List<Avaliacao>>
    @Insert suspend fun inserir(avaliacao: Avaliacao): Long
    @Update suspend fun atualizar(avaliacao: Avaliacao)
    @Delete suspend fun excluir(avaliacao: Avaliacao)
    @Query("DELETE FROM avaliacoes WHERE materiaId = :materiaId")
    suspend fun excluirPorMateria(materiaId: Long)
}
