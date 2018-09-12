package com.donotpanic.airport.domain.location;

import com.donotpanic.airport.domain.airport.Airport;
import com.donotpanic.airport.domain.common.Named;

public interface AirportLocation extends Named, CoordinateObject {
    Airport getAirport();

}
