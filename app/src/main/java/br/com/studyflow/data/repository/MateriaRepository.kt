package br.com.studyflow.data.repository

import br.com.studyflow.data.local.dao.MateriaDao
import br.com.studyflow.data.local.entity.MateriaEntity
import br.com.studyflow.model.Materia
import br.com.studyflow.model.MateriaDetalhes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// Todo acesso ao banco roda no Dispatchers.IO, fora da thread principal.
class MateriaRepository(
    private val materiaDao: MateriaDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val materiasEmTempoReal: Flow<List<Materia>> = materiaDao.observarTodasMaterias()
        .map { lista -> lista.map { it.toMateria() } }
        .flowOn(dispatcher)

    val materiasComAvaliacoesEmTempoReal: Flow<List<MateriaDetalhes>> =
        materiaDao.observarTodasMateriasCompletas()
            .map { lista -> lista.map { it.toMateriaDetalhes() } }
            .flowOn(dispatcher)

    suspend fun salvarMateria(materia: Materia): Long = withContext(dispatcher) {
        val entity = MateriaEntity.fromMateria(materia)
        if (entity.id == 0) {
            materiaDao.inserirOuAtualizar(entity)
        } else {
            materiaDao.atualizar(entity)
            entity.id.toLong()
        }
    }

    // As avaliações da matéria são excluídas pelo banco (ON DELETE CASCADE).
    suspend fun removerMateria(materia: Materia) = withContext(dispatcher) {
        materiaDao.deletar(MateriaEntity.fromMateria(materia))
    }

    suspend fun obterPorId(id: Int): Materia? = withContext(dispatcher) {
        materiaDao.buscarPorId(id)?.toMateria()
    }
}
