package com.example.expensetracker

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDateToHumanReadableForm(dateinMillis: Long): String {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormatter.format(dateinMillis)
    }
}