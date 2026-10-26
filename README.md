# StudyFlow

Aplicativo Android nativo que ajuda estudantes a organizar provas, trabalhos e outras avaliações por matéria e a decidir o que estudar primeiro. Cada avaliação recebe uma prioridade calculada com **60% de urgência do prazo + 40% de impacto na nota**.

## Tema

**Variação do tema 08 do Catálogo de Projetos Integradores: Gestão de Eventos Acadêmicos.** Em vez de eventos, o StudyFlow organiza os compromissos avaliativos do estudante: provas, trabalhos, seminários, projetos e atividades.

**Problema:** o estudante acumula prazos de várias disciplinas em lugares diferentes (avisos em sala, ambiente virtual, grupos de mensagem) e perde entregas ou estuda primeiro o que é menos urgente. Isso leva a notas baixas, reprovações e, no limite, à evasão.

**Proposta do MVP:** reunir todas as avaliações num só lugar, funcionando sem internet, com um painel que mostra primeiro o que exige mais atenção, lembretes antes de cada prazo e o calendário de feriados nacionais para planejar as entregas.

## Integrantes

- Eric Andrey dos Santos
- Otavio Jose da Fonseca Maciel
- Arthur Marmentini
- Kaua Ballen Fortes

## Funcionalidades

- **Painel (Início):** pendências ordenadas por prioridade, contadores de pendentes, concluídas e atrasadas, e quanto falta para cada prazo.
- **Matérias:** listagem com ordenação salva, cadastro e edição em tela própria e exclusão com confirmação.
- **Avaliações:** listagem com filtros por status (Todas, Pendentes, Concluídas) e por matéria, cadastro e edição (título, tipo, matéria, data e peso), conclusão e reabertura, exclusão com confirmação.
- **Feriados:** feriados nacionais do ano, consultados na [BrasilAPI](https://brasilapi.com.br/docs#tag/Feriados-Nacionais), com os estados carregando, sucesso e erro com opção de tentar novamente.
- **Diálogos de confirmação (AlertDialog):** para excluir matéria ou avaliação e para descartar alterações não salvas de um formulário, inclusive pelo botão voltar do sistema.
- **Lembretes locais** 7, 3 e 1 dia antes do prazo, com WorkManager. O app pede a permissão de notificação no Android 13 ou superior.
- **Funciona sem internet:** todos os dados ficam no Room (SQLite). Só a aba Feriados usa a rede.

## Arquitetura

MVVM em camadas, com fluxo de dados unidirecional:

```text
UI (Jetpack Compose)  ──eventos──▶  ViewModel  ──▶  Repository  ──▶  Fonte de dados
        ▲                               │                              ├─ Room (DAO → SQLite)
        └──────── StateFlow<UiState> ───┘                              ├─ BrasilAPI (Retrofit)
                                                                       └─ SharedPreferences
```

- **UI:** telas 100% em Compose com Material 3. As telas recebem estado e funções por parâmetro (state hoisting), sem acessar a ViewModel diretamente. Os campos dos formulários usam `rememberSaveable`.
- **ViewModel:** herda de `ViewModel` (sem `Context`) e expõe o estado apenas como `StateFlow` imutável, modelado com `sealed interface` (por exemplo, `ListaMateriasUiState`: Carregando, Sucesso, Erro). As dependências são montadas por uma `ViewModelProvider.Factory`. O controle dos diálogos de confirmação também fica na ViewModel.
- **Repository:** separa a origem dos dados da interface, converte entidades em modelos e executa o banco e a rede no `Dispatchers.IO`.
- **Dados:** Room com duas entidades relacionadas (`MateriaEntity` 1:N `AvaliacaoEntity`, com `ON DELETE CASCADE`), DAOs com CRUD e leitura reativa via `Flow<List<T>>`, banco na versão 2 com migração. A ordenação escolhida em cada lista fica salva em SharedPreferences.

```text
app/src/main/java/br/com/studyflow/
├── MainActivity.kt        tema, AppNavHost e pedido de permissão de notificação
├── model/                 Materia, Avaliacao, TipoAvaliacao, Feriado, ordenações, filtro e regra de Prioridade
├── data/
│   ├── local/
│   │   ├── entity/        MateriaEntity, AvaliacaoEntity e as relações (@Embedded + @Relation)
│   │   ├── dao/           MateriaDao, AvaliacaoDao
│   │   ├── converter/     Converters (TipoAvaliacao ↔ texto)
│   │   ├── preferences/   AppPreferencesManager (SharedPreferences)
│   │   └── AppDatabase.kt RoomDatabase, versão 2, com MIGRATION_1_2
│   └── repository/        MateriaRepository, AvaliacaoRepository, FeriadoRepository
├── network/               RetrofitClient, FeriadoApiService (BrasilAPI) e serviços do backend/
├── notifications/         AgendadorLembretes e LembreteWorker (WorkManager)
└── ui/
    ├── navigation/        AppRoute (rotas tipadas) e AppNavHost (NavHost + NavigationBar)
    ├── state/             UiStates selados de cada tela
    ├── viewmodel/         PainelViewModel, MateriaViewModel, AvaliacaoViewModel, FeriadoViewModel
    ├── telas/             telas de painel, listas, formulários e feriados
    ├── components/        componentes visuais reaproveitados (cartões, chips, diálogos)
    ├── util/              formatação de datas, peso e prazo restante
    └── theme/             cores, formas e tipografia do protótipo no Figma
```

**Navegação:** `NavHost` com rotas `@Serializable` e `NavigationBar` com quatro módulos: Início, Matérias (lista ↔ formulário), Avaliações (lista com filtros ↔ formulário) e Feriados. A seta da barra superior e o botão voltar do sistema seguem a pilha de navegação.

**Bibliotecas:**

| Uso | Biblioteca |
|---|---|
| Interface | Jetpack Compose (BOM 2026.02.01), Material 3, Material Icons Extended |
| Navegação | Navigation Compose 2.10.1 com Kotlin Serialization |
| Estado e ciclo de vida | Lifecycle ViewModel Compose e Runtime Compose 2.11.0 (`collectAsStateWithLifecycle`) |
| Assincronismo | Kotlin Coroutines e Flow (`viewModelScope`, `Dispatchers.IO`) |
| Banco local | Room 2.8.5 (com KSP) |
| Rede | Retrofit 3.0.0, conversor Gson e OkHttp Logging Interceptor 5.5.0 |
| Lembretes | WorkManager 2.10.0 |
| Testes | JUnit 4 |

Mais detalhes em [`docs/ARQUITETURA.md`](docs/ARQUITETURA.md). A organização de pacotes segue o projeto `CadastroAlunos` das aulas ([repositório do professor](https://github.com/joaopichetti/afya-programacao-aplicacoes-dispositivos-moveis-2026-02)).

## Como executar

**Requisitos:** Android Studio recente (com o JDK embutido), Android SDK 37 e um emulador ou celular com Android 8.0 ou superior.

1. Clone o repositório:
   ```bash
   git clone https://github.com/yeaxtt/Gestao_de_Projetos_Trabalho.git
   ```
2. No Android Studio, abra a pasta clonada (**File > Open**).
3. Aguarde a sincronização do Gradle. Se o Android Studio pedir, instale o Android SDK 37.
4. Escolha um emulador ou conecte um celular e clique em **Run 'app'**.
5. Na primeira abertura, permita as notificações para receber os lembretes.

Pelo terminal, na pasta do projeto:

```bash
./gradlew assembleDebug          # gera app/build/outputs/apk/debug/app-debug.apk
./gradlew testDebugUnitTest      # executa os testes unitários
```

A aba Feriados precisa de internet. As demais funções operam offline.

## Backend complementar (opcional)

A pasta `backend/` tem uma API REST em Node.js, Express e SQLite, pensada para uma futura sincronização. O app não depende dela.

```bash
cd backend
npm install
npm run dev     # http://localhost:3000 (no emulador: http://10.0.2.2:3000)
```

Use Node.js 20 ou 22 LTS, pois o driver SQLite contém um módulo nativo.
