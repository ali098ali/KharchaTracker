package com.kharcha.tracker.presentation.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kharcha.tracker.databinding.FragmentAddBinding
import com.kharcha.tracker.domain.model.ExpenseCategory
import com.kharcha.tracker.domain.model.PaymentMethod
import com.kharcha.tracker.domain.model.TransactionType
import com.kharcha.tracker.presentation.viewmodel.MainViewModel
import com.kharcha.tracker.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddFragment : Fragment() {
    private var _b: FragmentAddBinding? = null
    private val b get() = _b!!
    private val vm: MainViewModel by activityViewModels()
    private var date = Date()
    private var type = TransactionType.EXPENSE
    private var cat = ExpenseCategory.FOOD
    private var method = PaymentMethod.CASH

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentAddBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        b.rgType.setOnCheckedChangeListener { _, id ->
            type = if (id == b.rbIncome.id) TransactionType.INCOME else TransactionType.EXPENSE
            setupCategories()
        }
        setupCategories()
        setupMethods()
        b.tvDate.text = DateUtils.formatDisplay(date)
        b.btnDate.setOnClickListener {
            val c = Calendar.getInstance().apply { time = date }
            DatePickerDialog(requireContext(), { _, y, m, d ->
                c.set(y, m, d); date = c.time; b.tvDate.text = DateUtils.formatDisplay(date)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }
        b.btnSave.setOnClickListener { save() }
        b.btnCancel.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupCategories() {
        val list = if (type == TransactionType.INCOME) ExpenseCategory.incomeCategories() else ExpenseCategory.expenseCategories()
        val names = list.map { it.displayName }
        b.actvCat.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, names))
        b.actvCat.setText(list.first().displayName, false)
        b.actvCat.setOnItemClickListener { _, _, pos, _ -> cat = list[pos] }
    }

    private fun setupMethods() {
        val names = PaymentMethod.entries.map { it.displayName }
        b.actvMethod.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, names))
        b.actvMethod.setText(PaymentMethod.CASH.displayName, false)
        b.actvMethod.setOnItemClickListener { _, _, pos, _ -> method = PaymentMethod.entries[pos] }
    }

    private fun save() {
        val amt = b.etAmount.text?.toString()?.toDoubleOrNull()
        if (amt == null || amt <= 0) {
            b.tilAmount.error = "Enter valid amount"; return
        }
        b.tilAmount.error = null
        val note = b.etNote.text?.toString().orEmpty()
        if (type == TransactionType.INCOME) vm.addIncome(amt, cat, note, date)
        else vm.addExpense(amt, cat, note, date, method)
        Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
