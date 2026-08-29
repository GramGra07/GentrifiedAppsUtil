package org.gentrifiedApps.gentrifiedAppsUtil.classes;

import org.gentrifiedApps.gentrifiedAppsUtil.classes.equations.SlopeIntercept;
import org.gentrifiedApps.gentrifiedAppsUtil.classes.except.ExceptionThrower;
import org.gentrifiedApps.gentrifiedAppsUtil.classes.generics.pointClasses.Point;

import java.util.List;

class LinearInterpolation {
    // trying to find a way to have a linear element between two points
    private List<Double> xList;
    private List<Double> yList;
    private double lines;
    private List<SlopeIntercept> linearInterpolatorLines;

    public LinearInterpolation(List<Point> pointsList) throws Exception {
        for (Point point : pointsList) {
            xList.add(point.getX());
            yList.add(point.getY());
        }
        lines = xList.size() - 1;

        for (int i = 0; i < lines; i++) {
            SlopeIntercept slope = new SlopeIntercept((yList.get(i + 1) - yList.get(i)) / (xList.get(i + 1) - xList.get(i)));
            //y-mx = b
            double y = yList.get(i);
            double x = xList.get(i);
            double m = slope.getM().doubleValue();
            double b = y - (m * x);
            slope.setB(b);
            linearInterpolatorLines.add(slope);
        }

        if (linearInterpolatorLines.size() != lines) {
            ExceptionThrower.throwException("LinearInterpolation", new Exception("The number of points is not equal to the number of lines"));
        }
    }

    /*
     * Returns the y value for a given x value using the slope-intercept form of a line. Will return **-1** given an invalid value of x instead of erroring
     * @param x The x value to calculate the y value for.
     * @return The y value for the given x value.
     */
    public double getValue(double x) {
        for (int i = 0; i < lines; i++) {
            if (x >= xList.get(i) && x <= xList.get(i + 1)) {
                return linearInterpolatorLines.get(i).getY(x);
            }
        }
        return -1;
    }
}