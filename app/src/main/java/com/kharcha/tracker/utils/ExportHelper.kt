package com.kharcha.tracker.utils

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import com.kharcha.tracker.domain.model.Transaction
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ExportHelper {

    fun exportCsv(context: Context, list: List<Transaction>): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val file = File(dir, "expense_export_${System.currentTimeMillis()}.csv")
        file.bufferedWriter().use { w ->
            w.appendLine("id,amount,type,category,note,date,paymentMethod")
            val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            list.forEach { t ->
                w.appendLine("${t.id},${t.amount},${t.type},${t.category},${t.note.replace(",", " ")},${fmt.format(t.date)},${t.paymentMethod}")
            }
        }
        return file
    }

    fun exportPdf(context: Context, list: List<Transaction>, title: String): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val file = File(dir, "expense_report_${System.currentTimeMillis()}.pdf")
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        var page = doc.startPage(pageInfo)
        var canvas = page.canvas
        val paint = Paint().apply { textSize = 12f }
        val titlePaint = Paint().apply { textSize = 18f; isFakeBoldText = true }
        var y = 40f
        canvas.drawText(title, 40f, y, titlePaint)
        y += 30f
        canvas.drawText("Generated: ${Date()}", 40f, y, paint)
        y += 25f
        val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        list.take(40).forEach { t ->
            if (y > 800) {
                doc.finishPage(page)
                page = doc.startPage(pageInfo)
                canvas = page.canvas
                y = 40f
            }
            canvas.drawText(
                "${fmt.format(t.date)} | ${t.category.displayName} | ${CurrencyUtils.format(t.amount)} | ${t.note.take(30)}",
                40f, y, paint
            )
            y += 18f
        }
        doc.finishPage(page)
        FileOutputStream(file).use { doc.writeTo(it) }
        doc.close()
        return file
    }

    fun backupJson(context: Context, list: List<Transaction>): File {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: context.filesDir
        val file = File(dir, "expense_backup_${System.currentTimeMillis()}.json")
        val sb = StringBuilder()
        sb.append("[")
        list.forEachIndexed { i, t ->
            if (i > 0) sb.append(",")
            sb.append("""{"id":${t.id},"amount":${t.amount},"type":"${t.type}","category":"${t.category}","note":"${t.note.replace("\"","'")}","date":${t.date.time},"payment":"${t.paymentMethod}"}""")
        }
        sb.append("]")
        file.writeText(sb.toString())
        return file
    }
}
