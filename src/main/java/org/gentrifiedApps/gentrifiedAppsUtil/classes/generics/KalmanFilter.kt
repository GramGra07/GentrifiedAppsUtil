package org.gentrifiedApps.gentrifiedAppsUtil.classes.generics

class KalmanFilter {
    class KalmanCoefficients(val Q: Double, val R: Double)

    var x = 0.0 // your initial state
    var p = 1.0 // your initial covariance guess
    var K = 1.0 // your initial Kalman gain guess

    val kalman: KalmanCoefficients

    constructor(Q: Double, R: Double) {
        this.kalman = KalmanCoefficients(Q, R)
    }

    constructor(kalman: KalmanCoefficients) {
        this.kalman = kalman
    }

    var x_previous = x
    var p_previous = p
    var u = 0.0
    var z = 0.0

    /**
     * Run in your loop.
     *
     * @param model the CHANGE(!) in state from the model
     * @param sensor the state from the sensor
     * @return the kalman filtered state
     */
    fun update(model: Double, sensor: Double): Double {
        u = model // Ex: change in position from odometry.
        x = x_previous + u

        p = p_previous + this.kalman.Q

        K = p / (p + this.kalman.R)

        z = sensor // Pose Estimate from April Tag / Distance Sensor

        x = x + K * (z - x)

        p = (1 - K) * p

        x_previous = x
        p_previous = p
        return x
    }
}