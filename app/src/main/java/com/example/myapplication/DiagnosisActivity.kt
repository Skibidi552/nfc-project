package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DiagnosisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnosis)

        val diagnosisText = findViewById<TextView>(R.id.textViewDiagnosisContent)

        // Lấy từ NFC
        val diagnosisFromNFC = intent.getStringExtra("DIAGNOSIS_DATA") ?: "No data"

        // Hiển thị
        diagnosisText.text = diagnosisFromNFC

        // Lưu lại
        val prefs = getSharedPreferences("patient_data", Context.MODE_PRIVATE)
        prefs.edit().putString("diagnosis_data", diagnosisFromNFC).apply()
    }
}
