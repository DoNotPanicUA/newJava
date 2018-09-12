package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.aircraft.AircraftStates;
import com.donotpanic.airport.domain.aircraft.Plane;

public class AircraftAction_Landing extends AircraftAction {

    @Override
    protected boolean checkPreCondition(Plane plane) {
        return (plane.getState() == AircraftStates.LANDING);
    }

    @Override
    protected void preAction(Plane plane) {

    }

    @Override
    protected void action(Plane plane) throws Throwable {
        plane.land();
    }

    @Override
    protected boolean checkEndOfAction(Plane plane) {
        return true;
    }

    @Override
    protected void postAction(Plane plane) {
        plane.setState(AircraftStates.LANDED_AIRSTRIP);
    }
}
