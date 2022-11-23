package com.example.firepreventionapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi

class TemperatureActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        updateActualTemperature(MainActivity.actualTemperature)
        updateMaxTemperature(MainActivity.maxTemperature)

        val refreshButton = findViewById<TextView>(R.id.temp_refresh)
        refreshButton.setOnClickListener {
            updateActualTemperature(MainActivity.actualTemperature)
            updateMaxTemperature(MainActivity.maxTemperature)
        }
    }

    private fun updateActualTemperature(value: String) {
        val tempMsg = findViewById<TextView>(R.id.actual_temperature)
        tempMsg.text = getString(R.string.temp_formatter, value)
    }

    private fun updateMaxTemperature(value: String) {
        val tempMsg = findViewById<TextView>(R.id.max_temperature)
        tempMsg.text = getString(R.string.temp_formatter, value)
    }
}
