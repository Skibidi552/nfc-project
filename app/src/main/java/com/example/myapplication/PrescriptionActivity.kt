package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PrescriptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prescription)

        val prescriptionText = findViewById<TextView>(R.id.textViewPrescriptionContent)

        // Dữ liệu từ NFC
        val prescriptionData = intent.getStringExtra("PRESCRIPTION_DATA") ?: "No prescriptions"

        // Hiển thị
        prescriptionText.text = prescriptionData

        // Lưu lại
        val prefs = getSharedPreferences("patient_data", Context.MODE_PRIVATE)
        prefs.edit().putString("prescription_data", prescriptionData).apply()
    }
}
