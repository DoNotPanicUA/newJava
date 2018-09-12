package com.donotpanic.airport.domain.airport;

import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.Engine.GlobalObject;
import com.donotpanic.airport.domain.Engine.ObjectType;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.common.PrintService;
import com.donotpanic.airport.domain.location.AirportLocation;
import com.donotpanic.airport.domain.location.Coordinates;

import java.awt.*;

/**
 * Created by DoNotPanic-NB on 01.11.2017.
 */
public abstract class PlaneAirportLocation implements AirportLocation, GlobalObject{

    private Plane reservedByPlane;
    private Plane currentPlane;
    private final Airport airport;
    private final String objectName;
    private final ObjectType objType;
    private GlobalEngine globalEngine;

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public ObjectType getObjectType() {
        return objType;
    }

    PlaneAirportLocation(Airport airport, String name, ObjectType objType, GlobalEngine globalEngine){
        this.airport = airport;
        this.objectName = name;
        this.objType = objType;
        this.globalEngine = globalEngine;
    }

    @Override
    public Airport getAirport() {
        return this.airport;
    }

    @Override
    public String getObjectName() {
        return this.objectName;
    }

    @Override
    public Coordinates getCoordinates() {
        return globalEngine.getObjectCoordinates(this);
    }

    boolean reserveLocation(Plane plane){
        if (this.reservedByPlane == null || this.reservedByPlane.equals(plane)){
            this.reservedByPlane = plane;
            PrintService.printMessageObj(this.getObjectName()+" is reserved for the "+ plane.getObjectName() +"!", this);
            return true;
        }else{
            return false;
        }
    }

    Plane getReserve(){
        return this.reservedByPlane;
    }

    void freeReserveLocation(){
        this.reservedByPlane = null;
    }

    boolean checkReserveBy(Plane plane){
        if (reservedByPlane == null){
            return false;
        }else{
            return (reservedByPlane.equals(plane));
        }
    }

    boolean checkIsLocationFree(){
        return (this.reservedByPlane == null);
    }



}
