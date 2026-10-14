package com.kharcha.tracker.presentation.ui.budget

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kharcha.tracker.databinding.FragmentBudgetBinding
import com.kharcha.tracker.databinding.ItemBudgetBinding
import com.kharcha.tracker.domain.model.Budget
import com.kharcha.tracker.domain.model.ExpenseCategory
import com.kharcha.tracker.presentation.viewmodel.MainViewModel
import com.kharcha.tracker.utils.CurrencyUtils
import com.kharcha.tracker.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BudgetFragment : Fragment() {
    private var _b: FragmentBudgetBinding? = null
    private val b get() = _b!!
    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentBudgetBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        b.btnAddBudget.setOnClickListener { showAddDialog() }
        b.btnPrev.setOnClickListener { vm.prevMonth() }
        b.btnNext.setOnClickListener { vm.nextMonth() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { vm.budgets.collect { list -> bindList(list) } }
                launch { vm.goals.collect { /* could show goals section */ } }
                launch { vm.year.collect { b.tvMonth.text = DateUtils.formatMonthYear(it, vm.month.value) } }
                launch { vm.month.collect { b.tvMonth.text = DateUtils.formatMonthYear(vm.year.value, it) } }
            }
        }
    }

    private fun bindList(list: List<Budget>) {
        b.rvBudgets.layoutManager = LinearLayoutManager(requireContext())
        b.rvBudgets.adapter = object : RecyclerView.Adapter<VH>() {
            override fun onCreateViewHolder(p: ViewGroup, v: Int) =
                VH(ItemBudgetBinding.inflate(LayoutInflater.from(p.context), p, false))
            override fun getItemCount() = list.size
            override fun onBindViewHolder(h: VH, pos: Int) = h.bind(list[pos])
        }
    }

    inner class VH(private val ib: ItemBudgetBinding) : RecyclerView.ViewHolder(ib.root) {
        fun bind(budget: Budget) {
            ib.tvTitle.text = budget.category?.displayName ?: "Overall Monthly"
            ib.tvAmount.text = "${CurrencyUtils.format(budget.spent)} / ${CurrencyUtils.format(budget.amount)}"
            ib.tvRemaining.text = "Remaining: ${CurrencyUtils.format(budget.remaining)}"
            ib.progress.progress = (budget.progress * 100).toInt().coerceAtMost(100)
            if (budget.isOver) ib.tvRemaining.setTextColor(0xFFEF4444.toInt())
            ib.btnDelete.setOnClickListener { vm.removeBudget(budget) }
        }
    }

    private fun showAddDialog() {
        val view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null)
        // Simple dialog with amount + optional category
        val et = EditText(requireContext()).apply { hint = "Budget amount (Rs)" }
        val cats = listOf("Overall") + ExpenseCategory.expenseCategories().map { it.displayName }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Set Budget")
            .setView(et)
            .setItems(cats.toTypedArray()) { _, which ->
                val amt = et.text.toString().toDoubleOrNull()
                if (amt == null || amt <= 0) {
                    Toast.makeText(requireContext(), "Invalid amount", Toast.LENGTH_SHORT).show()
                    return@setItems
                }
                val cat = if (which == 0) null else ExpenseCategory.expenseCategories()[which - 1]
                vm.addBudget(amt, cat)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
