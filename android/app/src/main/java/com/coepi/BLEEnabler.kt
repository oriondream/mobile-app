package com.coepi

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.content.Intent
import androidx.core.app.ComponentActivity

class BLEEnabler(
    private val activity: ComponentActivity,
    private val listener: ((Boolean) -> Unit)
) {
    private val enableBluetoothRequestCode = 1

    fun enable() {
        activity.bluetoothManager?.adapter?.takeIf { !it.isEnabled }?.apply {
            log.d("Bluetooth not enabled. Requesting...")
            val enableBluetoothIntent = Intent(ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBluetoothIntent, enableBluetoothRequestCode)
        } ?: {
            listener(true)
        }()
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == enableBluetoothRequestCode) {
            when (resultCode) {
                RESULT_OK -> listener.invoke(true)
                RESULT_CANCELED -> listener.invoke(false)
                else -> throw Exception("Unexpected result code: $resultCode")
            }
        }
    }
}
