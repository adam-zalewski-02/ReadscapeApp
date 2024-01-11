package com.example.catalog

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.content.Context
import java.nio.charset.Charset


class NfcHandler(private val context: Context) {
    private var nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)

    fun sendNfcData(dummyData: String) {
        // Start HCE service here. You may need to configure this based on your requirements.
        // This is a placeholder for starting the service.
        // Actual implementation might require interacting with your service.
    }

    fun receiveNfcData() {
        nfcAdapter?.enableReaderMode(context as Activity, { tag ->
            // Handle the NFC tag detected in HCE mode
            // This is where you read data from the emulated card.
            // Implement your logic to read from the tag.
        }, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
    }

    fun disableNfcReaderMode() {
        if (context is Activity) {
            nfcAdapter?.disableReaderMode(context)
        }
    }
}
