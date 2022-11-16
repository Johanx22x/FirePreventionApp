package com.example.firepreventionapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateConnectionStatus(false)

        val buttonBluetoothClick = findViewById<Button>(R.id.button_try_connect)
        buttonBluetoothClick.setOnClickListener {
            tryConnectBluetooth()
        }

        val buttonTemperatureClick = findViewById<Button>(R.id.button_show_temperature)
        buttonTemperatureClick.setOnClickListener {
            val intent = Intent(this, TemperatureActivity::class.java)
            startActivity(intent)
        }

        val buttonHumidityClick = findViewById<Button>(R.id.button_show_humidity)
        buttonHumidityClick.setOnClickListener {
            val intent = Intent(this, HumidityActivity::class.java)
            startActivity(intent)
        }

        val buttonGasClick = findViewById<Button>(R.id.button_show_gas)
        buttonGasClick.setOnClickListener {
            val intent = Intent(this, GasActivity::class.java)
            startActivity(intent)
        }

        val buttonAlertClick = findViewById<Button>(R.id.button_show_status)
        buttonAlertClick.setOnClickListener {
            val intent = Intent(this, AlertActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateConnectionStatus(value : Boolean) {
        val connectionMessage = findViewById<TextView>(R.id.connection)
        if (value) {
            connectionMessage.text = getString(R.string.connection_status, "Connected")
        } else {
            connectionMessage.text = getString(R.string.connection_status, "Disconnected")
        }
    }

    private fun tryConnectBluetooth() {
        updateConnectionStatus(true)
    }
}
