package com.itio.silowuz.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.itio.silowuz.R
import com.itio.silowuz.data.StepRepository
import com.itio.silowuz.ui.theme.MainGreen
import com.itio.silowuz.ui.theme.SecondaryGreen
import com.itio.silowuz.ui.theme.SubTextGray
import java.util.prefs.Preferences

class AppWidget() : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val todaysStepsLabel = context.getString(R.string.todays_steps)

        provideContent {
            GlanceTheme{
                MyContent(todaysStepsLabel, context)
            }
        }
    }

    @Composable
    private fun MyContent(todaysStepsLabel: String, context: Context) {
        val stepRepository = remember { StepRepository.getInstance(context) }
        val steps = stepRepository.getTodaySteps()
        val stepGoal = 10000


        Box(
            modifier = GlanceModifier
                .cornerRadius(15.dp)
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .background(GlanceTheme.colors.surface),
            contentAlignment = Alignment.TopStart
        ) {
            Column(
                modifier = GlanceModifier
                    .padding(horizontal = 15.dp, vertical = 10.dp)
            ) {
                Text(
                    text = todaysStepsLabel,
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = GlanceTheme.colors.inverseSurface
                    )
                )

                Spacer(modifier = GlanceModifier.height(8.dp))

                Box(
                    modifier = GlanceModifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = steps.toString(),
                            style = TextStyle(
                                color = ColorProvider(day = MainGreen, night = MainGreen),
                                fontSize = 48.sp),
                        )
                        Text(
                            text = "Goal: $stepGoal steps",
                            style = TextStyle(
                                color = ColorProvider(day = SubTextGray, night = SubTextGray),
                                fontSize = 12.sp)
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(8.dp))

                LinearProgressIndicator(
                    progress = steps.toFloat() / stepGoal.toFloat(),
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .cornerRadius(15.dp),
                    color = ColorProvider(day = MainGreen, night = MainGreen),
                    backgroundColor = ColorProvider(day = SecondaryGreen.copy(alpha = 0.3f), night = SecondaryGreen.copy(alpha = 0.3f))
                )
            }
        }
    }

    suspend fun updateStepWidget(context: Context) {
        AppWidget().updateAll(context)
    }
}
