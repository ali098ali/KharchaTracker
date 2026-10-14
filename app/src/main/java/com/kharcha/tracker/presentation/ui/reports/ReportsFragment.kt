package com.kharcha.tracker.presentation.ui.reports

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kharcha.tracker.databinding.FragmentReportsBinding
import com.kharcha.tracker.presentation.viewmodel.MainViewModel
import com.kharcha.tracker.utils.ExportHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportsFragment : Fragment() {
    private var _b: FragmentReportsBinding? = null
    private val b get() = _b!!
    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        _b = FragmentReportsBinding.inflate(i, c, false)
        return b.root
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        b.btnCsv.setOnClickListener { exportCsv() }
        b.btnPdf.setOnClickListener { exportPdf() }
        b.btnBackup.setOnClickListener { backup() }
    }

    private fun exportCsv() {
        viewLifecycleOwner.lifecycleScope.launch {
            val list = vm.transactions.first()
            val file = ExportHelper.exportCsv(requireContext(), list)
            shareFile(file, "text/csv")
            Toast.makeText(requireContext(), "CSV saved: ${file.name}", Toast.LENGTH_LONG).show()
        }
    }

    private fun exportPdf() {
        viewLifecycleOwner.lifecycleScope.launch {
            val list = vm.transactions.first()
            val file = ExportHelper.exportPdf(requireContext(), list, "Expense Report")
            shareFile(file, "application/pdf")
            Toast.makeText(requireContext(), "PDF saved: ${file.name}", Toast.LENGTH_LONG).show()
        }
    }

    private fun backup() {
        viewLifecycleOwner.lifecycleScope.launch {
            val list = vm.transactions.first()
            val file = ExportHelper.backupJson(requireContext(), list)
            shareFile(file, "application/json")
            Toast.makeText(requireContext(), "Backup: ${file.name}", Toast.LENGTH_LONG).show()
        }
    }

    private fun shareFile(file: java.io.File, mime: String) {
        val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.provider", file)
        startActivity(Intent(Intent.ACTION_SEND).apply {
            type = mime; putExtra(Intent.EXTRA_STREAM, uri); addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        })
    }

    override fun onDestroyView() { super.onDestroyView(); _b = null }
}
