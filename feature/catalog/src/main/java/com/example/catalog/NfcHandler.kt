package com.example.catalog

import android.app.Activity
import android.content.Context
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.util.Log
import android.widget.Toast

class NfcHandler(private val context: Context) {
    private var nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
    var hceDataToSend = "Default HCE Data"
    var receivedString: String? = null
        private set

    fun sendNfcData(dummyData: String) {
        hceDataToSend = dummyData

    }

    fun receiveNfcData() {
        nfcAdapter?.enableReaderMode(context as Activity, { tag: Tag? ->
            tag?.let {
                IsoDep.get(it)?.use { isoDep ->
                    isoDep.connect()
                    // Send a command APDU to request data
                    val commandApdu = createCommandApdu()
                    val response = isoDep.transceive(commandApdu)
                    // Process the response, which should be the string sent by the HCE device
                    val receivedString = String(response, Charsets.UTF_8)
                    // You can use this string as needed, like showing a toast for demonstration
                    Log.d("receivedNFC", "Received: $receivedString")
                    (context as Activity).runOnUiThread {
                        Toast.makeText(context, "Received: $receivedString", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
    }

    private fun createCommandApdu(): ByteArray {
        // This is an example SELECT APDU command that matches the one in MyHostApduService
        return byteArrayOf(0x00.toByte(), 0xA4.toByte(), 0x04.toByte(), 0x00.toByte(), 0x07.toByte(), 0xD2.toByte(), 0x76.toByte(), 0x00.toByte(), 0x00.toByte(), 0x85.toByte(), 0x01.toByte(), 0x01.toByte(), 0x00.toByte())
    }

    fun disableNfcReaderMode() {
        if (context is Activity) {
            nfcAdapter?.disableReaderMode(context)
        }
    }

    fun enableNfcReaderMode() {
        if (context is Activity) {
            nfcAdapter?.enableReaderMode(context, { tag: Tag? ->
                tag?.let {
                    IsoDep.get(it)?.use { isoDep ->
                        isoDep.connect()
                        // Send a command APDU to request data
                        val commandApdu = createCommandApdu()
                        val response = isoDep.transceive(commandApdu)
                        // Process the response, which should be the string sent by the HCE device
                        val receivedString = String(response, Charsets.UTF_8)
                        // You can use this string as needed, like showing a toast for demonstration
                        (context as Activity).runOnUiThread {
                            Toast.makeText(context, "Received: $receivedString", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }, NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null)
        }
    }
}
