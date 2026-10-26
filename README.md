# StudyFlow

Aplicativo Android para organizar provas, trabalhos e seminários por matéria. O painel ordena as avaliações usando a regra definida no TAP: **60% urgência do prazo + 40% impacto da nota**.

## Integrantes

- Eric Andrey dos Santos
- Otavio Jose da Fonseca Maciel
- Arthur Marmentini
- Kaua Ballen Fortes

## Funcionalidades

- cadastro e exclusão segura de matérias;
- cadastro de avaliações com tipo, matéria, peso e data real;
- painel ordenado automaticamente por prioridade;
- filtros por matéria e por status;
- conclusão e reabertura de avaliações;
- persistência offline com Room/SQLite;
- lembretes locais 7, 3 e 1 dia antes do prazo;
- API REST complementar em Node.js, Express e SQLite.

## Executar o Android

1. Abra esta pasta no Android Studio.
2. Use JDK 17 e instale o Android SDK 35 quando solicitado.
3. Aguarde a sincronização do Gradle.
4. Execute em um emulador ou celular Android 7.0 ou superior.

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

`Compose UI → ViewModel → Repository → DAO → Room → SQLite`

- **Compose:** telas e interação;
- **ViewModel:** estado e ações da interface;
- **Repository:** abstração de acesso aos dados;
- **DAO/Room:** persistência local reativa com Flow;
- **WorkManager:** agendamento confiável dos lembretes.

Consulte `docs/GUIA_DE_APRESENTACAO.md` para um roteiro curto de demonstração.
