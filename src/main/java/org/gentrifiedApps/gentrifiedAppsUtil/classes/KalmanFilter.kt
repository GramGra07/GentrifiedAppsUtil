package org.firstinspires.ftc.teamcode.helpers.control;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Twist2d;
import com.acmerobotics.roadrunner.Vector2d;

public class KalmanFilter {
    public static final class KalmanCoefficients {
        public final double Q;
        public final double R;
        /**
         * @param Q model covariance (trust in model), default 0.1
         * @param R sensor covariance (trust in sensor), default 0.4
         */
        public KalmanCoefficients(double Q, double R) {
            this.Q = Q;
            this.R = R;
        }
    }
    double x = 0; // your initial state
    double p = 1; // your initial covariance guess
    double K = 1; // your initial Kalman gain guess

    public final KalmanCoefficients kalman;
    /**
     * @param Q model covariance (trust in model), default 0.1
     * @param R sensor covariance (trust in sensor), default 0.4
     */
    public KalmanFilter(double Q, double R) {
        this.kalman = new KalmanCoefficients(Q, R);
    }
    public KalmanFilter(KalmanCoefficients kalman) {
        this.kalman = kalman;
    }

    double x_previous = x;
    double p_previous = p;
    double u = 0;
    double z = 0;

    /** Run in your loop.
     *
     * @param model the CHANGE(!) in state from the model
     * @param sensor the state from the sensor
     * @return the kalman filtered state
     */
    public double update(double model, double sensor) {

        u = model; // Ex: change in position from odometry.
        x = x_previous + u;

        p = p_previous + this.kalman.Q;

        K = p/(p + this.kalman.R);

        z = sensor; // Pose Estimate from April Tag / Distance Sensor

        x = x + K * (z - x);

        p = (1 - K) * p;

        x_previous = x;
        p_previous = p;
        return x;
    }
}