package com.itio.silowuz.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itio.silowuz.R

val BgGradientStart = Color(0xFFF0FDF4)
val BgGradientEnd = Color(0xFFD0FAE5)
val CardBg = Color(0xFFFFFFFF)
val BorderColor = Color.Black.copy(alpha = 0.1f)
val TextMain = Color(0xFF0A0A0A)
val TextGray = Color(0xFF717182)
val InputBg = Color(0xFFF3F3F5)
val ButtonGreen = Color(0xFF16A34A)
val ButtonTextLight = Color(0xFFFFFFFF)
val LinkGreen = Color(0xFF00A63E)
val LogoGradientStart = Color(0xFF00C950)
val LogoGradientEnd = Color(0xFF009966)

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onNavigateToRegister: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessageRes by remember { mutableStateOf<Int?>(null) }
    var firebaseErrorMessage by remember { mutableStateOf<String?>(null) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(BgGradientStart, BgGradientEnd))),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .background(CardBg, RoundedCornerShape(14.dp))
                .border(1.dp, BorderColor, RoundedCornerShape(14.dp))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    LogoGradientStart,
                                    LogoGradientEnd
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = R.drawable.exercise_ico),
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "SiłowUZ",
                    color = TextMain,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = stringResource(R.string.signin), color = TextGray, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(32.dp))

                InputField(
                    label = stringResource(R.string.email),
                    value = email,
                    onValueChange = { email = it })

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    label = stringResource(R.string.password),
                    value = password,
                    onValueChange = { password = it },
                    isPassword = true
                )

                errorMessageRes?.let { errorId ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = stringResource(id = errorId), color = Color.Red, fontSize = 12.sp)
                }

                firebaseErrorMessage?.let { errorText ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorText, color = Color.Red, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (isLoading) return@Button

                        if (email.isBlank() || password.isBlank()) {
                            errorMessageRes = R.string.fill_all_fields
                        } else {
                            errorMessageRes = null
                            isLoading = true

                            com.google.firebase.auth.FirebaseAuth.getInstance()
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        onLoginSuccess()
                                    } else {
                                        firebaseErrorMessage =
                                            (task.exception?.localizedMessage
                                                ?: R.string.unknown_error) as String?
                                    }
                                }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonGreen)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = ButtonTextLight,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Zaloguj",
                            color = ButtonTextLight,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text = "Nie masz konta?",
                        color = LinkGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String?,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    placeholder: String = ""
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (label != null) {
            Text(text = label, color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(color = TextMain, fontSize = 14.sp),
            singleLine = true,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(InputBg, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(text = placeholder, color = TextGray, fontSize = 14.sp)
                    }
                    innerTextField()
                }
            }
        )
    }
}