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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.itio.silowuz.R
import com.itio.silowuz.ui.theme.SubTextGray
import com.itio.silowuz.ui.theme.MainGreen
import com.itio.silowuz.ui.theme.SecondaryGreen
import com.itio.silowuz.ui.theme.White

lateinit var barEntriesList: ArrayList<BarEntry>

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
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
            ) {
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
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.todays_steps),
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column {
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
                        progress = { 0.3f },
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

                    Row {
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = White,
                                contentColor = MainGreen
                            )
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
                                contentColor = White
                            )
                        ) {
                            Text(
                                text = stringResource(R.string.stop_tracking)
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
            ) {
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
                                    text = stringResource(R.string.kcal),
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
                                    text = stringResource(R.string.training_day_streak),
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
                                    text = stringResource(R.string.active_minutes),
                                    color = SubTextGray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // BAR CHART
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .border(1.dp, MainGreen, RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 15.dp, 15.dp))
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Column() {
                    Text(
                        text = stringResource(R.string.weekly_progress),
                        fontSize = 16.sp
                    )

                    StepsBarChartData()
                    AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        factory = {
                            BarChart(context).apply {
                                val labels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

                                val dataSet = BarDataSet(barEntriesList, "").apply {
                                    color = MainGreen.toArgb()
                                    valueTextColor = MainGreen.toArgb()
                                    valueTextSize = 12f
                                }

                                data = BarData(dataSet)

                                description.isEnabled = false
                                legend.isEnabled = false
                                axisRight.isEnabled = false

                                xAxis.apply {
                                    position = XAxis.XAxisPosition.BOTTOM
                                    setDrawGridLines(false)
                                    granularity = 1f
                                    isGranularityEnabled = true

                                    valueFormatter = object : ValueFormatter() {
                                        override fun getAxisLabel(
                                            value: Float,
                                            axis: AxisBase?
                                        ): String {
                                            return labels.getOrNull(value.toInt()) ?: ""
                                        }
                                    }
                                }
                                invalidate()
                            }
                        }
                    )
                }
            }
        }
    }
}

fun StepsBarChartData() {
    barEntriesList = ArrayList()
    barEntriesList.add(BarEntry(0f, 100f))
    barEntriesList.add(BarEntry(1f, 200f))
    barEntriesList.add(BarEntry(2f, 300f))
    barEntriesList.add(BarEntry(3f, 400f))
    barEntriesList.add(BarEntry(4f, 500f))
    barEntriesList.add(BarEntry(5f, 600f))
    barEntriesList.add(BarEntry(6f, 700f))
}

