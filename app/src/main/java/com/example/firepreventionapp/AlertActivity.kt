package com.example.firepreventionapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi

class AlertActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)

        if (MainActivity.alertLog == "") {
            updateLogText("No alerts")
        } else {
            updateLogText(MainActivity.alertLog)
        }

        val buttonRefreshClick = findViewById<Button>(R.id.alert_refresh)
        buttonRefreshClick.setOnClickListener {
            updateLogText(MainActivity.alertLog)
        }
    }

    private fun updateLogText(text : String) {
        val connectionMessage = findViewById<TextView>(R.id.alert_log_text)
        connectionMessage.text = getString(R.string.alert_log, text)
    }
}