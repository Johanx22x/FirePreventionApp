package com.example.firepreventionapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AlertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        updateLogText("Click the Refresh button!")

        val buttonRefreshClick = findViewById<Button>(R.id.alert_refresh)
        buttonRefreshClick.setOnClickListener {
            updateLogText("Refresh button was pressed!")
        }
    }

    private fun updateLogText(text : String) {
        val connectionMessage = findViewById<TextView>(R.id.alert_log_text)
        connectionMessage.text = getString(R.string.alert_log, text)
    }
}