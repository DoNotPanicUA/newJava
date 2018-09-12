package com.donotpanic.airport.domain.aircraft;

import java.util.ArrayList;

public enum AircraftStates {
    PENDING_INI,
    PARK, MOVE_PARK_2_AIRSTRIP, READY_TO_TAKEOFF, TAKE_OFF, FLY, FLY_2_AIRPORT, FLY_2_AIRSTRIP, LANDING, LANDED_AIRSTRIP, MOVE_AIRSTRIP_2_PARK;

    private static ArrayList<AircraftStates> flyActions = new ArrayList<>();

    static{
        flyActions.clear();
        for (AircraftStates state : AircraftStates.values()){
            if (state.name().matches(".*FLY.*")){
                flyActions.add(state);
            }
        }

        System.out.println("A");


    }

    public static ArrayList<AircraftStates> getFlyActions(){
        return flyActions;
    }
}
