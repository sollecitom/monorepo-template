package com.company.libs.chassis.kotlin.extensions.number

import com.company.libs.chassis.kotlin.extensions.text.indexOfOrNull
import java.math.BigDecimal

fun BigDecimal.withPrecision(precision: Int): BigDecimal {
    val plain = movePointRight(precision).toPlainString()
    return BigDecimal(plain.substring(0, plain.indexOfOrNull(".") ?: plain.length)).movePointLeft(precision)
}