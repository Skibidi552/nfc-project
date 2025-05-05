package com.example.myapplication

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    private var nfcAdapter: NfcAdapter? = null
    private var enableScan: Boolean = false
    private var hasShownToast: Boolean = false

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.textViewStatus)
        val scanBtn = findViewById<Button>(R.id.buttonScanNFC)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        scanBtn.setOnClickListener {
            if (nfcAdapter == null) {
                Toast.makeText(this, "Thiết bị không hỗ trợ NFC", Toast.LENGTH_LONG).show()
            } else {
                enableScan = true
                hasShownToast = false  // Reset trạng thái mỗi lần nhấn nút
                statusText.text = "Hãy đưa thẻ NFC lại gần thiết bị..."
                Toast.makeText(this, "Sẵn sàng quét NFC", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null) {
            val pendingIntent = PendingIntent.getActivity(
                this, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_MUTABLE
            )
            val intentFilters = arrayOf(IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED))
            nfcAdapter?.enableForegroundDispatch(this, pendingIntent, intentFilters, null)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

        if (!enableScan || hasShownToast) return
        if (NfcAdapter.ACTION_TAG_DISCOVERED != intent.action) return

        val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        if (tag == null) {
            Toast.makeText(this, "Không thể đọc thẻ NFC", Toast.LENGTH_SHORT).show()
            return
        }

        intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
            val messages = rawMessages.map { it as NdefMessage }
            for (msg in messages) {
                for (record in msg.records) {
                    val payload = record.payload.toString(Charset.forName("US-ASCII")).trim()
                    try {
                        val json = JSONObject(payload)
                        val diagnosis = json.optString("diagnosis", "")
                        val prescription = json.optString("prescription", "")
                        val medications = json.optJSONArray("medications")

                        val sharedPrefs = getSharedPreferences("PatientData", MODE_PRIVATE)
                        val editor = sharedPrefs.edit()
                        editor.putString("diagnosis", diagnosis)
                        editor.putString("prescription", prescription)

                        val medList = mutableListOf<String>()
                        for (i in 0 until (medications?.length() ?: 0)) {
                            medList.add(medications!!.getString(i))
                        }

                        editor.putBoolean("med_vitamin_c", medList.contains("Vitamin C"))
                        editor.putBoolean("med_paracetamol", medList.contains("Paracetamol"))
                        editor.putBoolean("med_amoxicillin", medList.contains("Amoxicillin"))
                        editor.apply()

                        // ✅ Chỉ hiển thị 1 lần
                        hasShownToast = true
                        enableScan = false
                        Toast.makeText(this, "✅ Đã lưu dữ liệu từ NFC", Toast.LENGTH_SHORT).show()
                        statusText.text = "✅ Đã lưu dữ liệu từ NFC"

                        // 👉 Chuyển sang màn hình chi tiết
                        val detailIntent = Intent(this, PatientDetailActivity::class.java)
                        detailIntent.putExtra("PATIENT_ID", payload)
                        startActivity(detailIntent)

                    } catch (e: Exception) {
                        Toast.makeText(this, "❌ Dữ liệu không đúng định dạng", Toast.LENGTH_SHORT).show()
                        statusText.text = "❌ Lỗi định dạng dữ liệu"
                        hasShownToast = true
                        enableScan = false
                    }
                }
            }
        }
    }
}
