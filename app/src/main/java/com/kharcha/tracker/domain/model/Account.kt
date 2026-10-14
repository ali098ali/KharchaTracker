package com.kharcha.tracker.domain.model

data class Account(
    val id: Long = 0,
    val name: String,
    val type: PaymentMethod = PaymentMethod.CASH,
    val balance: Double = 0.0,
    val currency: String = "PKR",
    val isDefault: Boolean = false
)
