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
import androidx.compose.ui.layout.*
import com.skydoves.landscapist.*
import com.skydoves.landscapist.coil3.*
@Composable
fun DialogoNovaAvaliacao() {
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
                    .padding(vertical = 161.dp,horizontal = 24.dp,)
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
                    Text("Nova avaliação",
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
                            placeholder = "Título *",
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
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 10.dp,)
                                .fillMaxWidth()
                        ){
                            Column(
                                modifier = Modifier
                                    .padding(end = 6.dp,)
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .background(
                                        color = Color(0xFF1E293B),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { println("Pressed!") }
                                    .padding(vertical = 6.dp,horizontal = 16.dp,)
                            ){
                                Text("Prova",
                                    color = Color(0xFFE2E8F1),
                                    fontSize = 14.sp,
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .padding(end = 6.dp,)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFF757780),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .weight(1f)
                                    .clickable { println("Pressed!") }
                                    .padding(vertical = 7.dp,)
                            ){
                                Text("Trabalho",
                                    color = Color(0xFF45464F),
                                    fontSize = 14.sp,
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFF757780),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .weight(1f)
                                    .clickable { println("Pressed!") }
                                    .padding(vertical = 7.dp,)
                            ){
                                Text("Seminário",
                                    color = Color(0xFF45464F),
                                    fontSize = 14.sp,
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .padding(bottom = 10.dp,)
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFF757780),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .clip(shape = RoundedCornerShape(4.dp))
                                        .fillMaxWidth()
                                        .padding(vertical = 16.dp,horizontal = 17.dp,)
                                ){
                                    Text("Algoritmos",
                                        color = Color(0xFF1B1B21),
                                        fontSize = 16.sp,
                                    )
                                    CoilImage(
                                        imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/orsx8i9f_expires_30_days.png"},
                                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                        modifier = Modifier
                                            .clip(shape = RoundedCornerShape(4.dp))
                                            .width(24.dp)
                                            .height(24.dp)
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .offset(x = 12.dp, y = -8.dp)
                                    .align(Alignment.TopStart)
                                    .padding(start = 12.dp)
                                    .background(
                                        color = Color(0xFFE9E7EF),
                                    )
                                    .padding(horizontal = 4.dp,)
                            ){
                                Text("Matéria",
                                    color = Color(0xFF45464F),
                                    fontSize = 12.sp,
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .padding(bottom = 10.dp,)
                        ){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                TextFieldView(
                                    placeholder = "5",
                                    value = textField2.value,
                                    onValueChange = { newText -> textField2.value = newText },
                                    textStyle = TextStyle(
                                        color = Color(0xFF1B1B21),
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
                            Column(
                                modifier = Modifier
                                    .offset(x = 12.dp, y = -8.dp)
                                    .align(Alignment.TopStart)
                                    .padding(start = 12.dp)
                                    .background(
                                        color = Color(0xFFE9E7EF),
                                    )
                                    .padding(horizontal = 4.dp,)
                            ){
                                Text("Peso de 0 a 5 *",
                                    color = Color(0xFF45464F),
                                    fontSize = 12.sp,
                                )
                            }
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(bottom = 10.dp,)
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFF757780),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clip(shape = RoundedCornerShape(20.dp))
                                .fillMaxWidth()
                                .clickable { println("Pressed!") }
                                .padding(vertical = 11.dp,)
                        ){
                            CoilImage(
                                imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/b9kjfzy5_expires_30_days.png"},
                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                modifier = Modifier
                                    .padding(end = 8.dp,)
                                    .clip(shape = RoundedCornerShape(20.dp))
                                    .width(18.dp)
                                    .height(18.dp)
                            )
                            Text("Prazo: 15/10/2026",
                                color = Color(0xFF3659C9),
                                fontSize = 14.sp,
                            )
                        }
                        Text("Lembretes serão programados para 7, 3 e 1 dia antes.",
                            color = Color(0xFF1B1B21),
                            fontSize = 12.sp,
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