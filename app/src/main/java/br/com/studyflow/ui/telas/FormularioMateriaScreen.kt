package br.com.studyflow.ui.telas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.studyflow.model.Materia
import br.com.studyflow.ui.components.navyButtonColors
import br.com.studyflow.ui.preview.DadosExemplo
import br.com.studyflow.ui.state.FormularioMateriaUiState
import br.com.studyflow.ui.state.SalvarMateriaUiState
import br.com.studyflow.ui.theme.Background
import br.com.studyflow.ui.theme.FieldShape
import br.com.studyflow.ui.theme.StudyFlowTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioMateriaScreen(
    formularioUiState: FormularioMateriaUiState,
    salvarUiState: SalvarMateriaUiState,
    onSalvarClick: (Materia) -> Unit,
    onVoltarClick: () -> Unit,
    onTentarNovamente: () -> Unit,
    onSalvoComSucesso: () -> Unit,
    onErroMostrado: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Reage ao estado de salvamento
    LaunchedEffect(salvarUiState) {
        when (salvarUiState) {
            is SalvarMateriaUiState.Sucesso -> {
                onSalvoComSucesso()
            }
            is SalvarMateriaUiState.Erro -> {
                snackbarHostState.showSnackbar(salvarUiState.mensagem)
                onErroMostrado()
            }
            else -> {}
        }
    }

    val editando = formularioUiState is FormularioMateriaUiState.Sucesso

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(if (editando) "Editar matéria" else "Nova matéria")
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
                is FormularioMateriaUiState.Carregando -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is FormularioMateriaUiState.Erro -> {
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

                is FormularioMateriaUiState.Ocioso -> {
                    FormularioMateriaConteudo(
                        materia = null,
                        salvando = salvarUiState is SalvarMateriaUiState.Salvando,
                        onSalvarClick = onSalvarClick
                    )
                }

                is FormularioMateriaUiState.Sucesso -> {
                    FormularioMateriaConteudo(
                        materia = formularioUiState.materia,
                        salvando = salvarUiState is SalvarMateriaUiState.Salvando,
                        onSalvarClick = onSalvarClick
                    )
                }
            }
        }
    }
}

@Composable
private fun FormularioMateriaConteudo(
    materia: Materia?,
    salvando: Boolean,
    onSalvarClick: (Materia) -> Unit
) {
    val editando = materia != null

    var nome by remember(materia) { mutableStateOf(materia?.nome ?: "") }
    var professor by remember(materia) { mutableStateOf(materia?.professor ?: "") }

    var nomeErro by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = nome,
            onValueChange = {
                nome = it
                nomeErro = false
            },
            label = { Text("Nome da matéria *") },
            placeholder = { Text("Ex: Programação para Dispositivos Móveis") },
            isError = nomeErro,
            supportingText = {
                if (nomeErro) Text("Nome é obrigatório")
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            enabled = !salvando,
            shape = FieldShape,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = professor,
            onValueChange = { professor = it },
            label = { Text("Professor (opcional)") },
            placeholder = { Text("Ex: Prof. Helena Prado") },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            enabled = !salvando,
            shape = FieldShape,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nomeErro = nome.isBlank()

                if (!nomeErro) {
                    val materiaSalvar = Materia(
                        id = materia?.id ?: 0,
                        nome = nome.trim(),
                        professor = professor.trim()
                    )
                    onSalvarClick(materiaSalvar)
                }
            },
            enabled = !salvando,
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
}

@Preview(name = "Nova matéria", showSystemUi = true)
@Composable
private fun FormularioMateriaNovaPreview() {
    StudyFlowTheme {
        FormularioMateriaScreen(
            formularioUiState = FormularioMateriaUiState.Ocioso,
            salvarUiState = SalvarMateriaUiState.Ocioso,
            onSalvarClick = {}, onVoltarClick = {}, onTentarNovamente = {}, onSalvoComSucesso = {}, onErroMostrado = {}
        )
    }
}

@Preview(name = "Editar matéria", showSystemUi = true)
@Composable
private fun FormularioMateriaEditarPreview() {
    StudyFlowTheme {
        FormularioMateriaScreen(
            formularioUiState = FormularioMateriaUiState.Sucesso(DadosExemplo.materias[2]),
            salvarUiState = SalvarMateriaUiState.Ocioso,
            onSalvarClick = {}, onVoltarClick = {}, onTentarNovamente = {}, onSalvoComSucesso = {}, onErroMostrado = {}
        )
    }
}
