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
fun Componentes() {
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
                .padding(top = 24.dp,bottom = 24.dp,end = 24.dp,)
                .verticalScroll(rememberScrollState())
        ){
            Text("Componentes",
                color = Color(0xFF1B1B21),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 24.dp,start = 24.dp,)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 24.dp,start = 24.dp,)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .background(
                        color = Color(0xFFF4F3FA),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .shadow(
                        elevation = 3.dp,
                        spotColor = Color(0x2E000000),
                    )
                    .padding(vertical = 12.dp,horizontal = 25.dp,)
            ){
                Text("0",
                    color = Color(0xFF1B1B21),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text("Pendentes",
                    color = Color(0xFF1B1B21),
                    fontSize = 11.sp,
                )
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 24.dp,start = 24.dp,)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF4F3FA),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .shadow(
                        elevation = 3.dp,
                        spotColor = Color(0x2E000000),
                    )
                    .padding(top = 16.dp,bottom = 16.dp,end = 16.dp,)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 6.dp,start = 16.dp,)
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp,)
                            .weight(1f)
                    ){
                        Text("Prova de Cálculo II",
                            color = Color(0xFF1B1B21),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text("Cálculo II • Prova",
                            color = Color(0xFF45464F),
                            fontSize = 14.sp,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFFC5C6D0),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable { println("Pressed!") }
                            .padding(vertical = 7.dp,horizontal = 13.dp,)
                    ){
                        Text("Alta · 92",
                            color = Color(0xFF1B1B21),
                            fontSize = 14.sp,
                        )
                    }
                }
                Text("Prazo: 15/10/2026  •  Peso: 5",
                    color = Color(0xFF1B1B21),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(bottom = 6.dp,start = 16.dp,)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 16.dp,)
                        .fillMaxWidth()
                ){
                    CoilImage(
                        imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/bqlnzkdi_expires_30_days.png"},
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .width(40.dp)
                            .height(40.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .padding(vertical = 10.dp,horizontal = 12.dp,)
                    ){
                        CoilImage(
                            imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/06o6w32p_expires_30_days.png"},
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                            modifier = Modifier
                                .padding(end = 6.dp,)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .width(18.dp)
                                .height(18.dp)
                        )
                        Text("Concluir",
                            color = Color(0xFF3659C9),
                            fontSize = 14.sp,
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 24.dp,start = 24.dp,)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF4F3FA),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .shadow(
                        elevation = 3.dp,
                        spotColor = Color(0x2E000000),
                    )
                    .padding(top = 16.dp,bottom = 16.dp,end = 16.dp,)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(bottom = 6.dp,start = 16.dp,)
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier
                            .padding(end = 8.dp,)
                            .weight(1f)
                    ){
                        Text("Prova de Cálculo II",
                            color = Color(0xFF1B1B21),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text("Cálculo II • Prova",
                            color = Color(0xFF45464F),
                            fontSize = 14.sp,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                color = Color(0xFFC5C6D0),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(shape = RoundedCornerShape(8.dp))
                            .clickable { println("Pressed!") }
                            .padding(vertical = 7.dp,horizontal = 13.dp,)
                    ){
                        Text("Alta · 92",
                            color = Color(0xFF1B1B21),
                            fontSize = 14.sp,
                        )
                    }
                }
                Text("Prazo: 15/10/2026  •  Peso: 5",
                    color = Color(0xFF1B1B21),
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(bottom = 6.dp,start = 16.dp,)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 16.dp,)
                        .fillMaxWidth()
                ){
                    CoilImage(
                        imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/va65hric_expires_30_days.png"},
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .width(40.dp)
                            .height(40.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(20.dp))
                            .padding(vertical = 10.dp,horizontal = 12.dp,)
                    ){
                        CoilImage(
                            imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/4up1r9bp_expires_30_days.png"},
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                            modifier = Modifier
                                .padding(end = 6.dp,)
                                .clip(shape = RoundedCornerShape(20.dp))
                                .width(18.dp)
                                .height(18.dp)
                        )
                        Text("Reabrir",
                            color = Color(0xFF3659C9),
                            fontSize = 14.sp,
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 24.dp,start = 24.dp,)
                    .clip(shape = RoundedCornerShape(12.dp))
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFF4F3FA),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .shadow(
                        elevation = 3.dp,
                        spotColor = Color(0x2E000000),
                    )
                    .padding(vertical = 8.dp,horizontal = 16.dp,)
            ){
                CoilImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/s22ktlsp_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .padding(end = 16.dp,)
                        .clip(shape = RoundedCornerShape(12.dp))
                        .width(24.dp)
                        .height(24.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp,)
                        .weight(1f)
                ){
                    Text("Cálculo II",
                        color = Color(0xFF1B1B21),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("Prof. Helena Prado • 1 avaliação(ões)",
                        color = Color(0xFF45464F),
                        fontSize = 14.sp,
                    )
                }
                CoilImage(
                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/6aavzj9o_expires_30_days.png"},
                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(24.dp))
                        .width(48.dp)
                        .height(48.dp)
                )
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 24.dp,start = 24.dp,)
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(
                        color = Color(0xFF1E293B),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { println("Pressed!") }
                    .padding(vertical = 6.dp,horizontal = 16.dp,)
            ){
                Text("Todas",
                    color = Color(0xFFE2E8F1),
                    fontSize = 14.sp,
                )
            }
            Column(
                modifier = Modifier
                    .padding(start = 24.dp,)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF757780),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(shape = RoundedCornerShape(8.dp))
                    .clickable { println("Pressed!") }
                    .padding(vertical = 7.dp,horizontal = 17.dp,)
            ){
                Text("Todas",
                    color = Color(0xFF45464F),
                    fontSize = 14.sp,
                )
            }
        }
    }
}