package com.itio.silowuz.component.exercise

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import android.bluetooth.BluetoothSocket as AndroidBluetoothSocket
object BluetoothSocket {
    private val MY_UUID: UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")

    @SuppressLint("MissingPermission")
    suspend fun startServerAndSend(adapter: BluetoothAdapter, data: String): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val serverSocket = adapter.listenUsingRfcommWithServiceRecord("SilowuzExport", MY_UUID)

            val socket: AndroidBluetoothSocket? = try {
                serverSocket.accept(30000)
            } finally {
                serverSocket.close()
            }

            socket?.use { s ->
                val outputStream = s.outputStream
                val dataToWithNewLine = if (data.endsWith("\n")) data else "$data\n"
                outputStream.write(dataToWithNewLine.toByteArray(Charsets.UTF_8))
                outputStream.flush()
                kotlinx.coroutines.delay(500)
            }

            if (socket != null) Result.success(Unit)
            else Result.failure(Exception("Timeout: Nikt się nie połączył"))

        } catch (e: Exception) {
            Log.e("BT_DEBUG", "Błąd: ${e.message}")
            Result.failure(e)
        }
    }
    suspend fun connectAndReceive(device: BluetoothDevice): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val socket = device.createRfcommSocketToServiceRecord(MY_UUID)
            socket.connect()

            val reader = socket.inputStream.bufferedReader()
            val result = reader.readLine()
            socket.close()
            if (result != null) Result.success(result)
            else Result.failure(Exception("Otrzymano pusty strumień"))
        } catch (e: Exception) {
            Log.e("BT_ERROR", "Błąd połączenia: ${e.message}")
            Result.failure(e)
        }
    }
}