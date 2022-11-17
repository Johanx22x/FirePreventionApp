package com.example.firepreventionapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TemperatureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        updateActualTemperature(0)
        updateMaxTemperature(0)
    }

    private fun updateActualTemperature(value : Int) {
        val tempMsg = findViewById<TextView>(R.id.actual_temperature)
        tempMsg.text = getString(R.string.temp_formatter, value)
    }

    private fun updateMaxTemperature(value : Int) {
        val tempMsg = findViewById<TextView>(R.id.max_temperature)
        tempMsg.text = getString(R.string.temp_formatter, value)
    }
}
