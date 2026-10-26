package br.com.studyflow.data.dao
import androidx.room.*
import br.com.studyflow.data.model.Materia
import kotlinx.coroutines.flow.Flow

@Dao
interface MateriaDao {
    @Query("SELECT * FROM materias ORDER BY nome")
    fun observar(): Flow<List<Materia>>
    @Insert suspend fun inserir(materia: Materia): Long
    @Update suspend fun atualizar(materia: Materia)
    @Delete suspend fun excluir(materia: Materia)
}
