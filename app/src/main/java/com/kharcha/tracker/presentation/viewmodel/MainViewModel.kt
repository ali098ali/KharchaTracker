package com.kharcha.tracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kharcha.tracker.domain.model.*
import com.kharcha.tracker.domain.usecase.*
import com.kharcha.tracker.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addTxUseCase: AddTransactionUseCase,
    private val updateTxUseCase: UpdateTransactionUseCase,
    private val deleteTxUseCase: DeleteTransactionUseCase,
    private val getByMonthUseCase: GetTransactionsByMonthUseCase,
    private val searchTxUseCase: SearchTransactionsUseCase,
    private val getFilteredUseCase: GetFilteredTransactionsUseCase,
    private val getSummaryUseCase: GetDashboardSummaryUseCase,
    private val getCatsUseCase: GetCategoryTotalsUseCase,
    private val getDailyUseCase: GetDailyTotalsUseCase,
    private val getBudgetsUseCase: GetBudgetsUseCase,
    private val addBudgetUseCase: AddBudgetUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase,
    private val refreshBudgetUseCase: RefreshBudgetSpentUseCase,
    private val getGoalsUseCase: GetGoalsUseCase,
    private val addGoalUseCase: AddGoalUseCase,
    private val updateGoalUseCase: UpdateGoalUseCase,
    private val deleteGoalUseCase: DeleteGoalUseCase
) : ViewModel() {

    private val _year = MutableStateFlow(DateUtils.currentYear())
    private val _month = MutableStateFlow(DateUtils.currentMonth())
    val year = _year.asStateFlow()
    val month = _month.asStateFlow()

    val transactions = _year.flatMapLatest { y ->
        _month.flatMapLatest { m -> getByMonthUseCase(y, m) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _summary = MutableStateFlow(DashboardSummary())
    val summary = _summary.asStateFlow()

    private val _cats = MutableStateFlow<List<CategoryTotal>>(emptyList())
    val categoryTotals = _cats.asStateFlow()

    private val _daily = MutableStateFlow<List<DailyTotal>>(emptyList())
    val dailyTotals = _daily.asStateFlow()

    val budgets = _year.flatMapLatest { y ->
        _month.flatMapLatest { m -> getBudgetsUseCase(y, m) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val goals = getGoalsUseCase().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _searchQuery = MutableStateFlow("")
    val searchResults = _searchQuery.flatMapLatest { q ->
        if (q.isBlank()) flowOf(emptyList()) else searchTxUseCase(q)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _event = MutableStateFlow<String?>(null)
    val event = _event.asStateFlow()

    init { refresh() }

    fun setMonth(y: Int, m: Int) {
        _year.value = y
        _month.value = m
        refresh()
    }

    fun prevMonth() {
        var m = _month.value - 1
        var y = _year.value
        if (m < 0) { m = 11; y-- }
        setMonth(y, m)
    }

    fun nextMonth() {
        var m = _month.value + 1
        var y = _year.value
        if (m > 11) { m = 0; y++ }
        setMonth(y, m)
    }

    fun refresh() {
        viewModelScope.launch {
            val y = _year.value
            val m = _month.value
            _summary.value = getSummaryUseCase(y, m)
            _cats.value = getCatsUseCase(y, m)
            _daily.value = getDailyUseCase(y, m)
            refreshBudgetUseCase(y, m)
        }
    }

    fun addExpense(
        amount: Double,
        cat: ExpenseCategory,
        note: String,
        date: Date,
        method: PaymentMethod = PaymentMethod.CASH
    ) {
        viewModelScope.launch {
            addTxUseCase(
                Transaction(
                    amount = amount,
                    type = TransactionType.EXPENSE,
                    category = cat,
                    note = note,
                    date = date,
                    paymentMethod = method
                )
            )
            refresh()
            _event.value = "Expense added"
        }
    }

    fun addIncome(amount: Double, cat: ExpenseCategory, note: String, date: Date) {
        viewModelScope.launch {
            addTxUseCase(
                Transaction(
                    amount = amount,
                    type = TransactionType.INCOME,
                    category = cat,
                    note = note,
                    date = date
                )
            )
            refresh()
            _event.value = "Income added"
        }
    }

    fun updateTransaction(t: Transaction) {
        viewModelScope.launch {
            updateTxUseCase(t)
            refresh()
            _event.value = "Updated"
        }
    }

    fun deleteTransaction(t: Transaction) {
        viewModelScope.launch {
            deleteTxUseCase(t)
            refresh()
            _event.value = "Deleted"
        }
    }

    fun search(q: String) {
        _searchQuery.value = q
    }

    fun addBudget(amount: Double, category: ExpenseCategory?) {
        viewModelScope.launch {
            addBudgetUseCase(
                Budget(
                    category = category,
                    amount = amount,
                    year = _year.value,
                    month = _month.value
                )
            )
            refresh()
            _event.value = "Budget set"
        }
    }

    fun removeBudget(b: Budget) {
        viewModelScope.launch {
            deleteBudgetUseCase(b)
            _event.value = "Budget removed"
        }
    }

    fun addGoal(title: String, target: Double) {
        viewModelScope.launch {
            addGoalUseCase(Goal(title = title, targetAmount = target))
            _event.value = "Goal added"
        }
    }

    fun contributeToGoal(g: Goal, amount: Double) {
        viewModelScope.launch {
            val updated = g.copy(
                currentAmount = g.currentAmount + amount,
                isCompleted = g.currentAmount + amount >= g.targetAmount
            )
            updateGoalUseCase(updated)
        }
    }

    fun clearEvent() {
        _event.value = null
    }
}
