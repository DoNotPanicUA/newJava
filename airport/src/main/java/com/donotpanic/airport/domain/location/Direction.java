package com.donotpanic.airport.domain.location;

/**
 * Created by DoNotPanic-NB on 31.10.2017.
 */
public class Direction {
    private double coefX;
    private double coefY;

    public double getCoefX() {
        return coefX;
    }

    public Direction setCoefX(double coefX) {
        this.coefX = coefX;
        return this;
    }

    public double getCoefY() {
        return coefY;
    }

    public Direction setCoefY(double coefY) {
        this.coefY = coefY;
        return this;
    }
}
