package com.itio.silowuz.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel(){
    private val auth = FirebaseAuth.getInstance()
    var currentUser = mutableStateOf<FirebaseUser?>(auth.currentUser)


}