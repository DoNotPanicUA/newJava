package com.donotpanic.airport.domain.location;

/**
 * Created by DoNotPanic-UA on 04.08.2017.
 */
public abstract class Coordinates {
    private int dbCoordainateId = -1;
    private final double X;
    private final double Y;

    Coordinates(double x, double y){
        this.X = roundDouble(x, 10);
        this.Y = roundDouble(y, 10);
    }

//    Coordinates(Coordinates coordinates){
//        this.X = coordinates.getX();
//        this.Y = coordinates.getY();
//    }

    private double roundDouble(Double inDouble, int precision){
        return Math.round(Math.pow(10, precision) * inDouble) / Math.pow(10, precision);
    }

    public int getDbCoordainateId() {
        return dbCoordainateId;
    }

    public Coordinates setDbCoordainateId(int dbCoordainateId) {
        this.dbCoordainateId = dbCoordainateId;
        return this;
    }

    public double getX(){
        return this.X;
    }

    public double getY(){
        return this.Y;
    }

    public double getShortX(){
        return roundDouble(this.X, 2);
    }

    public double getShortY(){
        return roundDouble(this.Y, 2);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinates){
            return (this.getX() == ((Coordinates) obj).getX() && this.getY() == ((Coordinates) obj).getY());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return new Double(this.getX() * 271 + this.getY()).intValue();
    }
}
