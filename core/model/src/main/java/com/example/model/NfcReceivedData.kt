package com.example.model

object NfcReceivedDataManager {
    private var receivedData: ReceivedData? = null

    fun setData(data: String) {
        receivedData = ReceivedData(data)
    }

    fun getData(): ReceivedData? {
        return receivedData
    }

    fun clearData() {
        receivedData = null
    }
}

class ReceivedData(val data: String)