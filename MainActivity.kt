package com.example.emicalculator

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This line connects the Kotlin code to your XML file
        setContentView(R.layout.activity_main)

        val etAmount = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etAmount)
        val etInterest = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etInterest)
        val etTenure = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etTenure)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        val tvResult = findViewById<TextView>(R.id.tvResult)

        btnCalculate.setOnClickListener {
            val p = etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val annualRate = etInterest.text.toString().toDoubleOrNull() ?: 0.0
            val n = etTenure.text.toString().toDoubleOrNull() ?: 0.0

            if (p > 0 && annualRate > 0 && n > 0) {
                val r = annualRate / 12 / 100
                val emi = (p * r * (1 + r).pow(n)) / ((1 + r).pow(n) - 1)
                tvResult.text = "Monthly EMI: ${String.format("%.2f", emi)}"
            } else {
                tvResult.text = "Please enter all values"
            }
        }
    }
}