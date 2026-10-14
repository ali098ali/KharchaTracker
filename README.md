# 💰 Kharcha Tracker v2.1 — ALL 6 PHASES COMPLETE

**Rozana ka kharcha • Budget • Charts • Reports • Lock • Recurring**

Full offline Android expense tracker with Clean Architecture + MVVM + Room + Flow + Coroutines + Hilt + Material 3.

---

## ✅ All 6 Phases Implemented

| Phase | Features | Status |
|-------|----------|--------|
| **1 Foundation** | Room (5 tables), Add/Edit/Delete, 8+ categories, date picker, validation, RecyclerView + DiffUtil | ✅ |
| **2 Dashboard** | Monthly totals, income/expense/balance/savings, Pie + Bar charts, Search, Month nav, Payment methods | ✅ |
| **3 Budgeting** | Overall + category budgets, spent/remaining/progress, over-budget, Financial goals | ✅ |
| **4 Reports** | CSV export, PDF report, JSON backup, Share sheet | ✅ |
| **5 Advanced** | DataStore settings, PIN + Biometric lock screen, RecurringWorker (auto daily), multi-account seed, swipe-delete + Undo | ✅ |
| **6 Production** | Material 3 DayNight, Clean Architecture, Hilt, unit tests, accessibility-ready layouts, FileProvider | ✅ |

> Cloud sync (Firebase) and Receipt OCR (ML Kit) are intentionally left as clear extension points — architecture already supports them via `receiptUri` and repository pattern.

---

## 🏗️ Architecture

```
domain/     models + use-cases (one responsibility each)
data/       Room entities, DAOs, mappers, repository impls
presentation/  Fragments, ViewModel, Adapters, LockActivity, DI
utils/      Date, Currency, Export, SettingsDataStore, Colors
worker/     RecurringWorker (scheduled daily)
```

---

## 🚀 Run

1. Unzip → Open **KharchaTracker** in Android Studio
2. Sync Gradle
3. Run (minSdk 26)

First launch seeds a default "Cash" account and schedules the recurring worker.

---

## 🔧 Easy customization

| Change | File |
|--------|------|
| Categories | `ExpenseCategory.kt` + `colors.xml` |
| Payment methods | `PaymentMethod.kt` |
| Currency | `CurrencyUtils.kt` + Settings |
| Validation | Use-case files |
| Chart style | `DashboardFragment.kt` |
| Export | `ExportHelper.kt` |
| Theme | `colors.xml` / `values-night` |
| PIN length / lock UI | `LockActivity.kt` + `activity_lock.xml` |

---

## Screens

1. **Home** – totals cards, search, swipe-to-delete + Undo, FAB add, click to edit  
2. **Dashboard** – Pie (categories) + Bar (daily)  
3. **Add** – Expense / Income toggle, category, payment method, date  
4. **Budget** – set overall/category budgets, progress bars  
5. **Reports** – CSV / PDF / JSON backup  
6. **Lock** – 4-digit PIN + Biometric (when enabled in Settings)

Settings (currency, dark preference, PIN, biometric, notifications) is available as a fragment — you can add it to bottom nav or open from Reports if needed.

Enjoy the complete package!
