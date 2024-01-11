package com.example.readscape

import android.nfc.cardemulation.HostApduService
import android.os.Bundle

class MyHostApduService : HostApduService() {
    companion object {
        // This is an example AID, replace with your actual AID
        val SELECT_APDU = byteArrayOf(0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte(), 0x07.toByte(), 0xD2.toByte(), 0x76.toByte(), 0x00.toByte(), 0x00.toByte(), 0x85.toByte(), 0x01.toByte(), 0x01.toByte(), 0x00.toByte())
        // This is the dummy data we want to send
        val DUMMY_RESPONSE = "Dummy String".toByteArray(Charsets.UTF_8)
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu == null) return ByteArray(0)

        // Check if the APDU matches the SELECT AID command
        return if (commandApdu.contentEquals(SELECT_APDU)) {
            DUMMY_RESPONSE
        } else {
            // If not, return a default response
            ByteArray(0)
        }
    }

    override fun onDeactivated(reason: Int) {
        // Handle deactivation (e.g., log or clean up resources)
    }
}
