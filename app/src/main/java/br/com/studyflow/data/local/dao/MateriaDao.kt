package br.com.studyflow.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import br.com.studyflow.data.local.entity.MateriaComAvaliacoes
import br.com.studyflow.data.local.entity.MateriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriaDao {

    @Query("SELECT * FROM materias ORDER BY nome ASC")
    fun observarTodasMaterias(): Flow<List<MateriaEntity>>

    @Transaction
    @Query("SELECT * FROM materias ORDER BY nome ASC")
    fun observarTodasMateriasCompletas(): Flow<List<MateriaComAvaliacoes>>

    @Query("SELECT * FROM materias WHERE id = :id")
    suspend fun buscarPorId(id: Int): MateriaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserirOuAtualizar(materia: MateriaEntity): Long

    @Update
    suspend fun atualizar(materia: MateriaEntity)

    @Delete
    suspend fun deletar(materia: MateriaEntity)
}
