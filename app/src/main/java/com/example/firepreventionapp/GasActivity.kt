package com.example.firepreventionapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi

class GasActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gas)

        updateActualGas(MainActivity.actualGas)

        val refreshButton = findViewById<TextView>(R.id.gas_refresh)
        refreshButton.setOnClickListener {
            updateActualGas(MainActivity.actualGas)
        }
    }

    private fun updateActualGas(value: String) {
        val gasMsg = findViewById<TextView>(R.id.gas_value)
        gasMsg.text = getString(R.string.gas_formatter, value)
    }
}