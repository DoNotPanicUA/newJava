package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.aircraft.AircraftStates;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.location.Direction;
import com.donotpanic.airport.domain.location.GeoLocationService;

public class AircraftAction_FlyCircle extends AircraftAction {

    private Direction direction;

    @Override
    protected boolean checkPreCondition(Plane plane) {
        return plane.getState().equals(AircraftStates.FLY);
    }

    @Override
    protected void preAction(Plane plane) {
        if (plane.getDestinationTo() == null){
            direction = new Direction().setCoefX(1).setCoefY(0);
        }else {
            direction = CommonServices.getCommonServices().getGeoLocationService().getDirection(plane.getDestinationTo(), plane.getDestinationFrom());
        }
    }

    @Override
    protected void action(Plane plane) throws Throwable {
        if (checkPreCondition(plane)){
            direction = CommonServices.getCommonServices().getGeoLocationService().shiftCircleDirection(direction, getEngine().getEngineSettings().getCircleSides());
            getEngine().moveObjectByDirection(plane, plane.getMoveAction(), direction);
        }
    }

    @Override
    protected boolean checkEndOfAction(Plane plane) {
        return !checkPreCondition(plane);
    }

    @Override
    protected void postAction(Plane plane) {

    }
}
