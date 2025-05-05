package com.example.myapplication

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class VisitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit)

        val visitContainer = findViewById<LinearLayout>(R.id.visitListContainer)
        val patientId = intent.getStringExtra("PATIENT_ID")

        val visits = getVisitDataById(patientId)

        visits.forEach { (date, time) ->
            val item = layoutInflater.inflate(R.layout.visit_item, visitContainer, false)
            val dateText = item.findViewById<TextView>(R.id.textViewVisitDate)
            val timeText = item.findViewById<TextView>(R.id.textViewVisitTime)
            dateText.text = date
            timeText.text = time
            visitContainer.addView(item)
        }

        if (visits.isEmpty()) {
            val noDataText = TextView(this).apply {
                text = "Không có dữ liệu lịch sử visits cho bệnh nhân này."
                textSize = 16f
                setPadding(20, 40, 20, 0)
            }
            visitContainer.addView(noDataText)
        }
    }

    private fun getVisitDataById(patientId: String?): List<Pair<String, String>> {
        return when (patientId) {
            "12345" -> listOf(
                "Apr 25, 2024" to "03:06 PM",
                "Apr 20, 2024" to "09:15 AM",
                "Apr 10, 2024" to "11:30 AM"
            )
            "67890" -> listOf(
                "Mar 12, 2024" to "01:45 PM",
                "Mar 01, 2024" to "08:00 AM"
            )
            else -> emptyList()
        }
    }
}