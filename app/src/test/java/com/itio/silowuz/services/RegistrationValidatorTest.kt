package com.itio.silowuz.services

import org.junit.Test
import org.junit.Assert.*
import com.itio.silowuz.R


class RegistrationValidatorTest {
    @Test
    fun validate_emptyFields_returnsFillAllFieldsError() {
        val result = validateRegistrationInput("", "test@email.com", "Password123", "Password123")
        assertEquals(R.string.fill_all_fields, result)
    }

    @Test
    fun validate_invalidEmailFormat_returnsInvalidEmailError() {
        val result = validateRegistrationInput("Adam", "invalid-email@", "Password123", "Password123")
        assertEquals(R.string.invalid_email, result)
    }

    @Test
    fun validate_weakPasswordNoUppercase_returnsWeakPasswordError() {
        val result = validateRegistrationInput("Adam", "test@email.com", "password123", "password123")
        assertEquals(R.string.weak_password, result)
    }

    @Test
    fun validate_weakPasswordNoDigit_returnsWeakPasswordError() {
        val result = validateRegistrationInput("Adam", "test@email.com", "Passwordddd", "Passwordddd")
        assertEquals(R.string.weak_password, result)
    }

    @Test
    fun validate_weakPasswordTooShort_returnsWeakPasswordError() {
        val result = validateRegistrationInput("Adam", "test@email.com", "Pass1", "Pass1")
        assertEquals(R.string.weak_password, result)
    }

    @Test
    fun validate_passwordsDoNotMatch_returnsPasswordsNotMatchError() {
        val result = validateRegistrationInput("Adam", "test@email.com", "Password123", "Password456")
        assertEquals(R.string.passwords_not_match, result)
    }

    @Test
    fun validate_correctInputs_returnsNull() {
        val result = validateRegistrationInput("Adam", "test@email.com", "Password123", "Password123")
        assertNull(result)
    }
}