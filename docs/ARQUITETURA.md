# Arquitetura do StudyFlow

## Base: o projeto das aulas

A estrutura do app segue o projeto `CadastroAlunos`, desenvolvido nas aulas 06 a 08 de Programação para Dispositivos Móveis ([repositório do professor](https://github.com/joaopichetti/afya-programacao-aplicacoes-dispositivos-moveis-2026-02/tree/main/aula-06/CadastroAlunos)). As mesmas camadas, nomes de pacotes, padrões e versões de bibliotecas foram aplicados ao domínio do StudyFlow:

| No projeto das aulas | No StudyFlow |
|---|---|
| `model/Disciplina`, `model/Turma` | `model/Materia`, `model/Avaliacao` |
| `model/TurmaDetalhes` | `model/MateriaDetalhes`, `model/AvaliacaoDetalhes` |
| `model/OrdenacaoDisciplina` | `model/OrdenacaoMateria`, `model/OrdenacaoAvaliacao` |
| `data/local/entity/DisciplinaEntity` com `toDisciplina()` e `fromDisciplina()` | `MateriaEntity`, `AvaliacaoEntity` com `toX()` e `fromX()` |
| `TurmaEntity` com `ForeignKey` e `ON DELETE CASCADE` | `AvaliacaoEntity` → `MateriaEntity` com `ON DELETE CASCADE` |
| `TurmaComDisciplinaEAlunos` (`@Embedded` + `@Relation`) | `MateriaComAvaliacoes`, `AvaliacaoComMateria` |
| `data/local/converter/Converters` | `Converters` (`TipoAvaliacao` ↔ texto) |
| `data/local/dao/DisciplinaDao` (`observarTodas…`, `buscarPorId`, `inserirOuAtualizar`, `atualizar`, `deletar`) | `MateriaDao`, `AvaliacaoDao`, com os mesmos nomes de métodos |
| `AppDatabase.obterInstancia()` com `MIGRATION_1_2` | `AppDatabase.obterInstancia()` com `MIGRATION_1_2` |
| `data/local/preferences/AppPreferencesManager` | `AppPreferencesManager` (ordenação das listas) |
| `data/repository/DisciplinaRepository` (`…EmTempoReal`, `salvar…`, `remover…`, `obterPorId`) | `MateriaRepository`, `AvaliacaoRepository` |
| `network/RetrofitClient` + `AlunoApiService` | `RetrofitClient` + `FeriadoApiService` (BrasilAPI), `MateriaApiService`, `AvaliacaoApiService` |
| `ui/state/ListaDisciplinasUiState`, `FormularioDisciplinaUiState`, `SalvarDisciplinaUiState`, `ExclusaoDisciplinaUiState` | os mesmos quatro estados para Matéria e Avaliação, mais `PainelUiState` e `FeriadosUiState` |
| `ui/viewmodel/DisciplinaViewModel` (`combine` com a ordenação) | `MateriaViewModel`, `AvaliacaoViewModel`, `PainelViewModel`, `FeriadoViewModel`, adaptadas ao barema do trabalho: herdam de `ViewModel` sem `Context`, expõem `StateFlow` e são criadas por `ViewModelProvider.Factory` |
| `ui/navigation/AppRoute` (rotas `@Serializable`) + `AppNavHost` com `NavigationBar` | `AppRoute` + `AppNavHost` (Início, Matérias, Avaliações, Feriados) |
| `ui/telas/ListaDisciplinasScreen`, `FormularioDisciplinaScreen`, `ConfirmacaoExclusaoDisciplinaDialog` | `ListaMateriasScreen`, `FormularioMateriaScreen`, `ConfirmacaoExclusaoMateriaDialog` (e o mesmo para avaliações), `PainelScreen` |
| `gradle/libs.versions.toml`, AGP 9.4, Kotlin 2.2.10, SDK 37 | iguais, mais o WorkManager |

O que o StudyFlow tem além do projeto das aulas: a regra de prioridade (`model/Prioridade`), o painel, os filtros por status e matéria, os lembretes com WorkManager (`notifications/`), a aba de feriados com a BrasilAPI e o visual do protótipo no Figma (`ui/theme`, `ui/components`).

### Diferenças em relação ao projeto das aulas (exigidas pelo barema)

- **ViewModel sem Context.** O projeto das aulas usa `AndroidViewModel`. Aqui as ViewModels herdam de `ViewModel` e recebem repositórios, `AppPreferencesManager` e `AgendadorLembretes` pelo construtor. O `Context` só aparece na `Factory`, para montar essas dependências.
- **Estado em StateFlow.** Em vez de `mutableStateOf` com `private set`, cada estado é um `MutableStateFlow` privado exposto como `StateFlow` (`asStateFlow()`). O `AppNavHost` coleta com `collectAsStateWithLifecycle()`.
- **Dispatchers.IO explícito.** Os repositórios executam o Room e a rede com `withContext(Dispatchers.IO)` e `flowOn(Dispatchers.IO)`.
- **Diálogos tratados na ViewModel.** O item aguardando exclusão (`materiaParaExcluir`, `avaliacaoParaExcluir`) e a visibilidade do diálogo de descarte (`descarteVisivel`) ficam na ViewModel, com as funções `solicitar…`, `confirmar…` e `cancelar…`.

## Fluxo de dados

```text
Tela (Compose)
    ↓ ações (onSalvarClick, onExcluirClick…)   ↑ estados (ListaMateriasUiState…)
ViewModel
    ↓
Repository  ── converte Entity ↔ modelo
    ↓
DAO (Room)  ── consultas com Flow: a tela se atualiza sozinha quando o banco muda
    ↓
SQLite local (studyflow.db)
```

- As telas não conhecem o ViewModel: recebem estados e funções pelo `AppNavHost`, como no projeto das aulas.
- Os campos dos formulários usam `rememberSaveable` e sobrevivem à rotação da tela. Se houver alteração não salva, a seta da barra superior e o voltar do sistema (`BackHandler`) abrem o diálogo "Descartar alterações?".
- Cada formulário é uma tela própria, aberta com `AppRoute.MateriaFormulario(materiaId)` ou `AppRoute.AvaliacaoFormulario(avaliacaoId)`; o id 0 abre o cadastro e outro id abre a edição.
- O WorkManager agenda lembretes 7, 3 e 1 dia antes do prazo. Ao concluir, editar ou excluir, eles são refeitos ou cancelados.

## Banco de dados

- **Versão 2.** A versão 1 (StudyFlow V1.1) guardava `materiaId` e `dataMillis` sem chave estrangeira. A `MIGRATION_1_2` cria a nova tabela `avaliacoes` com colunas em snake_case (`materia_id`, `data_millis`) e `ON DELETE CASCADE`, copia os dados e descarta só avaliações sem matéria. Quem atualizar o app não perde nada.
- O tipo da avaliação é gravado como texto ("Prova", "Trabalho", "Seminário", "Projeto", "Atividade", "Outro"), pelo `Converters`. Os três últimos tipos entraram sem mudar o esquema do banco.

## Consumo de API (BrasilAPI)

```text
FeriadosScreen ─▶ FeriadoViewModel ─▶ FeriadoRepository ─▶ FeriadoApiService (Retrofit + Gson)
      ▲                  │                 (Dispatchers.IO)        GET https://brasilapi.com.br/api/feriados/v1/{ano}
      └── FeriadosUiState: Carregando | Sucesso(proximos, passados) | Erro
```

- **Carregando:** `CircularProgressIndicator`.
- **Sucesso:** feriados próximos primeiro, com quantos dias faltam, e depois os que já passaram.
- **Erro:** mensagem na tela com o botão "Tentar novamente" e um Snackbar com a mesma ação.
- O `FeriadoRepository` tem teste unitário com uma API falsa (`FeriadoRepositoryTest`).

## Backend complementar

```text
Cliente HTTP → API Express → validação → SQLite do servidor
```

O backend usa Node.js, TypeScript, Express e `better-sqlite3`. Ele tem seu próprio banco, separado do SQLite do celular. A camada `network/` do app já aponta para ele (`http://10.0.2.2:3000/api/` no emulador), mas o app é offline-first e não depende da API.

## Regra de prioridade

```text
prioridade = urgência × 0,60 + impacto × 0,40
```

A urgência aumenta à medida que o prazo se aproxima. O impacto converte o peso de 0 a 5 para a escala de 0 a 100. Avaliações concluídas não aparecem no painel de pendências. A regra fica em `model/Prioridade` e tem testes em `app/src/test`.
