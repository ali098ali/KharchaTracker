package com.kharcha.tracker.presentation.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kharcha.tracker.databinding.FragmentSettingsBinding
import com.kharcha.tracker.utils.SettingsDataStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _b: FragmentSettingsBinding? = null
    private val b get() = _b!!
    private lateinit var settings: SettingsDataStore

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentSettingsBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        settings = SettingsDataStore(requireContext())
        val currencies = listOf("PKR", "USD", "EUR", "GBP", "INR")
        b.actvCurrency.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, currencies))

        viewLifecycleOwner.lifecycleScope.launch {
            b.actvCurrency.setText(settings.currency.first(), false)
            b.switchDark.isChecked = settings.darkMode.first()
            b.switchNotif.isChecked = settings.notificationsEnabled.first()
            b.switchPin.isChecked = settings.pinEnabled.first()
            b.switchBio.isChecked = settings.biometricEnabled.first()
        }

        b.actvCurrency.setOnItemClickListener { _, _, pos, _ ->
            lifecycleScope.launch { settings.setCurrency(currencies[pos]) }
        }
        b.switchDark.setOnCheckedChangeListener { _, checked ->
            lifecycleScope.launch { settings.setDarkMode(checked) }
            Toast.makeText(requireContext(), "Restart app to apply theme fully", Toast.LENGTH_SHORT).show()
        }
        b.switchNotif.setOnCheckedChangeListener { _, c -> lifecycleScope.launch { settings.setNotifications(c) } }
        b.switchPin.setOnCheckedChangeListener { _, c -> lifecycleScope.launch { settings.setPinEnabled(c) } }
        b.switchBio.setOnCheckedChangeListener { _, c -> lifecycleScope.launch { settings.setBiometric(c) } }

        b.btnAbout.setOnClickListener {
            Toast.makeText(requireContext(), "Expense Tracker v2.1\nClean Architecture + Room + Charts", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
