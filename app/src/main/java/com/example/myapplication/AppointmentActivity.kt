package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AppointmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment)

        val timeText = findViewById<TextView>(R.id.textViewAppointmentTime)
        val locationText = findViewById<TextView>(R.id.textViewAppointmentLocation)

        // Đọc dữ liệu từ NFC
        val time = intent.getStringExtra("APPOINTMENT_TIME") ?: "Not set"
        val location = intent.getStringExtra("APPOINTMENT_LOCATION") ?: "Not set"

        // Hiển thị
        timeText.text = "Time: $time"
        locationText.text = "Location: $location"

        // Lưu lại
        val prefs = getSharedPreferences("patient_data", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putString("appointment_time", time)
            putString("appointment_location", location)
            apply()
        }
    }
}
