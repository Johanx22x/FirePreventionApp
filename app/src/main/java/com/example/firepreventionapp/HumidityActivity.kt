package com.example.firepreventionapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi

class HumidityActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_humidity)

        updateActualTemperature(MainActivity.actualHumidity)
        updateMaxTemperature(MainActivity.minHumidity)

        val refreshButton = findViewById<TextView>(R.id.temp_refresh)
        refreshButton.setOnClickListener {
            updateActualTemperature(MainActivity.actualHumidity)
            updateMaxTemperature(MainActivity.minHumidity)
        }
    }

    private fun updateActualTemperature(value: String) {
        val tempMsg = findViewById<TextView>(R.id.actual_humidity)
        tempMsg.text = getString(R.string.humidity_formatter, value, "%")
    }

    private fun updateMaxTemperature(value: String) {
        val tempMsg = findViewById<TextView>(R.id.min_humidity)
        tempMsg.text = getString(R.string.humidity_formatter, value, "%")
    }
}