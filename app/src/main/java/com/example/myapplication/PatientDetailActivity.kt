package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PatientDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_detail)

        val patientData = intent.getStringExtra("PATIENT_ID")

        val nameText = findViewById<TextView>(R.id.textViewName)
        val phoneText = findViewById<TextView>(R.id.textViewPhone)
        val emailText = findViewById<TextView>(R.id.textViewEmail)
        val genderAgeText = findViewById<TextView>(R.id.textViewGenderAge)
        val dobText = findViewById<TextView>(R.id.textViewDob)
        val diagnosisTextView = findViewById<TextView>(R.id.textViewDiagnosis)

        if (!patientData.isNullOrEmpty()) {
            val parts = patientData.split("|")
            if (parts.size >= 6) {
                val name = parts[0]
                val phone = parts[1]
                val email = parts[2]
                val gender = parts[3]
                val age = parts[4]
                val dob = parts[5]
                val diagnosis = if (parts.size > 6) parts[6] else "Không có dữ liệu chẩn đoán."

                nameText.text = name
                phoneText.text = "Phone: $phone"
                emailText.text = "Email: $email"
                genderAgeText.text = "Age: $age   Gender: $gender"
                dobText.text = "Date of birth: $dob"

                val prefs = getSharedPreferences("patient_data", Context.MODE_PRIVATE)
                with(prefs.edit()) {
                    putString("name", name)
                    putString("phone", phone)
                    putString("email", email)
                    putString("gender", gender)
                    putString("age", age)
                    putString("dob", dob)
                    putString("diagnosis", diagnosis)
                    apply()
                }

                findViewById<Button>(R.id.buttonDiagnosis).setOnClickListener {
                    val intent = Intent(this, DiagnosisActivity::class.java)
                    intent.putExtra("PATIENT_ID", patientData)
                    startActivity(intent)
                }

                findViewById<Button>(R.id.buttonPrescriptions).setOnClickListener {
                    val intent = Intent(this, PrescriptionActivity::class.java)
                    intent.putExtra("PATIENT_ID", patientData)
                    startActivity(intent)
                }

                findViewById<Button>(R.id.buttonMedication).setOnClickListener {
                    val intent = Intent(this, MedicationActivity::class.java)
                    val medicationFromNFC = "Vitamin C, Paracetamol"  // dữ liệu ví dụ từ NFC
                    intent.putExtra("MEDICATION_DATA", medicationFromNFC)
                    startActivity(intent)
                }

                findViewById<Button>(R.id.buttonAppointments).setOnClickListener {
                    val intent = Intent(this, AppointmentActivity::class.java)
                    intent.putExtra("PATIENT_ID", patientData)
                    startActivity(intent)
                }

                findViewById<Button>(R.id.buttonVisits).setOnClickListener {
                    val intent = Intent(this, VisitActivity::class.java)
                    intent.putExtra("PATIENT_ID", patientData)
                    startActivity(intent)
                }

                diagnosisTextView.text = diagnosis
            } else {
                Toast.makeText(this, "Dữ liệu NFC không hợp lệ", Toast.LENGTH_SHORT).show()
            }
        } else {
            nameText.text = "Không rõ"
            phoneText.text = "Phone: --"
            emailText.text = "Email: --"
            genderAgeText.text = "Age: --   Gender: --"
            dobText.text = "Date of birth: --"
            diagnosisTextView.text = "Không có dữ liệu chẩn đoán."
        }
    }
}
