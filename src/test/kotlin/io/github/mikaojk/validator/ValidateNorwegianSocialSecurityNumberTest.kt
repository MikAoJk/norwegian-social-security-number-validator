package io.github.mikaojk.validator

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ValidateNorwegianSocialSecurityNumberTest {

    @Test
    internal fun testShouldExtractBornYearFrom1854To1899() {
        val extractBornYear1 = extractBornYear("01015450000")
        val extractBornYear2 = extractBornYear("01015474900")
        val extractBornYear3 = extractBornYear("01019950000")
        val extractBornYear4 = extractBornYear("01019974900")

        assertEquals(1854, extractBornYear1)
        assertEquals(1854, extractBornYear2)
        assertEquals(1899, extractBornYear3)
        assertEquals(1899, extractBornYear4)
    }

    @Test
    internal fun shouldExtractBornYearFrom1900To1999() {
        val extractBornYear1 = extractBornYear("01010000000")
        val extractBornYear2 = extractBornYear("01010049900")
        val extractBornYear3 = extractBornYear("01019900000")
        val extractBornYear4 = extractBornYear("01019949900")

        assertEquals(1900, extractBornYear1)
        assertEquals(1900, extractBornYear2)
        assertEquals(1999, extractBornYear3)
        assertEquals(1999, extractBornYear4)
    }

    @Test
    internal fun shouldExtractBornYearFrom1940To1999() {
        val extractBornYear1 = extractBornYear("01014090000")
        val extractBornYear2 = extractBornYear("01014099900")
        val extractBornYear3 = extractBornYear("01019990000")
        val extractBornYear4 = extractBornYear("01019999900")

        assertEquals(1940, extractBornYear1)
        assertEquals(1940, extractBornYear2)
        assertEquals(1999, extractBornYear3)
        assertEquals(1999, extractBornYear4)
    }

    @Test
    internal fun shouldExtractBornYearFrom2000To2039() {
        val extractBornYear1 = extractBornYear("01010050000")
        val extractBornYear2 = extractBornYear("01010099900")
        val extractBornYear3 = extractBornYear("01013950000")
        val extractBornYear4 = extractBornYear("01013999900")

        assertEquals(2000, extractBornYear1)
        assertEquals(2000, extractBornYear2)
        assertEquals(2039, extractBornYear3)
        assertEquals(2039, extractBornYear4)
    }

    @Test
    internal fun shouldExtractBornYearFrom1991() {
        val extractBornYear =
            extractBornYear(generateSocialSecurityNumber(LocalDate.of(1991, 4, 7)))

        assertEquals(1991, extractBornYear)
    }

    @Test
    internal fun shouldExtractBornDateFrom1991() {
        val bornDate19910407 = LocalDate.of(1991, 4, 7)
        val extractBornDate = extractBornDate(generateSocialSecurityNumber(bornDate19910407))

        assertEquals(bornDate19910407, extractBornDate)
    }

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
        assertEquals(true, validateSocialSecurityNumberMod11(valid2032Number))
        assertEquals(true, validateSocialSecurityAndDNumber(valid2032Number))
    }

    @Test
    internal fun shouldStillValidateOldStyleNumbers() {
        val oldStyleNumber = "07049111198"
        assertEquals(true, validateSocialSecurityNumberMod11(oldStyleNumber))
        assertEquals(true, validateSocialSecurityAndDNumber(oldStyleNumber))
    }

    @Test
    internal fun shouldValidate2032OnlyNumbers() {
        val new2032OnlyNumber = "01010000048"
        assertEquals(true, validateSocialSecurityNumberMod11(new2032OnlyNumber))
        assertEquals(true, validateSocialSecurityAndDNumber(new2032OnlyNumber))
    }

    @Test
    internal fun shouldExtractMonthFromSyntheticNumber() {
        val syntheticNumber = "01810012345"
        assertEquals(1, extractBornMonth(syntheticNumber))
        
        val syntheticDec = "01920012345"
        assertEquals(12, extractBornMonth(syntheticDec))
    }

    @Test
    internal fun shouldExtractMonthFromRegularNumber() {
        val regularNumber = "01010012345" 
        assertEquals(1, extractBornMonth(regularNumber))
        
        val regularDec = "01120012345"
        assertEquals(12, extractBornMonth(regularDec))
    }

    @Test
    internal fun shouldExtractMonthFromHelsenettSyntheticNumber() {
        val syntheticNumber = "01660012345"
        assertEquals(1, extractBornMonth(syntheticNumber))
        
        val syntheticDec = "01770012345"
        assertEquals(12, extractBornMonth(syntheticDec))
        
        val syntheticJune = "01710012345"
        assertEquals(6, extractBornMonth(syntheticJune))
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
        assertEquals(true, validateMonthRange("66"))
        assertEquals(true, validateMonthRange("71"))
        assertEquals(true, validateMonthRange("77"))
        assertEquals(false, validateMonthRange("65"))
        assertEquals(false, validateMonthRange("78"))
    }

    @Test
    internal fun shouldValidateMonthRangeForOtherSyntheticNumbers() {
        assertEquals(true, validateMonthRange("81"))
        assertEquals(true, validateMonthRange("86"))
        assertEquals(true, validateMonthRange("92"))
        assertEquals(false, validateMonthRange("80"))
        assertEquals(false, validateMonthRange("93"))
    }

    @Test
    internal fun shouldValidateMonthRangeForInvalidInput() {
        assertEquals(false, validateMonthRange("XX"))
        assertEquals(false, validateMonthRange(""))
        assertEquals(false, validateMonthRange("1A"))
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
