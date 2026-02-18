package io.github.mikaojk.validator

import java.time.LocalDate

val lookup1: IntArray = intArrayOf(3, 7, 6, 1, 8, 9, 4, 5, 2, 0)
val lookup2: IntArray = intArrayOf(5, 4, 3, 2, 7, 6, 5, 4, 3, 2)

fun validateSocialSecurityNumberMod11(socialSecurityNumber: String): Boolean {
    if (socialSecurityNumber.length != 11) return false

    return validateMod11Old(socialSecurityNumber) || validateMod112032(socialSecurityNumber)
}

private fun validateMod11Old(socialSecurityNumber: String): Boolean {
    var checksum1 = 0
    var checksum2 = 0

    for (i in 0..9) {
        val currNum = (socialSecurityNumber[i] - '0')
        checksum1 += currNum * lookup1[i]
        checksum2 += currNum * lookup2[i]
    }

    checksum1 %= 11
    checksum2 %= 11

    val checksum1Final = if (checksum1 == 0) 0 else 11 - checksum1
    val checksum2Final = if (checksum2 == 0) 0 else 11 - checksum2

    return checksum1Final != 10 &&
        socialSecurityNumber[9] - '0' == checksum1Final &&
        socialSecurityNumber[10] - '0' == checksum2Final
}


private fun validateMod112032(socialSecurityNumber: String): Boolean {
    val digits = socialSecurityNumber.map { it - '0' }.toIntArray()
    val givenK1 = digits[9]
    val givenK2 = digits[10]

    var weightedK1 = 0
    for (i in 0..8) {
        weightedK1 += digits[i] * lookup1[i]
    }

    val remainderK1 = (weightedK1 + givenK1) % 11
    val validRemaindersK1 = setOf(0, 1, 2, 3)
    if (remainderK1 !in validRemaindersK1) {
        return false
    }

    var weightedK2 = 0
    for (i in 0..9) {
        weightedK2 += digits[i] * lookup2[i]
    }

    val remainderK2 = (weightedK2 + givenK2) % 11
    return remainderK2 == 0
}

private fun validatePersonAndPersonDNumberRange(socialSecurityNumber: String): Boolean {
    val socialSecurityNumberBornDay = socialSecurityNumber.substring(0, 2)
    val socialSecurityNumberMonth = socialSecurityNumber.substring(2, 4)
    val isDayValid = validateSocialSecurityNumberRange(socialSecurityNumberBornDay) ||
        validateSocialSecurityDNumberRange(socialSecurityNumberBornDay)
    val isMonthValid = validateMonthRange(socialSecurityNumberMonth)
    return isDayValid && isMonthValid
}

fun validateMonthRange(monthString: String): Boolean {
    val month = monthString.toInt()
    return month in 1..12 || month in 66..77 || month in 81..92
}

fun validateSocialSecurityAndDNumber(personNumber: String?): Boolean =
    personNumber != null &&
        validateSocialSecurityNumberMod11(personNumber) &&
        validatePersonAndPersonDNumberRange(personNumber)

fun validateSocialSecurityAndDNumber11Digits(personNumber: String): Boolean =
    personNumber.length == 11

fun validateSocialSecurityNumberRange(personNumberFirstAndSecoundChar: String): Boolean {
    return personNumberFirstAndSecoundChar.toInt() in 1..31
}

fun validateSocialSecurityDNumberRange(socialSecurityNumberFirstAndSecoundChar: String): Boolean {
    return socialSecurityNumberFirstAndSecoundChar.toInt() in 41..71
}

fun extractBornDate(personIdent: String): LocalDate =
    LocalDate.of(
        extractBornYear(personIdent),
        extractBornMonth(personIdent),
        extractBornDay(personIdent)
    )

fun extractBornYear(socialSecurityNumber: String): Int {
    val lastTwoDigitsOfYear = extractLastTwoDigistOfyear(socialSecurityNumber)
    val individualDigits = extractIndividualDigits(socialSecurityNumber)
    if (lastTwoDigitsOfYear in (0..99) && individualDigits in (0..499)) {
        return 1900 + lastTwoDigitsOfYear
    }

    if (lastTwoDigitsOfYear in (54..99) && individualDigits in 500..749) {
        return 1800 + lastTwoDigitsOfYear
    }

    if (lastTwoDigitsOfYear in (0..39) && individualDigits in 500..999) {
        return 2000 + lastTwoDigitsOfYear
    }
    return 1900 + lastTwoDigitsOfYear
}

fun extractBornDay(socialSecurityNumber: String): Int {
    val day = socialSecurityNumber.substring(0..1).toInt()
    return if (day < 40) day else day - 40
}

fun extractBornMonth(socialSecurityNumber: String): Int {
    val month = socialSecurityNumber.substring(2..3).toInt()
    return when {
        month >= 80 -> month - 80
        month >= 65 -> month - 65
        else -> month
    }
}

fun extractIndividualDigits(socialSecurityNumber: String): Int =
    socialSecurityNumber.substring(6, 9).toInt()

fun extractLastTwoDigistOfyear(socialSecurityNumber: String): Int =
    socialSecurityNumber.substring(4, 6).toInt()
