package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.aircraft.AircraftStates;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.airport.PlaneParkingPlace;

public class AircraftAction_Move2Park extends AircraftAction {
    @Override
    protected boolean checkPreCondition(Plane plane) {
        return (plane.getReservedParking() != null &&
                plane.getState().equals(AircraftStates.LANDED_AIRSTRIP));
    }

    @Override
    protected void preAction(Plane plane) {
        plane.setDestinationTo(plane.getReservedParking().getCoordinates());
    }

    @Override
    protected void action(Plane plane) throws Throwable {
        getEngine().moveObjectToCoordinates(plane, plane.getMoveAction(), plane.getDestinationTo());
    }

    @Override
    protected boolean checkEndOfAction(Plane plane) {
        return (getEngine().getBuilding(plane.getCoordinates()) instanceof PlaneParkingPlace);
    }

    @Override
    protected void postAction(Plane plane) {
        plane.setState(AircraftStates.PARK);
        plane.freeAirstrip();
    }
}
