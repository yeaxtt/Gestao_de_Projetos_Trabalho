package br.com.studyflow.data.repository
import br.com.studyflow.data.dao.AvaliacaoDao
import br.com.studyflow.data.dao.MateriaDao
import br.com.studyflow.data.model.Avaliacao
import br.com.studyflow.data.model.Materia

class StudyRepository(
    private val materiaDao: MateriaDao,
    private val avaliacaoDao: AvaliacaoDao
) {
    val materias = materiaDao.observar()
    val avaliacoes = avaliacaoDao.observar()
    suspend fun inserirMateria(nome: String, professor: String) =
        materiaDao.inserir(Materia(nome = nome, professor = professor))
    suspend fun atualizarMateria(m: Materia) = materiaDao.atualizar(m)
    suspend fun excluirMateria(m: Materia) {
        avaliacaoDao.excluirPorMateria(m.id)
        materiaDao.excluir(m)
    }
    suspend fun inserirAvaliacao(a: Avaliacao) = avaliacaoDao.inserir(a)
    suspend fun atualizarAvaliacao(a: Avaliacao) = avaliacaoDao.atualizar(a)
    suspend fun concluir(a: Avaliacao) = avaliacaoDao.atualizar(a.copy(concluida = !a.concluida))
    suspend fun excluirAvaliacao(a: Avaliacao) = avaliacaoDao.excluir(a)
}
