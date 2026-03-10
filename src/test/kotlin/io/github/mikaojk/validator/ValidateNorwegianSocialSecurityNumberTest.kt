package io.github.mikaojk.validator

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ValidateNorwegianSocialSecurityNumberTest {

    @Test
    internal fun shouldValidateTrueForValidatePersonAndDNumber11Digits() {
        val validationResult = validateSocialSecurityAndDNumber11Digits("01013999900")

        assertEquals(true, validationResult)
    }

    @Test
    internal fun shouldValidateFalseForValidatePersonAndDNumber11Digits() {
        val validationResult = validateSocialSecurityAndDNumber11Digits("0101399990")

        assertEquals(false, validationResult)
    }

    @Test
    internal fun shouldValidate2032StyleNumbersWithNewAlgorithm() {
        val valid2032Number = "20036914700"
        assertEquals(true, validateSocialSecurityNumber(valid2032Number))
        assertEquals(true, validateSocialSecurityAndDNumber(valid2032Number))
    }

    @Test
    internal fun shouldStillValidateOldStyleNumbers() {
        val oldStyleNumber = "07049111198"
        assertEquals(true, validateSocialSecurityNumber(oldStyleNumber))
        assertEquals(true, validateSocialSecurityAndDNumber(oldStyleNumber))
    }

    @Test
    internal fun shouldValidate2032OnlyNumbers() {
        val new2032OnlyNumber = "01010000048"
        assertEquals(true, validateSocialSecurityNumber(new2032OnlyNumber))
        assertEquals(true, validateSocialSecurityAndDNumber(new2032OnlyNumber))
    }




    @Test
    internal fun shouldValidateMonthRangeForRegularNumbers() {
        assertEquals(true, validateMonthRange("01"))
        assertEquals(true, validateMonthRange("06"))
        assertEquals(true, validateMonthRange("12"))
        assertEquals(false, validateMonthRange("00"))
        assertEquals(false, validateMonthRange("13"))
    }

    @Test
    internal fun shouldValidateMonthRangeForHelsenettSyntheticNumbers() {
        assertEquals(true, validateMonthRange("66", allowSynthetic = true))
        assertEquals(true, validateMonthRange("71", allowSynthetic = true))
        assertEquals(true, validateMonthRange("77", allowSynthetic = true))
        assertEquals(false, validateMonthRange("66", allowSynthetic = false))
        assertEquals(false, validateMonthRange("65", allowSynthetic = true))
        assertEquals(false, validateMonthRange("78", allowSynthetic = true))
    }

    @Test
    internal fun shouldValidateMonthRangeForOtherSyntheticNumbers() {
        assertEquals(true, validateMonthRange("81", allowSynthetic = true))
        assertEquals(true, validateMonthRange("86", allowSynthetic = true))
        assertEquals(true, validateMonthRange("92", allowSynthetic = true))
        assertEquals(false, validateMonthRange("81", allowSynthetic = false))
        assertEquals(false, validateMonthRange("80", allowSynthetic = true))
        assertEquals(false, validateMonthRange("93", allowSynthetic = true))
    }

    @Test
    internal fun shouldValidateMonthRangeForInvalidInput() {
        assertEquals(false, validateMonthRange("XX"))
        assertEquals(false, validateMonthRange(""))
        assertEquals(false, validateMonthRange("1A"))
    }

    @Test
    internal fun shouldRejectSyntheticFnrByDefaultInValidateSocialSecurityNumber() {
        val syntheticFnr = "01810011129"
        assertEquals(false, validateSocialSecurityNumber(syntheticFnr))
        assertEquals(false, validateSocialSecurityNumber(syntheticFnr, allowSynthetic = false))
    }

    @Test
    internal fun shouldAcceptSyntheticFnrWhenAllowSyntheticIsTrueInValidateSocialSecurityNumber() {
        val syntheticFnr = "01810011129"
        assertEquals(true, validateSocialSecurityNumber(syntheticFnr, allowSynthetic = true))
    }

    @Test
    internal fun shouldRejectSyntheticFnrByDefault() {
        val syntheticFnr = "01810011129"
        assertEquals(false, validateSocialSecurityAndDNumber(syntheticFnr))
        assertEquals(false, validateSocialSecurityAndDNumber(syntheticFnr, allowSynthetic = false))
    }

    @Test
    internal fun shouldAcceptSyntheticFnrWhenAllowSyntheticIsTrue() {
        val syntheticFnr = "01810011129"
        assertEquals(true, validateSocialSecurityAndDNumber(syntheticFnr, allowSynthetic = true))
    }

    @Test
    internal fun shouldAcceptHelsenettSyntheticFnrWhenAllowSyntheticIsTrue() {
        val helsenettSyntheticFnr = "01660011168"
        assertEquals(
            true,
            validateSocialSecurityAndDNumber(helsenettSyntheticFnr, allowSynthetic = true),
        )
        assertEquals(
            false,
            validateSocialSecurityAndDNumber(helsenettSyntheticFnr, allowSynthetic = false),
        )
    }

    @Test
    internal fun shouldStillAcceptRegularFnrWhenAllowSyntheticIsTrue() {
        val regularFnr = "07049111198"
        assertEquals(true, validateSocialSecurityAndDNumber(regularFnr, allowSynthetic = true))
        assertEquals(true, validateSocialSecurityAndDNumber(regularFnr, allowSynthetic = false))
    }
}

private fun generateSocialSecurityNumber(bornDate: LocalDate, useDNumber: Boolean = false): String {
    val socialSecurityNumberDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy")
    val personDate =
        bornDate.format(socialSecurityNumberDateFormat).let {
            if (useDNumber) "${it[0] + 4}${it.substring(1)}" else it
        }
    return (if (bornDate.year >= 2000) (75011..99999) else (11111..50099))
        .map { "$personDate$it" }
        .first { validateSocialSecurityAndDNumber(it) }
}
