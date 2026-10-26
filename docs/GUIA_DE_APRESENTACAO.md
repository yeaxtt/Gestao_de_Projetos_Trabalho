# Guia de apresentação do StudyFlow

Tempo total: 8 minutos, sendo 3 de apresentação e 5 de demonstração no emulador.

## Antes de começar

- Abra o emulador e rode o app pelo Android Studio (**Run 'app'**).
- Deixe pelo menos duas matérias e três avaliações cadastradas, com prazos diferentes.
- Confirme que o emulador tem internet, para a aba Feriados.

## Parte 1: apresentação (3 minutos)

**Problema (1 minuto).** O estudante tem prazos de várias disciplinas espalhados em avisos de sala, ambiente virtual e grupos de mensagem. Ele perde entregas ou estuda primeiro o que é menos urgente, o que leva a notas baixas e reprovações. O tema é uma variação do item 08 do catálogo, Gestão de Eventos Acadêmicos, voltada aos compromissos avaliativos.

**MVP (1 minuto).** O StudyFlow reúne matérias e avaliações num só lugar e funciona sem internet. O painel mostra primeiro o que exige mais atenção, com a regra 60% urgência do prazo + 40% peso na nota. Há lembretes 7, 3 e 1 dia antes e a aba de feriados nacionais para planejar as entregas.

**Arquitetura (1 minuto).** MVVM em camadas: Tela em Compose → ViewModel → Repository → Room ou Retrofit. As ViewModels não têm `Context` e expõem o estado como `StateFlow` de `sealed interface`. Os repositórios usam `Dispatchers.IO`. A estrutura de pacotes segue o projeto CadastroAlunos das aulas.

## Parte 2: demonstração (5 minutos)

1. **Menu de navegação.** Passe pelas quatro abas da barra inferior: Início, Matérias, Avaliações e Feriados.
2. **Listagem e formulário.** Em Matérias, toque em **+**, cadastre uma matéria e mostre que ela aparece na lista.
3. **AlertDialog de descarte.** Abra o cadastro de outra matéria, digite um nome e toque na seta de voltar. Mostre o diálogo "Descartar alterações?" e toque em **Descartar**.
4. **Avaliação e prioridade.** Em Avaliações, toque em **Nova avaliação**, escolha tipo, matéria, peso e uma data próxima e salve. Volte ao Início e mostre que ela entrou no ranking, com o selo de prioridade e "Faltam X dias".
5. **Filtros.** Em Avaliações, alterne entre Todas, Pendentes e Concluídas e filtre por matéria.
6. **AlertDialog de exclusão.** Toque na lixeira de uma avaliação, mostre o diálogo, cancele, e depois exclua de verdade. Aparece a mensagem de confirmação embaixo.
7. **API.** Abra Feriados e mostre a lista carregada da BrasilAPI. Se der tempo, troque para o ano seguinte para mostrar o indicador de carregamento.
8. **Persistência no Room.** Feche o app pela tela de apps recentes, abra de novo e mostre que os dados e a ordenação escolhida continuam.

## Perguntas técnicas prováveis

**Por que a ViewModel não recebe Context?**
Porque a ViewModel vive mais que a Activity (sobrevive à rotação). Guardar um `Context` nela pode causar vazamento de memória. As dependências que precisam de `Context` (banco, preferências, WorkManager) são criadas na `ViewModelProvider.Factory` e entregues prontas pelo construtor.

**Por que StateFlow?**
É um fluxo que sempre tem um valor atual e avisa quem observa quando ele muda. A ViewModel guarda o `MutableStateFlow` privado e expõe só o `StateFlow`, então apenas ela altera o estado. A tela coleta com `collectAsStateWithLifecycle`, que para de coletar quando o app vai para segundo plano.

**O que é o UiState selado?**
Uma `sealed interface` com todos os estados possíveis da tela, como Carregando, Sucesso e Erro. O `when` na tela obriga a tratar cada um, e o compilador avisa se faltar algum.

**Onde ficam as coroutines?**
A ViewModel inicia o trabalho com `viewModelScope.launch`, cancelado sozinho quando a ViewModel é destruída. O Repository troca para `Dispatchers.IO` com `withContext`, para o banco e a rede não travarem a interface.

**Como funciona a chamada à API?**
O `FeriadoApiService` é uma interface Retrofit com `@GET("feriados/v1/{ano}")`, e o Gson converte o JSON na classe `Feriado`. A ViewModel mostra Carregando, chama o Repository e troca para Sucesso ou Erro. No Erro, aparece "Tentar novamente" na tela e no Snackbar.

**Como funciona o Room aqui?**
`MateriaEntity` e `AvaliacaoEntity` são as tabelas, ligadas por chave estrangeira com `ON DELETE CASCADE`. Os DAOs têm inserir, atualizar, deletar e consultas que retornam `Flow<List<…>>`, então as listas se atualizam sozinhas quando o banco muda. O `AppDatabase` herda de `RoomDatabase` e tem a migração da versão 1 para a 2.

**Onde está a preferência do usuário?**
No `AppPreferencesManager`, com SharedPreferences. Ele guarda a ordenação escolhida em cada lista, que continua valendo depois de fechar o app.

**Para que serve o rememberSaveable?**
O `remember` perde o valor quando a Activity é recriada, por exemplo ao girar a tela. O `rememberSaveable` salva o valor e o restaura, então o que foi digitado no formulário não se perde.

**Como a prioridade é calculada?**
A urgência vem da distância até o prazo e o impacto vem do peso de 0 a 5, convertido para 0 a 100. O resultado é `urgência × 0,60 + impacto × 0,40`. A regra fica em `model/Prioridade` e tem testes unitários.

**Como funcionam os lembretes?**
O WorkManager agenda tarefas locais para 7, 3 e 1 dia antes. Ao concluir, editar ou excluir a avaliação, elas são canceladas ou refeitas. No Android 13 ou superior, o app pede a permissão de notificação ao abrir.
