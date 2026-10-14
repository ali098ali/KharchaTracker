package com.kharcha.tracker.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kharcha.tracker.R
import com.kharcha.tracker.databinding.ItemTransactionBinding
import com.kharcha.tracker.domain.model.Transaction
import com.kharcha.tracker.domain.model.TransactionType
import com.kharcha.tracker.utils.CategoryColorHelper
import com.kharcha.tracker.utils.CurrencyUtils
import com.kharcha.tracker.utils.DateUtils

class TransactionAdapter(
    private val onClick: (Transaction) -> Unit,
    private val onDelete: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionAdapter.VH>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemTransactionBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(t: Transaction) {
            b.tvCategory.text = t.category.displayName
            b.tvNote.text = t.note.ifBlank { "—" }
            b.tvDate.text = DateUtils.formatDisplay(t.date)
            b.tvMethod.text = t.paymentMethod.displayName
            val color = CategoryColorHelper.getColor(b.root.context, t.category)
            b.viewDot.setBackgroundColor(color)
            val isExpense = t.type == TransactionType.EXPENSE
            b.tvAmount.text = (if (isExpense) "- " else "+ ") + CurrencyUtils.format(t.amount)
            b.tvAmount.setTextColor(
                ContextCompat.getColor(b.root.context, if (isExpense) R.color.expense_red else R.color.success_green)
            )
            b.root.setOnClickListener { onClick(t) }
            b.btnDelete.setOnClickListener { onDelete(t) }
        }
    }

    class Diff : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(a: Transaction, b: Transaction) = a.id == b.id
        override fun areContentsTheSame(a: Transaction, b: Transaction) = a == b
    }
}
