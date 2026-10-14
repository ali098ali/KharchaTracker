package com.kharcha.tracker.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.kharcha.tracker.R
import com.kharcha.tracker.domain.model.ExpenseCategory

object CategoryColorHelper {
    fun getColor(ctx: Context, cat: ExpenseCategory): Int {
        val id = when (cat) {
            ExpenseCategory.FOOD -> R.color.cat_food
            ExpenseCategory.TRANSPORT -> R.color.cat_transport
            ExpenseCategory.SHOPPING -> R.color.cat_shopping
            ExpenseCategory.BILLS -> R.color.cat_bills
            ExpenseCategory.ENTERTAINMENT -> R.color.cat_entertainment
            ExpenseCategory.HEALTH -> R.color.cat_health
            ExpenseCategory.EDUCATION -> R.color.cat_education
            ExpenseCategory.SALARY -> R.color.cat_salary
            ExpenseCategory.FREELANCE -> R.color.cat_freelance
            ExpenseCategory.INVESTMENT -> R.color.cat_investment
            ExpenseCategory.OTHER -> R.color.cat_other
        }
        return ContextCompat.getColor(ctx, id)
    }
}
