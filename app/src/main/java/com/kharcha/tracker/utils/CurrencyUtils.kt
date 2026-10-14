package com.kharcha.tracker.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    private val fmt = NumberFormat.getNumberInstance(Locale("en", "PK")).apply {
        maximumFractionDigits = 0
    }

    fun format(amount: Double, currency: String = "PKR"): String {
        val symbol = when (currency) {
            "PKR" -> "Rs"
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            else -> currency
        }
        return "$symbol ${fmt.format(amount)}"
    }

    fun formatCompact(amount: Double): String = when {
        amount >= 100_000 -> "Rs ${"%.1f".format(amount / 100_000)}L"
        amount >= 1_000 -> "Rs ${"%.1f".format(amount / 1_000)}K"
        else -> format(amount)
    }
}
