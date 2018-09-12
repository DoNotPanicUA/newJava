package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.aircraft.AircraftStates;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.common.Actions;
import com.donotpanic.airport.domain.location.GeoLocationService;

public class AircraftAction_Fly2Airport extends AircraftAction {

    @Override
    protected boolean checkPreCondition(Plane plane) {
        return plane.getState().equals(AircraftStates.FLY);
    }

    @Override
    protected void preAction(Plane plane) {
        plane.setDestinationTo(plane.getCurrentRoute().getDestinationTo().getAirport().getCoordinates());
        plane.setState(AircraftStates.FLY_2_AIRPORT);
    }

    @Override
    protected void action(Plane plane) throws Throwable {
        getEngine().moveObjectToCoordinates(plane, plane.getMoveAction(), plane.getDestinationTo());
    }

    @Override
    protected boolean checkEndOfAction(Plane plane) {
        return Actions.RADIOTOWER_REQUEST.getDurationSec()*0.5*plane.getSpeed() >= CommonServices.getCommonServices().getGeoLocationService().calculateDistance(plane.getDestinationTo(), getEngine().getObjectCoordinates(plane));
    }

    @Override
    protected void postAction(Plane plane) {
        plane.setState(AircraftStates.FLY);
    }
}
