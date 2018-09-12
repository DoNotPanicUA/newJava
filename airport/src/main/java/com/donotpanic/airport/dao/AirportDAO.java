package com.donotpanic.airport.dao;

import com.donotpanic.airport.domain.aircraft.AircraftFactory;
import com.donotpanic.airport.domain.aircraft.AircraftModel;
import com.donotpanic.airport.domain.airport.Airport;
import com.donotpanic.airport.domain.location.Coordinates;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AirportDAO {
    void registerNewAirport(Airport airport);
    Airport getAirportByName(String airportName);
    ArrayList<Airport> getAllAirports();

    void registerAircraftModel(AircraftModel model);
    ArrayList<AircraftModel> getAllModels();

    int registerCoordinate(Coordinates coordinates);
    void printAllCoordinates() throws SQLException;

}
