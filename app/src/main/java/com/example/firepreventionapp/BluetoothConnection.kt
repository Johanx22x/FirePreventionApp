package com.example.firepreventionapp

import android.annotation.SuppressLint
import android.bluetooth.BluetoothSocket
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("MissingPermission")
class BluetoothConnection(bluetoothSocket: BluetoothSocket?, private val context : MainActivity) : Thread() {
    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null

    private var response = ""
    private var actualTemperature: String = ""
    private var actualHumidity: String = ""
    private var actualGas: String = ""
    private var actualAlert: String = ""

    init {
        this.bluetoothSocket = bluetoothSocket
        try {
            inputStream = bluetoothSocket?.inputStream
            outputStream = bluetoothSocket?.outputStream
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun run() {
        while (true) {
            try {
                // Check if is 00:00 to reset the min and max values
                if (context.isMidnight()) {
                    context.resetMinAndMax()
                }

                // Check if the connection is still active
                if (bluetoothSocket?.isConnected == false) {
                    context.updateConnectionStatus(false)
                    break
                }

                // Read the data from the bluetooth device and parse it
                response += read()
                parseResponse()

                // Sleep for 1 second
                sleep(1000)
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun parseResponse() {
        // Check if the response ends with $
        if (!response.endsWith("$")) {
            return
        }

        // Split the response into a list of strings
        val responseArray = response.split(";")

        when (responseArray.size) {
            3 -> {
                actualTemperature = responseArray[0]
                // Remove "T: " from the string
                actualTemperature = actualTemperature.substring(3)
                if (actualTemperature.toDouble() > MainActivity.maxTemperature.toDouble()) {
                    MainActivity.maxTemperature = actualTemperature
                }
                if (actualTemperature != MainActivity.actualTemperature) {
                    MainActivity.actualTemperature = actualTemperature
                }

                actualHumidity = responseArray[1]
                // Remove "H: " from the string
                actualHumidity = actualHumidity.substring(3)
                if (actualHumidity.toDouble() < MainActivity.minHumidity.toDouble()) {
                    MainActivity.minHumidity = actualHumidity
                }
                if (actualHumidity != MainActivity.actualHumidity) {
                    MainActivity.actualHumidity = actualHumidity
                }

                actualGas = responseArray[2]
                // Remove the last character, which is $
                actualGas = actualGas.substring(0, actualGas.length - 1)
                // Read after G: to get the actual gas value
                actualGas = actualGas.substring(3)

                // Check if exists a new alert, split the string to get the alert type
                val alertArray = actualGas.split("$")

                // Check if the alertArray has 2 elements
                if (alertArray.size == 2) {
                    actualGas = alertArray[0]
                    if (actualGas != MainActivity.actualGas) {
                        MainActivity.actualGas = actualGas
                    }

                    actualAlert = alertArray[1]
                    actualAlert = actualAlert.substring(3)
                    // Get the current date time
                    val currentDateTime =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            java.time.LocalDateTime.now()
                        } else {
                            java.util.Calendar.getInstance().time
                        }

                    MainActivity.alertLog += "$currentDateTime - $actualAlert\n"
                    Log.d("BluetoothConnection", "Alert: $actualAlert")
                    // Alert the user with a notification
                    context.alertUser(actualAlert)
                } else if (alertArray.size == 1) {
                    if (actualGas != MainActivity.actualGas) {
                        MainActivity.actualGas = actualGas
                    }
                }

                response = ""
            }
            1 -> {
                actualAlert = responseArray[0]
                // Remove the last character, which is $
                actualAlert = actualAlert.substring(0, actualAlert.length - 1)
                actualAlert = actualAlert.substring(3)
                // Get the current date time
                val currentDateTime =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    java.time.LocalDateTime.now()
                } else {
                    java.util.Calendar.getInstance().time
                }
                MainActivity.alertLog += "$currentDateTime - $actualAlert\n"
                Log.d("BluetoothConnection", "Alert: $actualAlert")
                context.alertUser(actualAlert)
                response = ""
            }
            else -> {
                Log.d("BluetoothConnection", "Response: $response")
                response = ""
            }
        }
    }

    fun write(message: String) {
        try {
            outputStream?.write(message.toByteArray())
            Log.d("BluetoothConnection", "Message sent")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("BluetoothConnection", "Message not sent")
        }
    }

    private fun read(): String {
        val buffer = ByteArray(1024)
        var bytes = 0
        try {
            bytes = inputStream?.read(buffer)!!
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("BluetoothConnection", "Message not read")
        }
        return String(buffer, 0, bytes)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun cancel() {
        try {
            bluetoothSocket?.close()
            context.updateConnectionStatus(false)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}