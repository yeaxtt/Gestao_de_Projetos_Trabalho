package br.com.studyflow.data.repository

import br.com.studyflow.data.local.dao.AvaliacaoDao
import br.com.studyflow.data.local.entity.AvaliacaoEntity
import br.com.studyflow.model.Avaliacao
import br.com.studyflow.model.AvaliacaoDetalhes
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

// Todo acesso ao banco roda no Dispatchers.IO, fora da thread principal.
class AvaliacaoRepository(
    private val avaliacaoDao: AvaliacaoDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    val avaliacoesEmTempoReal: Flow<List<AvaliacaoDetalhes>> =
        avaliacaoDao.observarTodasAvaliacoesCompletas()
            .map { lista -> lista.map { it.toAvaliacaoDetalhes() } }
            .flowOn(dispatcher)

    suspend fun salvarAvaliacao(avaliacao: Avaliacao): Int = withContext(dispatcher) {
        val entity = AvaliacaoEntity.fromAvaliacao(avaliacao)
        if (entity.id == 0) {
            avaliacaoDao.inserirOuAtualizar(entity).toInt()
        } else {
            avaliacaoDao.atualizar(entity)
            entity.id
        }
    }

    suspend fun alternarConclusao(avaliacao: Avaliacao): Avaliacao = withContext(dispatcher) {
        val atualizada = avaliacao.copy(concluida = !avaliacao.concluida)
        avaliacaoDao.atualizar(AvaliacaoEntity.fromAvaliacao(atualizada))
        atualizada
    }

    suspend fun removerAvaliacao(avaliacao: Avaliacao) = withContext(dispatcher) {
        avaliacaoDao.deletar(AvaliacaoEntity.fromAvaliacao(avaliacao))
    }

    suspend fun obterPorId(id: Int): Avaliacao? = withContext(dispatcher) {
        avaliacaoDao.buscarPorId(id)?.toAvaliacao()
    }

    suspend fun obterIdsPorMateria(materiaId: Int): List<Int> = withContext(dispatcher) {
        avaliacaoDao.buscarIdsPorMateria(materiaId)
    }
}
