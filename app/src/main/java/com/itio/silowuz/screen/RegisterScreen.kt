package com.itio.silowuz.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itio.silowuz.R

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onNavigateToLogin: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(BgGradientStart, BgGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .fillMaxWidth()
                .background(CardBg, RoundedCornerShape(14.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(LogoGradientStart, LogoGradientEnd))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.exercise_ico),
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "SiłowUZ", color = TextMain, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "Zarejestruj się", color = TextGray, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(32.dp))

                InputField(label = "Imię", value = name, onValueChange = { name = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputField(label = "Email", value = email, onValueChange = { email = it })
                Spacer(modifier = Modifier.height(16.dp))

                InputField(label = "Hasło", value = password, onValueChange = { password = it }, isPassword = true)
                Spacer(modifier = Modifier.height(16.dp))

                InputField(label = "Potwierdź hasło", value = confirmPassword, onValueChange = { confirmPassword = it }, isPassword = true)
                Spacer(modifier = Modifier.height(16.dp))

                errorMessage?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]+\$".toRegex()

                        if (name.isBlank() || email.isBlank() || password.isBlank()) {
                            errorMessage = "Wypełnij wszystkie pola"
                        } else if (!email.matches(emailRegex)) {
                            errorMessage = "Podaj poprawny adres e-mail"
                        } else if (password.length < 6) {
                            errorMessage = "Hasło musi mieć co najmniej 6 znaków"
                        } else if (password != confirmPassword) {
                            errorMessage = "Hasła nie są takie same"
                        } else {
                            errorMessage = null
                            onRegisterSuccess()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen)
                ) {
                    Text(text = "Zarejestruj", color = ButtonTextLight, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text(text = "Masz już konto?", color = LinkGreen, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}