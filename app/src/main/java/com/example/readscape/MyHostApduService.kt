package com.example.readscape

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.util.Log
import com.example.model.HceDataHolder

class MyHostApduService : HostApduService() {
    companion object {
        val SELECT_APDU = byteArrayOf(0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte(), 0x07.toByte(), 0xD2.toByte(), 0x76.toByte(), 0x00.toByte(), 0x00.toByte(), 0x85.toByte(), 0x01.toByte(), 0x01.toByte(), 0x00.toByte())
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) return ByteArray(0)
        Log.d("MyHostApduService", "Received APDU: ${commandApdu.joinToString(", ")}")

        return if (commandApdu.contentEquals(SELECT_APDU)) {
            HceDataHolder.hceDataToSend.toByteArray(Charsets.UTF_8)
        } else {
            ByteArray(0)
        }
    }

    override fun onDeactivated(reason: Int) {
        // Handle deactivation (e.g., log or clean up resources)
    }
}
