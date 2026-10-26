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
import androidx.compose.ui.layout.*
import com.skydoves.landscapist.*
import com.skydoves.landscapist.coil3.*
@Composable
fun DialogoExcluirMateria() {
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
                    .padding(vertical = 284.dp,horizontal = 24.dp,)
            ){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
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
                        .padding(vertical = 24.dp,)
                ){
                    CoilImage(
                        imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/g3uk2cw8_expires_30_days.png"},
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        modifier = Modifier
                            .padding(bottom = 16.dp,)
                            .clip(shape = RoundedCornerShape(28.dp))
                            .width(24.dp)
                            .height(24.dp)
                    )
                    Text("Excluir Cálculo II?",
                        color = Color(0xFF1B1B21),
                        fontSize = 24.sp,
                        modifier = Modifier
                            .padding(bottom = 16.dp,)
                    )
                    Text("As avaliações vinculadas a esta matéria também serão excluídas.",
                        color = Color(0xFF45464F),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(bottom = 16.dp,start = 24.dp,end = 24.dp,)
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 24.dp,)
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
                                .padding(vertical = 10.dp,horizontal = 12.dp,)
                        ){
                            Text("Excluir",
                                color = Color(0xFF3659C9),
                                fontSize = 14.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}