package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.common.Actions;
import com.donotpanic.airport.domain.location.GeoLocationService;

public class AircraftAction_RequestLandPark extends AircraftAction implements Runnable{
    private Plane plane;

    @Override
    protected boolean checkPreCondition(Plane plane) {
        return (plane.getReservedParking() == null &&
                Actions.RADIOTOWER_REQUEST.getDurationSec()*2.5*plane.getSpeed() >= CommonServices.getCommonServices().getGeoLocationService().calculateDistance(plane.getDestinationTo(), getEngine().getObjectCoordinates(plane)));
    }

    @Override
    protected void preAction(Plane plane) {

    }

    @Override
    protected void action(Plane plane) throws Throwable {
    }

    @Override
    protected boolean checkEndOfAction(Plane plane) {
        return true;
    }

    @Override
    protected void postAction(Plane plane) {
        this.plane = plane;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (this.plane.getReservedParking() == null) {
            this.plane.requestParking();
            Actions.STANDBY_5SEC.doAction();
        }
    }

}
