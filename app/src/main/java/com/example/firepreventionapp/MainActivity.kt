package com.example.firepreventionapp

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.IOException
import java.util.*

@RequiresApi(Build.VERSION_CODES.M)
class MainActivity : AppCompatActivity() {
    // Static variable to store temperature
    companion object {
        var actualTemperature: String = "0"
        var maxTemperature: String = "0"
        var actualHumidity: String = "0"
        var minHumidity: String = "100"
        var actualGas: String = "0"
        var alertLog : String = ""
    }

    private val REQUEST_ENABLE_BT : Int = 0
    private var bluetoothSocket: BluetoothSocket? = null
    lateinit var bluetoothConnection: BluetoothConnection

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        updateConnectionStatus(false)
        connectBluetooth()

        val buttonBluetoothClick = findViewById<Button>(R.id.button_try_connect)
        buttonBluetoothClick.setOnClickListener {
            connectBluetooth()
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

    fun updateConnectionStatus(value : Boolean) {
        val connectionMessage = findViewById<TextView>(R.id.connection)
        if (value) {
            connectionMessage.text = getString(R.string.connection_status, "Connected")
        } else {
            connectionMessage.text = getString(R.string.connection_status, "Disconnected")
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                connectBluetooth()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                connectBluetooth()
            } else {
                Toast.makeText(this, "Please enable bluetooth to use this app!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun connectBluetooth() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_ENABLE_BT)
            return
        }

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device!", Toast.LENGTH_SHORT).show()
            return
        }

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            return
        }

        val pairedDevices = bluetoothAdapter.bondedDevices
        if (pairedDevices.isEmpty()) {
            Toast.makeText(this, "Please pair the device first!", Toast.LENGTH_SHORT).show()
            return
        }

        var device: BluetoothDevice? = null
        for (pairedDevice in pairedDevices) {
            if (pairedDevice.name == "WALL-E1") {
                device = pairedDevice
                break
            }
        }

        if (device == null) {
            Toast.makeText(this, "Device not found!", Toast.LENGTH_SHORT).show()
            return
        }

        if (bluetoothSocket == null || !bluetoothSocket!!.isConnected) {
            try {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"))
                bluetoothSocket!!.connect()
                updateConnectionStatus(true)
                Toast.makeText(this, "Connected to device!", Toast.LENGTH_SHORT).show()
                bluetoothConnection = BluetoothConnection(bluetoothSocket, this)
                bluetoothConnection.start()
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Connection failed!", Toast.LENGTH_SHORT).show()
                updateConnectionStatus(false)
            }
        } else {
            Toast.makeText(this, "Already connected!", Toast.LENGTH_SHORT).show()
        }
    }

    fun isMidnight(): Boolean {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        return hour == 0 && minute == 0
    }

    fun resetMinAndMax() {
        minHumidity = "100"
        maxTemperature = "0"
    }
}