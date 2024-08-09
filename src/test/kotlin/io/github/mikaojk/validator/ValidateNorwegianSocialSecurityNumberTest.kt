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
