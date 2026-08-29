package org.gentrifiedApps.gentrifiedAppsUtil.classes;

public class KalmanFilter1D {
    private double q, r, x, p, k;

    public KalmanFilter1D(double q, double r) {
        this.q = q;
        this.r = r;
        this.p = 1.0;
        this.x = 0;
    }

    public double update(double measurement) {
        this.p = this.p + this.q;
        this.k = this.p / (this.p + this.r);
        this.x = this.x + this.k * (measurement - this.x);
        this.p = (1 - this.k) * this.p;
        return this.x;
    }
}
