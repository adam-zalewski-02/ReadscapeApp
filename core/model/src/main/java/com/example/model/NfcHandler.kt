package com.example.model

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.util.Log
import android.widget.Toast

class NfcHandler(private val context: Context) {
    private var nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
    var receivedNfcData = ""
    fun setHceData(data: String) {
        HceDataHolder.hceDataToSend = data
    }

    fun startNfcReaderMode() {
        if (context is Activity) {
            nfcAdapter?.enableReaderMode(context, { tag: Tag? ->
                handleNfcTag(tag)
            }, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
        }
    }

    private fun handleNfcTag(tag: Tag?) {
        tag?.let {
            IsoDep.get(it)?.use { isoDep ->
                isoDep.connect()
                val response = isoDep.transceive(createCommandApdu())
                val receivedString = String(response, Charsets.UTF_8)
                Log.d("NfcHandler", "Received NFC Data: $receivedString")
                NfcReceivedDataManager.setData(receivedString)
                receivedNfcData = receivedString
                (context as Activity).runOnUiThread {
                    Toast.makeText(context, "Received: $receivedString", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun stopNfcReaderMode() {
        if (context is Activity) {
            nfcAdapter?.disableReaderMode(context)
        }
    }

    private fun createCommandApdu(): ByteArray {
        // This is an example SELECT APDU command that matches the one in app/src/main/java/com/example/readscape/MyHostApduService.kt and app/src/main/res/xml/apduservice.xml
        return byteArrayOf(0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte(), 0x07.toByte(), 0xD2.toByte(), 0x76.toByte(), 0x00.toByte(), 0x00.toByte(), 0x85.toByte(), 0x01.toByte(), 0x01.toByte(), 0x00.toByte())
    }
}
