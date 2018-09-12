package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.common.Actions;

public class AircraftAction_RequestAirstrip extends AircraftAction implements Runnable{

    private Plane plane;

    @Override
    protected boolean checkPreCondition(Plane plane) {
        return (plane.getReservedAirstrip() == null);
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
        while (this.plane.getReservedAirstrip() == null) {
            this.plane.requestAirstrip();
            Actions.STANDBY_5SEC.doAction();
        }
    }
}
