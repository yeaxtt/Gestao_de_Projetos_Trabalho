# Guia de apresentação do StudyFlow

## Explicação em 30 segundos

O StudyFlow centraliza avaliações acadêmicas e ajuda o aluno a decidir o que estudar primeiro. Cada avaliação recebe uma pontuação de 0 a 100 calculada com 60% de urgência do prazo e 40% do peso da nota. O aplicativo funciona sem internet, salva os dados com Room/SQLite e programa lembretes locais para 7, 3 e 1 dia antes.

## Roteiro de demonstração

1. Mostre os indicadores de pendentes, concluídas e matérias.
2. Cadastre uma matéria e explique o fluxo Tela → ViewModel → Repository → DAO → Room, igual ao projeto CadastroAlunos das aulas.
3. Cadastre uma avaliação escolhendo tipo, matéria, peso e data.
4. Volte ao início e mostre que ela entrou no ranking de prioridade.
5. Cadastre outra avaliação com data/peso diferentes e compare as pontuações.
6. Mostre os filtros “Todas”, “Pendentes” e “Concluídas” e a ordenação no ícone do canto superior.
7. Conclua uma avaliação e depois reabra.
8. Feche e reabra o app para provar a persistência local.

## Perguntas prováveis

**Por que Kotlin e Compose?**  
Kotlin é a linguagem moderna usada no Android. Compose permite descrever a interface em Kotlin e atualizá-la automaticamente quando o estado muda.

**Por que Room?**  
Room organiza o SQLite com entidades e DAOs, valida consultas durante a compilação e integra com Kotlin Flow.

**Como funciona a prioridade?**  
A urgência vem da distância até o prazo, o impacto vem do peso de 0 a 5 e o resultado combina `urgência × 0,60 + impacto × 0,40`.

**Por que ViewModel e Repository?**  
O ViewModel mantém o estado da tela. O Repository separa a origem dos dados da interface, facilitando manutenção e testes. Como no projeto das aulas, há um ViewModel por assunto e cada tela recebe estados selados (Carregando, Sucesso, Erro).

**O que acontece com quem já tinha a versão anterior instalada?**  
O banco passou da versão 1 para a 2. A migração `MIGRATION_1_2` recria a tabela de avaliações com chave estrangeira para matérias e copia os dados, sem perder nada.

**Como os lembretes funcionam?**  
O WorkManager agenda tarefas locais para 7, 3 e 1 dia antes. Ao concluir ou excluir, elas são canceladas. Ao reabrir, são agendadas novamente.

**Por que existe backend se o app é offline?**  
Ele demonstra uma evolução possível para sincronização. O MVP continua offline-first e usa o banco local como fonte principal.

## Pontos técnicos para destacar

- os dados sobrevivem ao fechamento do aplicativo;
- filtros mudam a visualização sem apagar registros;
- excluir uma matéria remove avaliações vinculadas pelo próprio banco (`ON DELETE CASCADE`) e evita dados órfãos;
- a ordenação escolhida fica salva em SharedPreferences;
- campos obrigatórios e peso entre 0 e 5 são validados;
- o ranking se atualiza automaticamente com Flow;
- a navegação evita cópias repetidas da mesma tela na pilha.
