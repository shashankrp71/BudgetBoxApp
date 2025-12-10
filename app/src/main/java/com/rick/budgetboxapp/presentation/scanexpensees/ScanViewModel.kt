package com.rick.budgetboxapp.presentation.scanexpensees

import android.app.Application
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.rick.budgetboxapp.domain.repository.ExpenseRepository
import com.rick.budgetboxapp.domain.use_case.AddExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    app: Application,
    val addExpenseUseCase: AddExpenseUseCase
) : AndroidViewModel(app) {

    val context = app.applicationContext

    var loading by mutableStateOf(false)
    var error by mutableStateOf("")

    fun processImage(
        bitmap: Bitmap,
        callback: (String, Double, String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                loading = true
                error = ""

                val image = InputImage.fromBitmap(bitmap, 0)
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                val result = recognizer.process(image).await()

                val extractedText = result.text


                val amount = extractAmount(extractedText)
                val title = extractTitle(extractedText)
                val category = guessCategory(extractedText)

                callback(title, amount, category)

            } catch (e: Exception) {
                error = "Failed to read text: ${e.message}"
            } finally {
                loading = false
            }
        }
    }

    private fun extractAmount(text: String): Double {

        val keywords = listOf(
            "total", "net total", "grand total", "amount payable",
            "payable", "to pay", "balance", "balance due"
        )

        val lines = text.lowercase().split("\n")

        // STEP 1 → Search lines containing total keywords
        val totalLine = lines.firstOrNull { line ->
            keywords.any { keyword -> line.contains(keyword) }
        }

        if (totalLine != null) {
            val amount = extractNumberFromLine(totalLine)
            if (amount > 0) return amount
        }

        // STEP 2 → Fallback: return the largest amount in whole receipt
        return extractLargestNumber(text)
    }
    private fun extractNumberFromLine(line: String): Double {
        val regex = Regex("(₹|rs\\.?\\s*)?\\d{1,3}(,\\d{3})*(\\.\\d{1,2})?")
        val match = regex.find(line) ?: return 0.0

        return match.value
            .replace("₹", "")
            .replace("rs", "")
            .replace("rs.", "")
            .replace(",", "")
            .trim()
            .toDoubleOrNull() ?: 0.0
    }

    private fun extractLargestNumber(text: String): Double {
        val regex = Regex("(₹|rs\\.?\\s*)?\\d{1,3}(,\\d{3})*(\\.\\d{1,2})?")
        val matches = regex.findAll(text).map {
            it.value
                .replace("₹", "")
                .replace("rs", "")
                .replace("rs.", "")
                .replace(",", "")
                .trim()
                .toDoubleOrNull() ?: 0.0
        }.toList()

        return matches.maxOrNull() ?: 0.0
    }


    private fun extractTitle(text: String): String {
        return text.lines().firstOrNull()?.take(30) ?: "Scanned Item"
    }

    private fun guessCategory(text: String): String {
        return when {
            text.contains("food", true) -> "Food"
            text.contains("uber", true) || text.contains("ola", true) -> "Transport"
            text.contains("shop", true) -> "Shopping"
            else -> "Other"
        }
    }

    fun addExpenses(title: String, amount: Double, category: String){
        viewModelScope.launch {
            addExpenseUseCase(title,amount,category)
        }
    }
}
