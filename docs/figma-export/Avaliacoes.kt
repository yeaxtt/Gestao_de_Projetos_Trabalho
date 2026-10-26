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
fun Avaliacoes() {
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
            CoilImage(
                imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/6m16cnk2_expires_30_days.png"},
                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                modifier = Modifier
                    .height(69.dp)
                    .fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .padding(bottom = 55.dp,)
                    .fillMaxWidth()
                    .padding(top = 16.dp,start = 16.dp,end = 16.dp,)
            ){
                Column(
                    modifier = Modifier
                        .padding(bottom = 10.dp,)
                        .fillMaxWidth()
                ){
                    Text("Avaliações",
                        color = Color(0xFF1B1B21),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text("Filtre e acompanhe todos os prazos.",
                        color = Color(0xFF45464F),
                        fontSize = 14.sp,
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(bottom = 10.dp,)
                        .fillMaxWidth()
                        .padding(top = 2.dp,)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(bottom = 8.dp,end = 36.dp,)
                            .fillMaxWidth()
                    ){
                        Column(
                            modifier = Modifier
                                .padding(end = 8.dp,)
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
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(end = 8.dp,)
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
                            Text("Pendentes",
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
                            Text("Concluídas",
                                color = Color(0xFF45464F),
                                fontSize = 14.sp,
                            )
                        }
                    }
                    Box{
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
                                Text("Todas as matérias",
                                    color = Color(0xFF1B1B21),
                                    fontSize = 16.sp,
                                )
                                CoilImage(
                                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/7r8o46cd_expires_30_days.png"},
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
                                    color = Color(0xFFFAF8FF),
                                )
                                .padding(horizontal = 4.dp,)
                        ){
                            Text("Matéria",
                                color = Color(0xFF45464F),
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(bottom = 10.dp,)
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
                            imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/2xquq5ns_expires_30_days.png"},
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
                                imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/c44i0wta_expires_30_days.png"},
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
                        .padding(bottom = 10.dp,)
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
                            Text("Trabalho de Banco de Dados",
                                color = Color(0xFF1B1B21),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text("Banco de Dados • Trabalho",
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
                            Text("Média · 68",
                                color = Color(0xFF1B1B21),
                                fontSize = 14.sp,
                            )
                        }
                    }
                    Text("Prazo: 23/10/2026  •  Peso: 3.5",
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
                            imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/0b01af4q_expires_30_days.png"},
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
                                imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/4xtps0ny_expires_30_days.png"},
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
                        .padding(top = 16.dp,start = 16.dp,end = 16.dp,)
                ){
                    Box(
                        modifier = Modifier
                            .padding(bottom = 32.dp,)
                    ){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ){
                                Column(
                                    modifier = Modifier
                                        .padding(end = 8.dp,)
                                        .weight(1f)
                                ){
                                    Text("Seminário de Redes",
                                        color = Color(0xFF1B1B21),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                    Text("Redes de Computadores • Seminário",
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
                                    Text("Baixa · 41",
                                        color = Color(0xFF1B1B21),
                                        fontSize = 14.sp,
                                    )
                                }
                            }
                        }
                        Text("Prazo: 12/11/2026  •  Peso: 2",
                            color = Color(0xFF1B1B21),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .offset(x = 0.dp, y = 26.dp)
                                .align(Alignment.BottomStart)
                                .padding(bottom = 26.dp)
                        )
                        Column(
                            modifier = Modifier
                                .offset(x = 12.dp, y = 23.dp)
                                .align(Alignment.BottomEnd)
                                .padding(start = 12.dp, bottom = 23.dp)
                                .clip(shape = RoundedCornerShape(16.dp))
                                .background(
                                    color = Color(0xFF1E293B),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .shadow(
                                    elevation = 3.dp,
                                    spotColor = Color(0x33000000),
                                )
                                .clickable { println("Pressed!") }
                                .padding(16.dp)
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ){
                                CoilImage(
                                    imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/6skcpa36_expires_30_days.png"},
                                    imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                    modifier = Modifier
                                        .padding(end = 12.dp,)
                                        .clip(shape = RoundedCornerShape(16.dp))
                                        .width(24.dp)
                                        .height(24.dp)
                                )
                                Text("Nova avaliação",
                                    color = Color(0xFFE2E8F1),
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        CoilImage(
                            imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/a9dqg9m0_expires_30_days.png"},
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(20.dp))
                                .width(40.dp)
                                .height(11.dp)
                        )
                        Column(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(20.dp))
                                .width(101.dp)
                                .height(11.dp)
                        ){
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp,)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    Column(
                        modifier = Modifier
                            .clip(shape = RoundedCornerShape(12.dp))
                            .height(148.dp)
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFF4F3FA),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .shadow(
                                elevation = 3.dp,
                                spotColor = Color(0x2E000000),
                            )
                    ){
                    }
                }
                Column(
                    modifier = Modifier
                        .offset(x = -16.dp, y = -55.dp)
                        .align(Alignment.TopStart)
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFFEEEDF4),
                        )
                        .padding(vertical = 14.dp,horizontal = 8.dp,)
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(end = 8.dp,)
                                .weight(1f)
                        ){
                            CoilImage(
                                imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/yuiery7u_expires_30_days.png"},
                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                modifier = Modifier
                                    .padding(bottom = 4.dp,)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .width(64.dp)
                                    .height(32.dp)
                            )
                            Text("Início",
                                color = Color(0xFF45464F),
                                fontSize = 12.sp,
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(end = 8.dp,)
                                .weight(1f)
                        ){
                            CoilImage(
                                imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/bbnfmnhq_expires_30_days.png"},
                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                modifier = Modifier
                                    .padding(bottom = 4.dp,)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .width(64.dp)
                                    .height(32.dp)
                            )
                            Text("Matérias",
                                color = Color(0xFF45464F),
                                fontSize = 12.sp,
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                        ){
                            CoilImage(
                                imageModel = {"https://storage.googleapis.com/tagjs-prod.appspot.com/v1/GjRrvCaSIN/mnkczqmn_expires_30_days.png"},
                                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                                modifier = Modifier
                                    .padding(bottom = 4.dp,)
                                    .clip(shape = RoundedCornerShape(16.dp))
                                    .width(64.dp)
                                    .height(32.dp)
                            )
                            Text("Avaliações",
                                color = Color(0xFF1B1B21),
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}