# Arquitetura do StudyFlow

## Aplicativo Android

```text
Interface Jetpack Compose
        ↓ ações / ↑ estado
StudyViewModel
        ↓
StudyRepository
        ↓
DAOs do Room
        ↓
SQLite local
```

- A interface apenas apresenta o estado e encaminha ações do usuário.
- O ViewModel coordena cadastro, exclusão, conclusão e o ranking de prioridade.
- O Repository isola o acesso aos dados.
- Os DAOs contêm as consultas e operações persistentes.
- Room expõe listas por `Flow`; qualquer mudança atualiza a interface de forma reativa.
- WorkManager agenda lembretes locais de prazo e não exige servidor.

## Backend complementar

```text
Cliente HTTP → API Express → validação → SQLite do servidor
```

O backend usa Node.js, TypeScript, Express e `better-sqlite3`. Ele possui seu próprio banco, separado do SQLite do celular. A API está preparada para uma futura sincronização, mas não é usada como fonte principal do MVP offline-first.

## Regra de prioridade

```text
prioridade = urgência × 0,60 + impacto × 0,40
```

A urgência aumenta à medida que o prazo se aproxima. O impacto converte o peso de 0 a 5 para a escala de 0 a 100. Avaliações concluídas não aparecem no painel de pendências.
