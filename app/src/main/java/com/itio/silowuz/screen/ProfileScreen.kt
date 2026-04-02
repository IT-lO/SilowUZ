package com.itio.silowuz.screen

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import com.itio.silowuz.R

@Composable
fun ProfileScreen(paddingValues: PaddingValues, onLogout: () -> Unit){
    val currentLocales = AppCompatDelegate.getApplicationLocales()
    val isCurrentlyPolish = currentLocales.toLanguageTags().contains("pl")
    var isPolish by remember { mutableStateOf(isCurrentlyPolish) }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        IconButton(
            onClick = {
                isPolish = !isPolish
                val newLang = if (isPolish) "pl" else "en"
                val appLocale = LocaleListCompat.forLanguageTags(newLang)
                AppCompatDelegate.setApplicationLocales(appLocale)
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(
                    id = if (isPolish) R.drawable.ic_flag_pl else R.drawable.ic_flag_en
                ),
                contentDescription = stringResource(R.string.toggle_language),
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = stringResource(R.string.profile_screen))
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onLogout,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = stringResource(R.string.logout))
        }
    }

}