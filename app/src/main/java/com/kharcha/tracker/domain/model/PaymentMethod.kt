package com.kharcha.tracker.domain.model

enum class PaymentMethod(val displayName: String) {
    CASH("Cash"),
    BANK("Bank Account"),
    CARD("Credit/Debit Card"),
    JAZZCASH("JazzCash"),
    EASYPAISA("Easypaisa"),
    OTHER("Other");

    companion object {
        fun fromName(name: String): PaymentMethod =
            entries.find { it.name.equals(name, true) } ?: CASH
    }
}
