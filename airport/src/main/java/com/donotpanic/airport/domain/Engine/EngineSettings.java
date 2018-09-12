package com.donotpanic.airport.domain.Engine;

/**
 * Created by DoNotPanic-NB on 30.11.2017.
 */
public class EngineSettings {
    private double globalMapWidth;
    private double globalMapHeight;
    private double restrictedLine;
    private double airportMinimalDistance;
    private double buildingMinimalDistance;
    private double gameSpeed = 1;
    private int    circleSides;

    public double getGameSpeed() {
        return gameSpeed;
    }

    public int getCircleSides() {
        return circleSides;
    }

    void setCircleSides(int circleSides) {
        this.circleSides = circleSides;
    }

    void setGameSpeed(double gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public double getGlobalMapWidth() {
        return globalMapWidth;
    }

    void setGlobalMapWidth(double globalMapWidth) {
        this.globalMapWidth = globalMapWidth;
    }

    public double getGlobalMapHeight() {
        return globalMapHeight;
    }

    void setGlobalMapHeight(double globalMapHeight) {
        this.globalMapHeight = globalMapHeight;
    }

    public double getRestrictedLine() {
        return restrictedLine;
    }

    void setRestrictedLine(double restrictedLine) {
        this.restrictedLine = restrictedLine;
    }

    public double getAirportMinimalDistance() {
        return airportMinimalDistance;
    }

    void setAirportMinimalDistance(double airportMinimalDistance) {
        this.airportMinimalDistance = airportMinimalDistance;
    }

    public double getBuildingMinimalDistance() {
        return buildingMinimalDistance;
    }

    void setBuildingMinimalDistance(double buildingMinimalDistance) {
        this.buildingMinimalDistance = buildingMinimalDistance;
    }
}
