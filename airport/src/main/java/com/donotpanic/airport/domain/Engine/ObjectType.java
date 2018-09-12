package com.donotpanic.airport.domain.Engine;

/**
 * Created by DoNotPanic-NB on 29.11.2017.
 */
public enum ObjectType {
    //BUILDING(true, true),
    AIRCRAFT(false, true),

    AIRSTRIP(true, false),
    PLANE_PARKING(true, false),

    AIRPORT(true, true),
    RADIO_TOWER(true, true);

    //PLANE_LOCATION(true, false);

    private boolean isBuilding;
    private boolean isCrashIfTwo;

    ObjectType(boolean isBuilding, boolean isCrashIfTwo){
        this.isBuilding = isBuilding;
        this.isCrashIfTwo = isCrashIfTwo;
    }

    public boolean getIsBuilding(){
        return this.isBuilding;
    }

    public boolean isCrashIfTwo() {
        return isCrashIfTwo;
    }
}
