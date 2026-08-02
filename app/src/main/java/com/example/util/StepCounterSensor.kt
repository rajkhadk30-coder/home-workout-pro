package com.example.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StepCounterSensor(context: Context) : SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val stepSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

    private val _steps = MutableStateFlow(4250) // Initial default baseline steps
    val steps: StateFlow<Int> = _steps

    private var initialStepCount = -1

    fun startListening() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    fun simulateWalkSteps(additionalSteps: Int = 500) {
        _steps.value += additionalSteps
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null && event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val totalStepsSinceBoot = event.values[0].toInt()
            if (initialStepCount == -1) {
                initialStepCount = totalStepsSinceBoot
            }
            val currentSessionSteps = totalStepsSinceBoot - initialStepCount
            _steps.value = 4250 + currentSessionSteps
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}
