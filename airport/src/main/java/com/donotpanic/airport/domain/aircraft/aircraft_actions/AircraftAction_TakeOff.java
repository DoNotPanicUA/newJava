package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.aircraft.AircraftStates;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.airport.Airstrip;

/**
 * Created by DoNotPanic-NB on 23.01.2018.
 */
public class AircraftAction_TakeOff extends AircraftAction {

    @Override
    protected boolean checkPreCondition(Plane plane) {
        return (getEngine().getBuilding(plane.getCoordinates()) instanceof Airstrip &&
                plane.getState().equals(AircraftStates.READY_TO_TAKEOFF));
    }

    @Override
    protected void preAction(Plane plane) {
        plane.setState(AircraftStates.TAKE_OFF);
    }

    @Override
    protected void action(Plane plane) throws Throwable {
        plane.takeOff();
    }

    @Override
    protected boolean checkEndOfAction(Plane plane) {
        return true;
    }

    @Override
    protected void postAction(Plane plane) {
        plane.setState(AircraftStates.FLY);
        plane.leaveAirport();
    }
}
