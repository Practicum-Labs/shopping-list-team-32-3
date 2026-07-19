package ru.practicum.shoppinglist.root.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.practicum.shoppinglist.root.domain.api.ShakeEvent
import ru.practicum.shoppinglist.root.domain.api.ShakeRepository
import kotlin.math.sqrt

class SensorShakeRepositoryImp(
    private val context: Context,
) : ShakeRepository {
    private var lastShakeTime: Long = 0
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f

    private val sensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    override fun shakeEvents(): Flow<ShakeEvent> = callbackFlow {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        acceleration = 10f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let {
                    if (event.sensor?.type != Sensor.TYPE_ACCELEROMETER) return
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    lastAcceleration = currentAcceleration

                    currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                    val delta: Float = currentAcceleration - lastAcceleration
                    acceleration = acceleration * 0.9f + delta

                    if (acceleration > SHAKE_THRESHOLD) {
                        val now = System.currentTimeMillis()
                        if (now - lastShakeTime > MIN_TIME_BETWEEN_SHAKES_MS) {
                            lastShakeTime = now

                            trySend(
                                ShakeEvent
                            )
                        }
                    }
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // No-op for this use case
            }
        }

        sensorManager.registerListener(
            listener,
            sensor,
            SensorManager.SENSOR_DELAY_UI
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    companion object {
        private const val SHAKE_THRESHOLD = 12.0f
        private const val MIN_TIME_BETWEEN_SHAKES_MS = 1000L
    }
}
