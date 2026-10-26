package br.com.studyflow.data.repository

import br.com.studyflow.data.local.dao.AvaliacaoDao
import br.com.studyflow.data.local.entity.AvaliacaoEntity
import br.com.studyflow.model.Avaliacao
import br.com.studyflow.model.AvaliacaoDetalhes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AvaliacaoRepository(private val avaliacaoDao: AvaliacaoDao) {

    val avaliacoesEmTempoReal: Flow<List<AvaliacaoDetalhes>> =
        avaliacaoDao.observarTodasAvaliacoesCompletas().map { lista ->
            lista.map { it.toAvaliacaoDetalhes() }
        }

    suspend fun salvarAvaliacao(avaliacao: Avaliacao): Int {
        val entity = AvaliacaoEntity.fromAvaliacao(avaliacao)
        return if (entity.id == 0) {
            avaliacaoDao.inserirOuAtualizar(entity).toInt()
        } else {
            avaliacaoDao.atualizar(entity)
            entity.id
        }
    }

    suspend fun alternarConclusao(avaliacao: Avaliacao): Avaliacao {
        val atualizada = avaliacao.copy(concluida = !avaliacao.concluida)
        avaliacaoDao.atualizar(AvaliacaoEntity.fromAvaliacao(atualizada))
        return atualizada
    }

    suspend fun removerAvaliacao(avaliacao: Avaliacao) {
        avaliacaoDao.deletar(AvaliacaoEntity.fromAvaliacao(avaliacao))
    }

    suspend fun obterPorId(id: Int): Avaliacao? {
        return avaliacaoDao.buscarPorId(id)?.toAvaliacao()
    }

    suspend fun obterIdsPorMateria(materiaId: Int): List<Int> {
        return avaliacaoDao.buscarIdsPorMateria(materiaId)
    }
}
