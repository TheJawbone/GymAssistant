package com.example.dev.gymassistantv2.utils

class Stopwatch {

    private var startTime: Long = 0
    private var stopTime: Long = 0
    var isRunning = false

    val elapsedTime: Long
        get() = if (isRunning) {
            System.currentTimeMillis() - startTime
        } else stopTime - startTime

    val formattedElapsedTime: String
        get() {

            var minutes = java.lang.Long.toString(elapsedTime / 60000 % 60)
            if (minutes.length < 2) {
                minutes = "0$minutes"
            }

            var seconds = java.lang.Long.toString(elapsedTime / 1000 % 60)
            if (seconds.length < 2) {
                seconds = "0$seconds"
            }

            var hundredths = java.lang.Long.toString(elapsedTime / 10 % 100)
            if (hundredths.length < 2) {
                hundredths = "0$hundredths"
            }

            return "$minutes:$seconds:$hundredths"
        }


    fun start() {
        if (startTime == 0L) {
            startTime = System.currentTimeMillis()
        } else if (!isRunning) {
            startTime = System.currentTimeMillis() - (stopTime - startTime)
        }
        isRunning = true
    }


    fun stop() {
        if (isRunning) {
            stopTime = System.currentTimeMillis()
        }
        isRunning = false
    }

    fun reset() {
        startTime = 0
        stopTime = 0
        isRunning = false
    }

    fun set(elapsedTime: Long) {
        startTime = System.currentTimeMillis() - elapsedTime
        stopTime = System.currentTimeMillis()
    }
}