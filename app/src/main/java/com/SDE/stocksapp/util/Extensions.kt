package com.SDE.stocksapp.util

import java.util.Locale

fun String?.formatPrice(): String {
    val value = this?.toDoubleOrNull() ?: 0.0
    return String.format(Locale.US, "$%.2f", value)
}

fun String?.formatPercentage(): String {
    if (this == null || this == "None") return "0.00%"
    val cleanString = this.replace("%", "")
    val value = cleanString.toDoubleOrNull() ?: 0.0
    return String.format(Locale.US, "%.2f%%", value)
}
