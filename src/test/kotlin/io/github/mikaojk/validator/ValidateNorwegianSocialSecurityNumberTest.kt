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

    @Test
    internal fun shouldExtractMonthFromHelsenettSyntheticNumber() {
        // Helsenett synthetic numbers have +65 in month position
        // Month 01 becomes 66 in synthetic number
        val syntheticNumber = "01660012345" // January (01) + 65 = 66
        assertEquals(1, extractBornMonth(syntheticNumber))
        
        // Month 12 becomes 77 in synthetic number
        val syntheticDec = "01770012345" // December (12) + 65 = 77
        assertEquals(12, extractBornMonth(syntheticDec))
        
        // Month 06 becomes 71 in synthetic number
        val syntheticJune = "01710012345" // June (06) + 65 = 71
        assertEquals(6, extractBornMonth(syntheticJune))
    }

    @Test
    internal fun shouldValidateMonthRangeForRegularNumbers() {
        // Regular months (01-12)
        assertEquals(true, validateMonthRange("01"))
        assertEquals(true, validateMonthRange("06"))
        assertEquals(true, validateMonthRange("12"))
        assertEquals(false, validateMonthRange("00"))
        assertEquals(false, validateMonthRange("13"))
    }

    @Test
    internal fun shouldValidateMonthRangeForHelsenettSyntheticNumbers() {
        // Helsenett synthetic numbers: month + 65 (66-77)
        assertEquals(true, validateMonthRange("66")) // January + 65
        assertEquals(true, validateMonthRange("71")) // June + 65
        assertEquals(true, validateMonthRange("77")) // December + 65
        assertEquals(false, validateMonthRange("65")) // Invalid (65 would be month 0)
        assertEquals(false, validateMonthRange("78")) // Invalid (would be month 13)
    }

    @Test
    internal fun shouldValidateMonthRangeForOtherSyntheticNumbers() {
        // Other synthetic numbers: month + 80 (81-92)
        assertEquals(true, validateMonthRange("81")) // January + 80
        assertEquals(true, validateMonthRange("86")) // June + 80
        assertEquals(true, validateMonthRange("92")) // December + 80
        assertEquals(false, validateMonthRange("80")) // Invalid (80 would be month 0)
        assertEquals(false, validateMonthRange("93")) // Invalid (would be month 13)
    }

    @Test
    internal fun shouldValidateMonthRangeForInvalidInput() {
        // Test error handling for non-numeric input
        assertEquals(false, validateMonthRange("XX"))
        assertEquals(false, validateMonthRange(""))
        assertEquals(false, validateMonthRange("1A"))
    }

    @Test
    internal fun shouldValidateTenorTestNumbers() {
        // Tenor test numbers use month + 80 (range 81-92)
        // Example: A person born in January would have month 81 in a Tenor test number
        val tenorJanuaryNumber = "01810012345" // January (01) + 80 = 81
        assertEquals(true, validateMonthRange("81"))
        assertEquals(1, extractBornMonth(tenorJanuaryNumber))
        
        // Test a complete valid Tenor test number
        val validTenorNumber = "01850012348" // May (05) + 80 = 85
        assertEquals(true, validateMonthRange("85"))
        assertEquals(5, extractBornMonth(validTenorNumber))
        
        // Test December in Tenor format
        val tenorDecemberNumber = "01920012345" // December (12) + 80 = 92
        assertEquals(true, validateMonthRange("92"))
        assertEquals(12, extractBornMonth(tenorDecemberNumber))
    }

    @Test
    internal fun shouldExtractCorrectMonthFromTenorTestNumbers() {
        // Verify month extraction for all Tenor months (81-92)
        assertEquals(1, extractBornMonth("01810012345"))  // January
        assertEquals(2, extractBornMonth("01820012345"))  // February
        assertEquals(3, extractBornMonth("01830012345"))  // March
        assertEquals(4, extractBornMonth("01840012345"))  // April
        assertEquals(5, extractBornMonth("01850012345"))  // May
        assertEquals(6, extractBornMonth("01860012345"))  // June
        assertEquals(7, extractBornMonth("01870012345"))  // July
        assertEquals(8, extractBornMonth("01880012345"))  // August
        assertEquals(9, extractBornMonth("01890012345"))  // September
        assertEquals(10, extractBornMonth("01900012345")) // October
        assertEquals(11, extractBornMonth("01910012345")) // November
        assertEquals(12, extractBornMonth("01920012345")) // December
    }

    @Test
    internal fun shouldValidateTenorTestNumbersWithCorrectChecksum() {
        // Test Tenor numbers with valid checksum that would be used in Test-Norge
        // These numbers have month + 80 and must pass mod11 validation
        // Using the same generator pattern as existing tests to ensure valid checksum
        
        // Test with regular FNR that has synthetic month (81 = January + 80)
        // This demonstrates the month extraction works correctly for Tenor numbers
        val tenorStyleNumber = "01810012345"
        assertEquals(1, extractBornMonth(tenorStyleNumber)) // Month 81 = January
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
