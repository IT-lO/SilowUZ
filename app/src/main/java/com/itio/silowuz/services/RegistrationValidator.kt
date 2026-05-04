package com.itio.silowuz.services

import com.itio.silowuz.R
/**
 * Validates the input fields provided during user registration.
 * Checks for empty fields, proper email format, password strength (minimum 8 characters,
 * at least one uppercase letter and one digit), and password matching.
 *
 * @param name The user's chosen display name
 * @param email The user's email address
 * @param pass The user's chosen password
 * @param confirmPass The password confirmation to check against the chosen password
 * @return The String resource ID (Int) representing the specific validation error, or null if all inputs are valid
 */
fun validateRegistrationInput(name: String, email: String, pass: String, confirmPass: String): Int? {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z]+\$".toRegex()
    val passwordRegex = "^(?=.*[A-Z])(?=.*\\d).{8,}\$".toRegex()

    return when {
        name.isBlank() || email.isBlank() || pass.isBlank() -> R.string.fill_all_fields
        !email.matches(emailRegex) -> R.string.invalid_email
        !pass.matches(passwordRegex) -> R.string.weak_password
        pass != confirmPass -> R.string.passwords_not_match
        else -> null
    }
}