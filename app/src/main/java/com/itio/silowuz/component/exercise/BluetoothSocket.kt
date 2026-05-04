package com.itio.silowuz.component.exercise

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import android.bluetooth.BluetoothSocket as AndroidBluetoothSocket
/**
 * Utility object for handling Bluetooth RFCOMM communication.
 * Provides methods to act as a server to broadcast data, and as a client
 * to connect and receive data from another device.
 */
object BluetoothSocket {
    /**
     * Unique identifier for the application's Bluetooth service record.
     * Both the server and client must use this exact UUID to establish a connection.
     */
    private val MY_UUID: UUID = UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66")

    /**
     * Starts a Bluetooth server socket, waits for a client to connect, and sends the provided data.
     * The server will listen for a maximum of 30 seconds before timing out.
     * @param adapter The local BluetoothAdapter used to start the server socket
     * @param data The string data payload to send to the connected client
     * @return A Result indicating success (Unit) if data was sent, or failure if an error or timeout occurred
     */
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
            else Result.failure(Exception("Timeout: Nobody Connected"))

        } catch (e: Exception) {
            Log.e("BT_DEBUG", "Error: ${e.message}")
            Result.failure(e)
        }
    }
    /**
     * Connects to a specific remote Bluetooth device as a client and reads incoming data.
     * Suspends until a single line of text is received from the server.
     * @param device The target BluetoothDevice to connect to
     * @return A Result containing the received string data, or a failure exception if the connection/read fails
     */
    suspend fun connectAndReceive(device: BluetoothDevice): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val socket = device.createRfcommSocketToServiceRecord(MY_UUID)
            socket.connect()

            val reader = socket.inputStream.bufferedReader()
            val result = reader.readLine()
            socket.close()
            if (result != null) Result.success(result)
            else Result.failure(Exception("Received empty string"))
        } catch (e: Exception) {
            Log.e("BT_ERROR", "Connection Error: ${e.message}")
            Result.failure(e)
        }
    }
}