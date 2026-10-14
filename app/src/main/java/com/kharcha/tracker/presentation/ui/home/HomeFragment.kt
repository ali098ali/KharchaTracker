package com.kharcha.tracker.presentation.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kharcha.tracker.R
import com.kharcha.tracker.databinding.FragmentHomeBinding
import com.kharcha.tracker.domain.model.Transaction
import com.kharcha.tracker.presentation.ui.adapter.TransactionAdapter
import com.kharcha.tracker.presentation.viewmodel.MainViewModel
import com.kharcha.tracker.utils.CurrencyUtils
import com.kharcha.tracker.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _b: FragmentHomeBinding? = null
    private val b get() = _b!!
    private val vm: MainViewModel by activityViewModels()
    private lateinit var adapter: TransactionAdapter
    private var lastDeleted: Transaction? = null

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentHomeBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        adapter = TransactionAdapter(
            onClick = { t ->
                val bundle = Bundle().apply { putLong("txId", t.id) }
                findNavController().navigate(R.id.editFragment, bundle)
            },
            onDelete = { t -> performDelete(t) }
        )
        b.rv.layoutManager = LinearLayoutManager(requireContext())
        b.rv.adapter = adapter
        setupSwipeToDelete()

        b.btnPrev.setOnClickListener { vm.prevMonth() }
        b.btnNext.setOnClickListener { vm.nextMonth() }
        b.fabAdd.setOnClickListener { findNavController().navigate(R.id.action_home_to_add) }

        b.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = true.also { vm.search(q.orEmpty()) }
            override fun onQueryTextChange(q: String?) = true.also {
                if (q.isNullOrBlank()) { /* month list restores via flow */ }
                else vm.search(q)
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { vm.transactions.collect { list ->
                    if (b.searchView.query.isNullOrBlank()) {
                        adapter.submitList(list)
                        b.tvEmpty.isVisible = list.isEmpty()
                    }
                }}
                launch { vm.summary.collect { s ->
                    b.tvExpense.text = CurrencyUtils.format(s.monthlyExpense)
                    b.tvIncome.text = CurrencyUtils.format(s.monthlyIncome)
                    b.tvBalance.text = CurrencyUtils.format(s.balance)
                }}
                launch { vm.year.collect { y -> b.tvMonth.text = DateUtils.formatMonthYear(y, vm.month.value) } }
                launch { vm.month.collect { m -> b.tvMonth.text = DateUtils.formatMonthYear(vm.year.value, m) } }
                launch { vm.event.collect { e -> e?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    vm.clearEvent()
                }}}
                launch { vm.searchResults.collect {
                    if (!b.searchView.query.isNullOrBlank()) adapter.submitList(it)
                }}
            }
        }
    }

    private fun setupSwipeToDelete() {
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                val pos = vh.bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val item = adapter.currentList.getOrNull(pos) ?: return
                    performDelete(item)
                }
            }
        })
        helper.attachToRecyclerView(b.rv)
    }

    private fun performDelete(t: Transaction) {
        lastDeleted = t
        vm.deleteTransaction(t)
        Snackbar.make(b.root, "Expense deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                lastDeleted?.let { deleted ->
                    vm.addExpense(
                        deleted.amount, deleted.category, deleted.note,
                        deleted.date, deleted.paymentMethod
                    )
                    lastDeleted = null
                }
            }.show()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
