class PIDFController(var kP: Double, var kI: Double, var kD: Double, var kF: Double) {
    private var setPoint: Double = 0.0
    private var measuredValue: Double = 0.0
    private var minIntegral: Double = -1.0
    private var maxIntegral: Double = 1.0

    private var errorValP: Double = 0.0
    private var errorValV: Double = 0.0
    private var totalError: Double = 0.0
    private var prevErrorVal: Double = 0.0

    private var errorToleranceP: Double = 0.05
    private var errorToleranceV: Double = Double.POSITIVE_INFINITY

    private var lastTimeStamp: Double = 0.0
    private var period: Double = 0.0

    constructor(kP: Double, kI: Double, kD: Double, kF: Double, sp: Double, pv: Double) : this(kP, kI, kD, kF) {
        setPoint = sp
        measuredValue = pv
        errorValP = setPoint - measuredValue
        reset()
    }

    fun reset() {
        totalError = 0.0
        prevErrorVal = 0.0
        lastTimeStamp = 0.0
    }

    fun setTolerance(positionTolerance: Double, velocityTolerance: Double = Double.POSITIVE_INFINITY) {
        errorToleranceP = positionTolerance
        errorToleranceV = velocityTolerance
    }

    fun getSetPoint(): Double = setPoint

    fun setSetPoint(sp: Double) {
        setPoint = sp
        errorValP = setPoint - measuredValue
        errorValV = (errorValP - prevErrorVal) / period
    }

    fun atSetPoint(): Boolean {
        return kotlin.math.abs(errorValP) < errorToleranceP && kotlin.math.abs(errorValV) < errorToleranceV
    }

    fun getCoefficients(): DoubleArray = doubleArrayOf(kP, kI, kD, kF)

    fun getPositionError(): Double = errorValP

    fun getTolerance(): DoubleArray = doubleArrayOf(errorToleranceP, errorToleranceV)

    fun getVelocityError(): Double = errorValV

    fun calculate(): Double = calculate(measuredValue)

    fun calculate(pv: Double, sp: Double): Double {
        setSetPoint(sp)
        return calculate(pv)
    }

    fun calculate(pv: Double): Double {
        prevErrorVal = errorValP

        val currentTimeStamp = System.nanoTime() / 1E9
        if (lastTimeStamp == 0.0) lastTimeStamp = currentTimeStamp
        period = currentTimeStamp - lastTimeStamp
        lastTimeStamp = currentTimeStamp

        errorValP = setPoint - pv
        measuredValue = pv

        errorValV = if (kotlin.math.abs(period) > 1E-6) {
            (errorValP - prevErrorVal) / period
        } else {
            0.0
        }

        totalError += period * (setPoint - measuredValue)
        totalError = totalError.coerceIn(minIntegral, maxIntegral)

        return kP * errorValP + kI * totalError + kD * errorValV + kF * setPoint
    }

    fun setPIDF(kP: Double, kI: Double, kD: Double, kF: Double) {
        this.kP = kP
        this.kI = kI
        this.kD = kD
        this.kF = kF
    }

    fun setIntegrationBounds(integralMin: Double, integralMax: Double) {
        minIntegral = integralMin
        maxIntegral = integralMax
    }

    fun clearTotalError() {
        totalError = 0.0
    }

    fun setP(kP: Double) { this.kP = kP }
    fun setI(kI: Double) { this.kI = kI }
    fun setD(kD: Double) { this.kD = kD }
    fun setF(kF: Double) { this.kF = kF }

    fun getP(): Double = kP
    fun getI(): Double = kI
    fun getD(): Double = kD
    fun getF(): Double = kF
    fun getPeriod(): Double = period
}