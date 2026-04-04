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
import androidx.compose.ui.res.stringResource
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
    var isLoading by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
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
                        painter = painterResource(id = R.drawable.exercise_ico),
                        contentDescription = "Logo",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.app_name),
                    color = TextMain,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = stringResource(R.string.signup), color = TextGray, fontSize = 16.sp)

                Spacer(modifier = Modifier.height(32.dp))

                InputField(
                    label = stringResource(R.string.name),
                    value = name,
                    onValueChange = { name = it })
                Spacer(modifier = Modifier.height(16.dp))

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
                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    label = stringResource(R.string.confirm_password),
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(16.dp))

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

                        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]+\$".toRegex()
                        val passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}\$".toRegex()

                        if (name.isBlank() || email.isBlank() || password.isBlank()) {
                            errorMessageRes = R.string.fill_all_fields
                            firebaseErrorMessage = null
                        } else if (!email.matches(emailRegex)) {
                            errorMessageRes = R.string.invalid_email
                            firebaseErrorMessage = null
                        } else if (!password.matches(passwordRegex)) {
                            errorMessageRes = R.string.weak_password
                            firebaseErrorMessage = null
                        } else if (password != confirmPassword) {
                            errorMessageRes = R.string.passwords_not_match
                            firebaseErrorMessage = null
                        } else {
                            errorMessageRes = null
                            firebaseErrorMessage = null
                            isLoading = true

                            com.google.firebase.auth.FirebaseAuth.getInstance()
                                .createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        val user = task.result?.user
                                        val profileUpdates =
                                            com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                                .setDisplayName(name)
                                                .build()

                                        user?.updateProfile(profileUpdates)
                                            ?.addOnCompleteListener { profileTask ->
                                                onRegisterSuccess()
                                            }
                                    } else {
                                        firebaseErrorMessage =
                                            (task.exception?.localizedMessage
                                                ?: R.string.unknown_error) as String?
                                        errorMessageRes = null
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
                            text = stringResource(R.string.register),
                            color = ButtonTextLight,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = onNavigateToLogin) {
                    Text(
                        text = stringResource(R.string.already_signed),
                        color = LinkGreen,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}