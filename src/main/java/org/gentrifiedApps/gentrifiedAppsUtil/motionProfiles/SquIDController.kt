class SquIDController {
    private var p: Double = 0.0
    private var i: Double = 0.0
    private var d: Double = 0.0

    fun setPID(p: Double,d:Double) {
        this.p = p
        this.d= d
    }

    fun calculate(setpoint: Double, current: Double): Double {
        return (kotlin.math.sqrt(kotlin.math.abs((setpoint - current) * p)) * kotlin.math.sign(setpoint - current)) +
                (d * setpoint - current)
    }
}