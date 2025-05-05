package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MedicationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medication)

        val vitamin = findViewById<TextView>(R.id.textVitamin)
        val paracetamol = findViewById<TextView>(R.id.textParacetamol)
        val amoxicillin = findViewById<TextView>(R.id.textAmoxicillin)

        val prefs = getSharedPreferences("patient_data", MODE_PRIVATE)

        vitamin.text = if (prefs.getBoolean("med_vitamin_c", false))
            "Vitamin C: ✔" else "Vitamin C: ✘"

        paracetamol.text = if (prefs.getBoolean("med_paracetamol", false))
            "Paracetamol: ✔" else "Paracetamol: ✘"

        amoxicillin.text = if (prefs.getBoolean("med_amoxicillin", false))
            "Amoxicillin: ✔" else "Amoxicillin: ✘"
    }
}
