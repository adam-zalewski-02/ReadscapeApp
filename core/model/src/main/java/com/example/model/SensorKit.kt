package com.example.model
object SensorKitManager {
    private var sensorkit: SensorKit? = null

    fun setSensorKit(id: String) {
        sensorkit = SensorKit(id)
    }

    fun getSensorKit(): SensorKit? {
        return sensorkit
    }

    fun clearSensorKit() {
        sensorkit = null
    }
}
class SensorKit(val sensorKitId: String)