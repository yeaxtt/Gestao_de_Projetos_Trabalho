package br.com.studyflow.ui.screens

import androidx.compose.runtime.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.*
@Composable
fun DialogoNovaMateria() {
    val textField1 = remember { mutableStateOf("") }
    val textField2 = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color(0xFFFFFFFF),
            )
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    color = Color(0xFFFAF8FF),
                )
                .verticalScroll(rememberScrollState())
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF000000),
                    )
                    .padding(vertical = 263.dp,horizontal = 24.dp,)
            ){
                Column(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(28.dp))
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFE9E7EF),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .shadow(
                            elevation = 3.dp,
                            spotColor = Color(0x33000000),
                        )
                        .padding(top = 24.dp,bottom = 24.dp,end = 24.dp,)
                ){
                    Text("Nova matéria",
                        color = Color(0xFF1B1B21),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(bottom = 16.dp,start = 24.dp,)
                    )
                    Column(
                        modifier = Modifier
                            .padding(bottom = 16.dp,start = 24.dp,)
                            .fillMaxWidth()
                    ){
                        TextFieldView(
                            placeholder = "Nome *",
                            value = textField1.value,
                            onValueChange = { newText -> textField1.value = newText },
                            textStyle = TextStyle(
                                color = Color(0xFF45464F),
                                fontSize = 16.sp,
                            ),
                            modifier = Modifier
                                .padding(bottom = 10.dp,)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF757780),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clip(shape = RoundedCornerShape(4.dp))
                                .fillMaxWidth()
                                .padding(vertical = 16.dp,horizontal = 17.dp,)
                        )
                        TextFieldView(
                            placeholder = "Professor (opcional)",
                            value = textField2.value,
                            onValueChange = { newText -> textField2.value = newText },
                            textStyle = TextStyle(
                                color = Color(0xFF45464F),
                                fontSize = 16.sp,
                            ),
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF757780),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clip(shape = RoundedCornerShape(4.dp))
                                .fillMaxWidth()
                                .padding(vertical = 16.dp,horizontal = 17.dp,)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 24.dp,)
                            .fillMaxWidth()
                    ){
                        Column(
                            modifier = Modifier
                                .padding(end = 8.dp,)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .padding(vertical = 10.dp,horizontal = 12.dp,)
                        ){
                            Text("Cancelar",
                                color = Color(0xFF3659C9),
                                fontSize = 14.sp,
                            )
                        }
                        Column(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(20.dp))
                                .background(
                                    color = Color(0x1C1B1B20),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable { println("Pressed!") }
                                .padding(vertical = 10.dp,horizontal = 24.dp,)
                        ){
                            Text("Salvar",
                                color = Color(0xFF000000),
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}