package br.com.studyflow.data.repository

import br.com.studyflow.data.local.dao.MateriaDao
import br.com.studyflow.data.local.entity.MateriaEntity
import br.com.studyflow.model.Materia
import br.com.studyflow.model.MateriaDetalhes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MateriaRepository(private val materiaDao: MateriaDao) {

    val materiasEmTempoReal: Flow<List<Materia>> = materiaDao.observarTodasMaterias().map { lista ->
        lista.map { it.toMateria() }
    }

    val materiasComAvaliacoesEmTempoReal: Flow<List<MateriaDetalhes>> =
        materiaDao.observarTodasMateriasCompletas().map { lista ->
            lista.map { it.toMateriaDetalhes() }
        }

    suspend fun salvarMateria(materia: Materia): Long {
        val entity = MateriaEntity.fromMateria(materia)
        return if (entity.id == 0) {
            materiaDao.inserirOuAtualizar(entity)
        } else {
            materiaDao.atualizar(entity)
            entity.id.toLong()
        }
    }

    // As avaliações da matéria são excluídas pelo banco (ON DELETE CASCADE).
    suspend fun removerMateria(materia: Materia) {
        materiaDao.deletar(MateriaEntity.fromMateria(materia))
    }

    suspend fun obterPorId(id: Int): Materia? {
        return materiaDao.buscarPorId(id)?.toMateria()
    }
}
