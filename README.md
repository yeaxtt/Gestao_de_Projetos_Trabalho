# StudyFlow

Aplicativo Android para organizar provas, trabalhos e seminários por matéria. O painel ordena as avaliações usando a regra definida no TAP: **60% urgência do prazo + 40% impacto da nota**.

## Integrantes

- Eric Andrey dos Santos
- Otavio Jose da Fonseca Maciel
- Arthur Marmentini
- Kaua Ballen Fortes

## Funcionalidades

- cadastro, edição e exclusão de matérias, com confirmação;
- cadastro, edição e exclusão de avaliações com tipo, matéria, peso e data real;
- painel ordenado automaticamente por prioridade;
- filtros por matéria e por status;
- ordenação das listas, guardada entre uma abertura e outra do app;
- conclusão e reabertura de avaliações;
- persistência offline com Room/SQLite;
- lembretes locais 7, 3 e 1 dia antes do prazo;
- API REST complementar em Node.js, Express e SQLite.

## Executar o Android

1. Abra esta pasta no Android Studio.
2. Instale o Android SDK 37 quando solicitado (o Gradle 9.6 e o JDK vêm com o Android Studio).
3. Aguarde a sincronização do Gradle.
4. Execute em um emulador ou celular Android 8.0 ou superior.

Para gerar um APK de teste: **Build > Generate App Bundles or APKs > Generate APKs**. O resultado fica em `app/build/outputs/apk/debug/app-debug.apk`.

## Executar o backend

```bash
cd backend
npm install
npm run dev
```

Use Node.js 20 ou 22 LTS, pois o driver SQLite contém um módulo nativo.

A API fica disponível em `http://localhost:3000`. O aplicativo é offline-first e não depende da API para seu funcionamento principal; ela representa uma possibilidade de evolução para sincronização.

## Arquitetura Android

A organização segue o projeto `CadastroAlunos` das aulas 06 a 08 de Programação para Dispositivos Móveis ([repositório do professor](https://github.com/joaopichetti/afya-programacao-aplicacoes-dispositivos-moveis-2026-02)):

`Telas (Compose) → ViewModel → Repository → DAO → Room → SQLite`

```text
app/src/main/java/br/com/studyflow/
├── MainActivity.kt
├── model/                 Materia, Avaliacao, TipoAvaliacao, ordenações, filtro e regra de Prioridade
├── data/
│   ├── local/
│   │   ├── entity/        MateriaEntity, AvaliacaoEntity e as relações (MateriaComAvaliacoes, AvaliacaoComMateria)
│   │   ├── dao/           MateriaDao, AvaliacaoDao
│   │   ├── converter/     Converters (TipoAvaliacao ↔ texto)
│   │   ├── preferences/   AppPreferencesManager (ordenação salva em SharedPreferences)
│   │   └── AppDatabase.kt banco Room, versão 2, com a migração 1 → 2
│   └── repository/        MateriaRepository, AvaliacaoRepository
├── network/               RetrofitClient e um ApiService por assunto (para a API em backend/)
├── notifications/         AgendadorLembretes e LembreteWorker (WorkManager)
└── ui/
    ├── navigation/        AppRoute (rotas tipadas) e AppNavHost
    ├── state/             estados de cada tela: Lista, Formulário, Salvar, Exclusão e Painel
    ├── viewmodel/         PainelViewModel, MateriaViewModel, AvaliacaoViewModel
    ├── telas/             PainelScreen, ListaMateriasScreen, FormularioMateriaScreen, ListaAvaliacoesScreen, FormularioAvaliacaoScreen
    ├── components/        componentes visuais do protótipo no Figma
    └── theme/             Color, Shape, Type e Theme
```

Detalhes em `docs/ARQUITETURA.md`.

Consulte `docs/GUIA_DE_APRESENTACAO.md` para um roteiro curto de demonstração.
