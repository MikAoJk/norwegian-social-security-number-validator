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
        // Test 2032-style numbers that pass the new validation but not the old
        // These numbers have valid K1 remainders (0,1,2,3) and K2 remainder (0)
        
        // Valid 2032-style fødselsnummer from reference implementation
        val valid2032Number = "20036914700"
        assertEquals(true, validateSocialSecurityNumberMod11(valid2032Number))
        assertEquals(true, validateSocialSecurityAndDNumber(valid2032Number))
    }

    @Test
    internal fun shouldStillValidateOldStyleNumbers() {
        // Ensure backward compatibility - old numbers should still work
        val oldStyleNumber = "07049111198"
        assertEquals(true, validateSocialSecurityNumberMod11(oldStyleNumber))
        assertEquals(true, validateSocialSecurityAndDNumber(oldStyleNumber))
    }

    @Test
    internal fun shouldValidate2032OnlyNumbers() {
        // Test a number that is valid ONLY under 2032 rules, not old rules
        val new2032OnlyNumber = "01010000048"
        assertEquals(true, validateSocialSecurityNumberMod11(new2032OnlyNumber))
        assertEquals(true, validateSocialSecurityAndDNumber(new2032OnlyNumber))
    }

    @Test
    internal fun shouldExtractMonthFromSyntheticNumber() {
        // Synthetic numbers have +80 in month position
        // Month 01 becomes 81 in synthetic number
        val syntheticNumber = "01810012345" // January (01) + 80 = 81
        assertEquals(1, extractBornMonth(syntheticNumber))
        
        // Month 12 becomes 92 in synthetic number
        val syntheticDec = "01920012345" // December (12) + 80 = 92
        assertEquals(12, extractBornMonth(syntheticDec))
    }

    @Test
    internal fun shouldExtractMonthFromRegularNumber() {
        // Regular numbers should still work
        val regularNumber = "01010012345" // January (01)
        assertEquals(1, extractBornMonth(regularNumber))
        
        val regularDec = "01120012345" // December (12)
        assertEquals(12, extractBornMonth(regularDec))
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
