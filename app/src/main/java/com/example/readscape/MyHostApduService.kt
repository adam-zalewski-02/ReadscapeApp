package com.example.readscape

import android.nfc.cardemulation.HostApduService
import android.os.Bundle

class MyHostApduService : HostApduService() {
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        val responseString = "Dummy String"
        return responseString.toByteArray(Charsets.UTF_8)
    }

    override fun onDeactivated(reason: Int) {
        // Handle deactivation (e.g., log or clean up resources)
    }
}
