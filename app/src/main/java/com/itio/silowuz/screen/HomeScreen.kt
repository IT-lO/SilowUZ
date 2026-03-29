package com.itio.silowuz.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itio.silowuz.ui.theme.SubTextGray
import com.itio.silowuz.ui.theme.MainGreen
import com.itio.silowuz.ui.theme.SecondaryGreen
import com.itio.silowuz.ui.theme.White

@Composable
fun HomeScreen(paddingValues: PaddingValues){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.TopCenter
    ){
        Column{
            // WELCOME
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(MainGreen, SecondaryGreen),
                            start = Offset(0f, 0f),
                            end = Offset(1000f, 1000f) // Adjust as needed
                        )
                    )
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                contentAlignment = Alignment.TopStart
            ){
                Column {
                    Text(
                        text = "Witaj, Użytkowniku!",
                        color = White,
                        fontSize = 24.sp
                    )
                    Text(
                        text = "niedziela, 29 marca 2026",
                        color = White,
                        fontSize = 14.sp
                    )
                }
            }

            // STEPS
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .border(1.dp, MainGreen, RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                contentAlignment = Alignment.TopStart
            ){
                Column {
                    Text(
                        text = "Dzisiejsze kroki",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ){
                        Column{
                            Text(
                                text = "3000",
                                color = MainGreen,
                                fontSize = 48.sp
                            )
                            Text(
                                text = "Cel: 10 000 kroków",
                                color = SubTextGray,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { 0.5f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50)),
                        color = MainGreen,
                        trackColor = SecondaryGreen.copy(alpha = 0.3f),
                        strokeCap = StrokeCap.Butt,
                        gapSize = 0.dp,
                        drawStopIndicator = {}
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row{
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = White,
                                contentColor = MainGreen)
                        ) {
                            Text(
                                text = "+100 kroków (Test)"
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MainGreen,
                                contentColor = White)
                        ) {
                            Text(
                                text = "Stop Tracking"
                            )
                        }
                    }
                }
            }

            // STATS
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ){
                Column() {
                    Row() {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    MainGreen,
                                    RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
                                )
                                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            contentAlignment = Alignment.TopStart
                        )
                        {
                            Column() {
                                Text(
                                    text = "120",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold

                                )
                                Text(
                                    text = "kcal",
                                    color = SubTextGray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    MainGreen,
                                    RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
                                )
                                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            contentAlignment = Alignment.TopStart
                        )
                        {
                            Column() {
                                Text(
                                    text = "2.40",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold

                                )
                                Text(
                                    text = "km",
                                    color = SubTextGray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row() {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    MainGreen,
                                    RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
                                )
                                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            contentAlignment = Alignment.TopStart
                        )
                        {
                            Column() {
                                Text(
                                    text = "7",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold

                                )
                                Text(
                                    text = "Seria treningów dni",
                                    color = SubTextGray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    MainGreen,
                                    RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp)
                                )
                                .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            contentAlignment = Alignment.TopStart
                        )
                        {
                            Column() {
                                Text(
                                    text = "45",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold

                                )
                                Text(
                                    text = "Aktywne minuty",
                                    color = SubTextGray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // BAR CHART

        }
    }
}