package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.aircraft.AircraftStates;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.airport.Airstrip;

public class AircraftAction_Move2Airstrip extends AircraftAction {

    @Override
    protected boolean checkPreCondition(Plane plane) {
        return (plane.getReservedAirstrip() != null &&
                plane.getState() == AircraftStates.PARK &&
                plane.getDestinationTo() == null);
    }

    @Override
    protected void preAction(Plane plane) {
        plane.setState(AircraftStates.MOVE_PARK_2_AIRSTRIP);
        plane.setDestinationTo(plane.getReservedAirstrip().getCoordinates());

    }

    @Override
    protected void action(Plane plane) throws Throwable{
        getEngine().moveObjectToCoordinates(plane, plane.getMoveAction(), plane.getDestinationTo());
    }

    @Override
    protected boolean checkEndOfAction(Plane plane) {
        return (getEngine().getBuilding(plane.getCoordinates()) instanceof Airstrip);
    }

    @Override
    protected void postAction(Plane plane) {
        plane.setDestinationTo(null);
        plane.updateDestinationFrom();
        plane.setState(AircraftStates.READY_TO_TAKEOFF);
        plane.freeParking();
    }


}
