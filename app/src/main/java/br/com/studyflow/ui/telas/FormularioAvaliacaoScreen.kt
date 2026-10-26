package br.com.studyflow.ui.telas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.studyflow.model.Avaliacao
import br.com.studyflow.model.Materia
import br.com.studyflow.model.TipoAvaliacao
import br.com.studyflow.ui.components.ChipSegmento
import br.com.studyflow.ui.components.MateriaDropdown
import br.com.studyflow.ui.components.navyButtonColors
import br.com.studyflow.ui.preview.DadosExemplo
import br.com.studyflow.ui.state.FormularioAvaliacaoUiState
import br.com.studyflow.ui.state.SalvarAvaliacaoUiState
import br.com.studyflow.ui.theme.Background
import br.com.studyflow.ui.theme.FieldShape
import br.com.studyflow.ui.theme.Ink
import br.com.studyflow.ui.theme.OutlineColor
import br.com.studyflow.ui.theme.StudyFlowTheme
import br.com.studyflow.ui.util.dataUtcParaHorarioLocal
import br.com.studyflow.ui.util.formatarData
import br.com.studyflow.ui.util.formatarPeso

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAvaliacaoScreen(
    formularioUiState: FormularioAvaliacaoUiState,
    salvarUiState: SalvarAvaliacaoUiState,
    onSalvarClick: (Avaliacao) -> Unit,
    onVoltarClick: () -> Unit,
    onCadastrarMateriaClick: () -> Unit,
    onTentarNovamente: () -> Unit,
    onSalvoComSucesso: () -> Unit,
    onErroMostrado: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Reage ao estado de salvamento
    LaunchedEffect(salvarUiState) {
        when (salvarUiState) {
            is SalvarAvaliacaoUiState.Sucesso -> {
                onSalvoComSucesso()
            }
            is SalvarAvaliacaoUiState.Erro -> {
                snackbarHostState.showSnackbar(salvarUiState.mensagem)
                onErroMostrado()
            }
            else -> {}
        }
    }

    val editando = (formularioUiState as? FormularioAvaliacaoUiState.Sucesso)?.avaliacao != null

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editando) "Editar avaliação" else "Nova avaliação")
                },
                navigationIcon = {
                    IconButton(onClick = onVoltarClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (formularioUiState) {
                is FormularioAvaliacaoUiState.Ocioso,
                is FormularioAvaliacaoUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is FormularioAvaliacaoUiState.Erro -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = formularioUiState.mensagem,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(onClick = onTentarNovamente) {
                            Text("Tentar novamente")
                        }
                    }
                }

                is FormularioAvaliacaoUiState.Sucesso -> {
                    FormularioAvaliacaoConteudo(
                        avaliacao = formularioUiState.avaliacao,
                        materias = formularioUiState.materias,
                        salvando = salvarUiState is SalvarAvaliacaoUiState.Salvando,
                        onSalvarClick = onSalvarClick,
                        onCadastrarMateriaClick = onCadastrarMateriaClick
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormularioAvaliacaoConteudo(
    avaliacao: Avaliacao?,
    materias: List<Materia>,
    salvando: Boolean,
    onSalvarClick: (Avaliacao) -> Unit,
    onCadastrarMateriaClick: () -> Unit
) {
    val editando = avaliacao != null

    var titulo by remember(avaliacao) { mutableStateOf(avaliacao?.titulo ?: "") }
    var tipo by remember(avaliacao) { mutableStateOf(avaliacao?.tipo ?: TipoAvaliacao.PROVA) }
    var materiaId by remember(avaliacao, materias) { mutableStateOf(avaliacao?.materiaId ?: materias.firstOrNull()?.id) }
    var peso by remember(avaliacao) { mutableStateOf(avaliacao?.peso?.let(::formatarPeso) ?: "5") }
    var dataMillis by remember(avaliacao) {
        mutableLongStateOf(avaliacao?.dataMillis ?: (System.currentTimeMillis() + 3 * 86_400_000L))
    }
    var mostrarCalendario by remember { mutableStateOf(false) }

    var tituloErro by remember { mutableStateOf(false) }
    var pesoErro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (materias.isEmpty()) {
            Text(
                text = "Cadastre uma matéria antes de criar avaliações.",
                color = MaterialTheme.colorScheme.error
            )
            OutlinedButton(onClick = onCadastrarMateriaClick, modifier = Modifier.fillMaxWidth()) {
                Text("Cadastrar matéria")
            }
        }

        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
                tituloErro = false
            },
            label = { Text("Título *") },
            placeholder = { Text("Ex: Prova de Cálculo II") },
            isError = tituloErro,
            supportingText = {
                if (tituloErro) Text("Título é obrigatório")
            },
            enabled = !salvando,
            shape = FieldShape,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            TipoAvaliacao.entries.forEach { opcao ->
                ChipSegmento(opcao.titulo, tipo == opcao, Modifier.weight(1f), paddingHorizontal = 2.dp) {
                    if (!salvando) tipo = opcao
                }
            }
        }

        MateriaDropdown(
            materias = materias,
            selecionadaId = materiaId,
            enabled = !salvando,
            onSelecionar = { materiaId = it }
        )

        OutlinedTextField(
            value = peso,
            onValueChange = {
                peso = it
                pesoErro = false
            },
            label = { Text("Peso de 0 a 5 *") },
            isError = pesoErro,
            supportingText = {
                if (pesoErro) Text("Informe um peso entre 0 e 5")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            enabled = !salvando,
            shape = FieldShape,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedButton(
            onClick = { mostrarCalendario = true },
            enabled = !salvando,
            border = BorderStroke(1.dp, OutlineColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Prazo: ${formatarData(dataMillis)}")
        }

        Text("Lembretes serão programados para 7, 3 e 1 dia antes.", color = Ink, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val pesoNum = peso.replace(',', '.').toDoubleOrNull()
                tituloErro = titulo.isBlank()
                pesoErro = pesoNum == null || pesoNum !in 0.0..5.0

                val materiaSelecionada = materiaId
                if (!tituloErro && !pesoErro && materiaSelecionada != null) {
                    val avaliacaoSalvar = Avaliacao(
                        id = avaliacao?.id ?: 0,
                        titulo = titulo.trim(),
                        tipo = tipo,
                        materiaId = materiaSelecionada,
                        dataMillis = dataMillis,
                        peso = pesoNum ?: 0.0,
                        concluida = avaliacao?.concluida ?: false
                    )
                    onSalvarClick(avaliacaoSalvar)
                }
            },
            enabled = !salvando && materias.isNotEmpty(),
            colors = navyButtonColors(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (salvando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (editando) "Atualizar" else "Cadastrar")
            }
        }
    }

    if (mostrarCalendario) {
        val estadoCalendario = rememberDatePickerState(initialSelectedDateMillis = dataMillis)
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(onClick = {
                    estadoCalendario.selectedDateMillis?.let { dataMillis = dataUtcParaHorarioLocal(it) }
                    mostrarCalendario = false
                }) { Text("Confirmar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = estadoCalendario)
        }
    }
}

@Preview(name = "Nova avaliação", showSystemUi = true)
@Composable
private fun FormularioAvaliacaoNovaPreview() {
    StudyFlowTheme {
        FormularioAvaliacaoScreen(
            formularioUiState = FormularioAvaliacaoUiState.Sucesso(null, DadosExemplo.materias),
            salvarUiState = SalvarAvaliacaoUiState.Ocioso,
            onSalvarClick = {}, onVoltarClick = {}, onCadastrarMateriaClick = {},
            onTentarNovamente = {}, onSalvoComSucesso = {}, onErroMostrado = {}
        )
    }
}

@Preview(name = "Editar avaliação", showSystemUi = true)
@Composable
private fun FormularioAvaliacaoEditarPreview() {
    StudyFlowTheme {
        FormularioAvaliacaoScreen(
            formularioUiState = FormularioAvaliacaoUiState.Sucesso(DadosExemplo.avaliacoes[0], DadosExemplo.materias),
            salvarUiState = SalvarAvaliacaoUiState.Ocioso,
            onSalvarClick = {}, onVoltarClick = {}, onCadastrarMateriaClick = {},
            onTentarNovamente = {}, onSalvoComSucesso = {}, onErroMostrado = {}
        )
    }
}
