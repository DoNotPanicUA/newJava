package com.donotpanic.airport.domain.airport;

import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.Engine.ObjectType;

/**
 * Created by aleonets on 21.08.2017.
 */
public class Airstrip extends PlaneAirportLocation {

    public Airstrip(Airport airport, int number){
        super(airport, "Airstrip #" + (number) + " (" + airport.getAirportName() + ")",ObjectType.AIRSTRIP
        , CommonServices.getCommonServices().getGlobalEngine());
    }
}
