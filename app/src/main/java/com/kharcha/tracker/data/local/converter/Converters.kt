package com.kharcha.tracker.data.local.converter

import androidx.room.TypeConverter
import com.kharcha.tracker.domain.model.ExpenseCategory
import com.kharcha.tracker.domain.model.PaymentMethod
import com.kharcha.tracker.domain.model.TransactionType

class Converters {
    @TypeConverter fun fromType(t: TransactionType) = t.name
    @TypeConverter fun toType(s: String) = TransactionType.valueOf(s)

    @TypeConverter fun fromCategory(c: ExpenseCategory) = c.name
    @TypeConverter fun toCategory(s: String) = ExpenseCategory.fromName(s)

    @TypeConverter fun fromPayment(p: PaymentMethod) = p.name
    @TypeConverter fun toPayment(s: String) = PaymentMethod.fromName(s)
}
