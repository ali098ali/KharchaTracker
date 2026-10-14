package com.kharcha.tracker.domain.model

import java.util.Date

data class Goal(
    val id: Long = 0,
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: Date? = null,
    val isCompleted: Boolean = false
) {
    val progress: Float get() = if (targetAmount > 0) (currentAmount / targetAmount).toFloat().coerceIn(0f, 1f) else 0f
    val remaining: Double get() = (targetAmount - currentAmount).coerceAtLeast(0.0)
}
