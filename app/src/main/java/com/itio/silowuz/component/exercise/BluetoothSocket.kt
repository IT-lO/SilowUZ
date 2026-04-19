package com.itio.silowuz.component.exercise

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import java.io.IOException
import java.util.UUID

object BluetoothSocket {
    private val MY_UUID: UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")

    fun startServerAndSend(adapter: BluetoothAdapter, data: String) {
        Thread {
            try {
                val serverSocket = adapter.listenUsingRfcommWithServiceRecord("SilowuzExport", MY_UUID)
                Log.d("BT_DEBUG", "Serwer otwarty, czekam...")

                val socket = serverSocket.accept()
                socket?.use { s ->
                    s.outputStream.write(data.toByteArray(Charsets.UTF_8))
                    s.outputStream.flush()
                    Thread.sleep(500)
                }
                serverSocket.close()
            } catch (e: SecurityException) {
                Log.e("BT_DEBUG", "BRAK UPRAWNIEŃ W RUNTIME: ${e.message}")
            } catch (e: IOException) {
                Log.e("BT_DEBUG", "Błąd IO: ${e.message}")
            }
        }.start()
    }

    fun connectAndReceive(device: BluetoothDevice, onReceived: (String) -> Unit) {
        Thread {
            try {
                val socket = device.createRfcommSocketToServiceRecord(MY_UUID)
                socket.connect()

                val reader = socket.inputStream.bufferedReader()
                val result =
                    reader.readLine()
                onReceived(result ?: "")
                socket.close()
            } catch (e: Exception) {
                android.util.Log.e("BT_ERROR", "Błąd połączenia: ${e.message}")
            }
        }.start()
    }
}