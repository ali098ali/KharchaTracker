package com.kharcha.tracker.presentation.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.kharcha.tracker.R
import com.kharcha.tracker.databinding.FragmentDashboardBinding
import com.kharcha.tracker.presentation.viewmodel.MainViewModel
import com.kharcha.tracker.utils.CategoryColorHelper
import com.kharcha.tracker.utils.CurrencyUtils
import com.kharcha.tracker.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private var _b: FragmentDashboardBinding? = null
    private val b get() = _b!!
    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentDashboardBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        setupCharts()
        b.btnPrev.setOnClickListener { vm.prevMonth() }
        b.btnNext.setOnClickListener { vm.nextMonth() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { vm.summary.collect { s ->
                    b.tvExpense.text = CurrencyUtils.format(s.monthlyExpense)
                    b.tvIncome.text = CurrencyUtils.format(s.monthlyIncome)
                    b.tvBalance.text = CurrencyUtils.format(s.balance)
                    b.tvSavings.text = CurrencyUtils.format(s.savings)
                }}
                launch { vm.categoryTotals.collect { updatePie(it) } }
                launch { vm.dailyTotals.collect { updateBar(it) } }
                launch { vm.year.collect { b.tvMonth.text = DateUtils.formatMonthYear(it, vm.month.value) } }
                launch { vm.month.collect { b.tvMonth.text = DateUtils.formatMonthYear(vm.year.value, it) } }
            }
        }
    }

    private fun setupCharts() {
        b.pieChart.apply {
            description.isEnabled = false; setUsePercentValues(true)
            holeRadius = 55f; setHoleColor(Color.TRANSPARENT); legend.isEnabled = true
            animateY(800, Easing.EaseInOutQuad)
        }
        b.barChart.apply {
            description.isEnabled = false; setDrawGridBackground(false); legend.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM; xAxis.setDrawGridLines(false)
            axisLeft.axisMinimum = 0f; axisRight.isEnabled = false
            animateY(800, Easing.EaseInOutQuad)
        }
    }

    private fun updatePie(list: List<com.kharcha.tracker.domain.model.CategoryTotal>) {
        if (list.isEmpty()) { b.pieChart.clear(); b.tvPieEmpty.isVisible = true; return }
        b.tvPieEmpty.isVisible = false
        val entries = list.map { PieEntry(it.total.toFloat(), it.category.displayName) }
        val colors = list.map { CategoryColorHelper.getColor(requireContext(), it.category) }
        val ds = PieDataSet(entries, "").apply { this.colors = colors; valueTextColor = Color.WHITE; valueTextSize = 11f }
        b.pieChart.data = PieData(ds).apply { setValueFormatter(PercentFormatter(b.pieChart)) }
        b.pieChart.invalidate()
    }

    private fun updateBar(list: List<com.kharcha.tracker.domain.model.DailyTotal>) {
        if (list.isEmpty()) { b.barChart.clear(); b.tvBarEmpty.isVisible = true; return }
        b.tvBarEmpty.isVisible = false
        val entries = list.map { BarEntry(it.day.toFloat(), it.total.toFloat()) }
        val labels = list.map { it.day.toString() }
        val ds = BarDataSet(entries, "").apply {
            color = ContextCompat.getColor(requireContext(), R.color.primary)
            valueTextSize = 9f
        }
        b.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        b.barChart.data = BarData(ds).apply { barWidth = 0.55f }
        b.barChart.invalidate()
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
