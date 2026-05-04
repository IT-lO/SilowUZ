package com.itio.silowuz.screen

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for the isExerciseValid function.
 * 
 * The function validates exercise data:
 * - name must not be empty (must contain at least one character)
 * - reps must be a valid integer
 * - sets must be a valid integer
 */
class PlansScreenTest {

    @Test
    fun isExerciseValid_validInputs_shouldReturnTrue() {
        assertTrue(isExerciseValid("Push Up", "10", "3"))
        assertTrue(isExerciseValid("Sit-up", "20", "4"))
    }

    @Test
    fun isExerciseValid_validNames_withDifferentCases_shouldReturnTrue() {
        assertTrue(isExerciseValid("push up", "10", "3"))
        assertTrue(isExerciseValid("Push Up", "10", "3"))
        assertTrue(isExerciseValid("PUSH UP", "10", "3"))
        assertTrue(isExerciseValid("Przysiady", "15", "3"))
    }

    @Test
    fun isExerciseValid_validNumbers_zeroValues_shouldReturnTrue() {
        assertTrue(isExerciseValid("Test", "0", "0"))
        assertTrue(isExerciseValid("Test", "1", "0"))
        assertTrue(isExerciseValid("Test", "0", "1"))
    }

    @Test
    fun isExerciseValid_validNumbers_differentFormats_shouldReturnTrue() {
        assertTrue(isExerciseValid("Test", "1", "2"))
        assertTrue(isExerciseValid("Test", "10", "20"))
        assertTrue(isExerciseValid("Test", "100", "50"))
    }

    @Test
    fun isExerciseValid_emptyName_shouldReturnFalse() {
        assertFalse(isExerciseValid("", "10", "3"))
    }

    @Test
    fun isExerciseValid_whitespaceOnlyName_shouldReturnFalse() {
        assertFalse(isExerciseValid("   ", "10", "3"))
        assertFalse(isExerciseValid("\t", "10", "3"))
        assertFalse(isExerciseValid("\n", "10", "3"))
        assertFalse(isExerciseValid("  \n  ", "10", "3"))
    }

    @Test
    fun isExerciseValid_emptyReps_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", "", "3"))
    }

    @Test
    fun isExerciseValid_whitespaceOnlyReps_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", "   ", "3"))
        assertFalse(isExerciseValid("Push Up", "\t", "3"))
    }

    @Test
    fun isExerciseValid_nonNumericReps_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", "abc", "3"))
        assertFalse(isExerciseValid("Push Up", "10abc", "3"))
        assertFalse(isExerciseValid("Push Up", "1.5", "3"))
        assertFalse(isExerciseValid("Push Up", "10x", "3"))
    }

    @Test
    fun isExerciseValid_emptySets_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", "10", ""))
    }

    @Test
    fun isExerciseValid_whitespaceOnlySets_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", "10", "   "))
        assertFalse(isExerciseValid("Push Up", "10", "\t"))
    }

    @Test
    fun isExerciseValid_nonNumericSets_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", "10", "abc"))
        assertFalse(isExerciseValid("Push Up", "10", "3xyz"))
        assertFalse(isExerciseValid("Push Up", "10", "3.5"))
    }


    @Test
    fun isExerciseValid_allFieldsInvalid_shouldReturnFalse() {
        assertFalse(isExerciseValid("", "", ""))
        assertFalse(isExerciseValid("   ", "abc", "xyz"))
    }

    @Test
    fun isExerciseValid_nameValid_butRepsInvalid_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", "abc", "3"))
        assertFalse(isExerciseValid("Push Up", "", "3"))
    }

    @Test
    fun isExerciseValid_nameAndRepsValid_butSetsInvalid_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", "10", "abc"))
        assertFalse(isExerciseValid("Push Up", "10", ""))
    }

    @Test
    fun isExerciseValid_repsAndSetsValid_butNameInvalid_shouldReturnFalse() {
        assertFalse(isExerciseValid("", "10", "3"))
        assertFalse(isExerciseValid("   ", "10", "3"))
    }

    @Test
    fun isExerciseValid_specialCharactersInName_shouldReturnTrue() {
        assertTrue(isExerciseValid("!@#$%", "10", "3"))
        assertTrue(isExerciseValid("Übung", "10", "3"))
    }

    @Test
    fun isExerciseValid_numericWithSpaces_shouldReturnFalse() {
        assertFalse(isExerciseValid("Push Up", " 10", "3"))
        assertFalse(isExerciseValid("Push Up", "10 ", "3"))
    }

    @Test
    fun isExerciseValid_emptyStringArguments_shouldReturnFalse() {
        assertFalse(isExerciseValid("", "", ""))
    }
}