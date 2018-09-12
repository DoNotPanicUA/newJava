package com.donotpanic.airport.domain.airport;

import com.donotpanic.airport.dao.AirportDAO;
import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.common.PrintService;
import com.donotpanic.airport.domain.location.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Random;
@Component("AirportFactory")
public class AirportFactory {

    private GlobalEngine globalEngine;
    private AirportDAO airportDAO;

    /*private constructor*/
    @Autowired
    private AirportFactory(@Qualifier("GlobalEngine")GlobalEngine globalEngine){
        this.globalEngine = globalEngine;
    }

    public Airport getInstanceAirport(String name){
        Airport newAirport = CommonServices.getCommonServices().getMainContext().getBean(Airport.class);
        newAirport.setAirportName(name);
//        iniAirport(name, newAirport, -1, -1);
//        airportDAO.registerNewAirport(newAirport);

        return newAirport;
    }

    public ArrayList<Airport> getAirports() throws Exception{
        ArrayList<Airport> airports = airportDAO.getAllAirports();
        for (Airport airport : airports){
            iniAirport(airport.getAirportName(), airport, -1, -1);
        }
        return airports;
    }

    public Airport getInstanceAirportDetailed(String name, int numAirstrips, int numParkingPlaces) throws Throwable{
        Airport newAirport = CommonServices.getCommonServices().getMainContext().getBean(Airport.class);
        iniAirport(name, newAirport, (numAirstrips >= 1 ? numAirstrips : 1), (numParkingPlaces >= 1 ? numParkingPlaces : 1));

        return newAirport;
    }

    private void startAirport(Airport airport){
        new Thread(airport).start();
        PrintService.printMessageObj("The airport is online.", airport);
    }

    private void iniAirport(String name, Airport airport, int numAirstrips, int numParkingPlaces) throws Exception{
        airport.setAirportName(name);
        if (airport.getCoordinates() == null){
            globalEngine.registerAirport(airport);
        }

        int AirstripsCount = (numAirstrips == -1 ? new Random().nextInt(2)+1 : numAirstrips);
        int ParkingCount = (numParkingPlaces == -1 ? new Random().nextInt(AirstripsCount*10)+AirstripsCount : numParkingPlaces);

        int i = 1;
        while (i <= AirstripsCount || i <= ParkingCount) {
            if (i <= AirstripsCount) {
                Airstrip airstrip = new Airstrip(airport, i);
                globalEngine.registerAirportBuilding(airport, airstrip, new Direction().setCoefX(0d).setCoefY(1d),
                        i * (globalEngine.getEngineSettings().getBuildingMinimalDistance()+0.8d));
                airport.addAirstrip(airstrip);
            }

            if (i <= ParkingCount) {
                PlaneParkingPlace parkingPlace = new PlaneParkingPlace(airport, i);
                globalEngine.registerAirportBuilding(airport, parkingPlace, new Direction().setCoefX(1d).setCoefY(0d),
                        i * (globalEngine.getEngineSettings().getBuildingMinimalDistance()+0.2d));
                airport.addParkingPlace(parkingPlace);
            }
            i++;
        }

        PrintService.printMessageObj("The airport is initialised.", airport);
        RadioTower radioTower = new RadioTower(airport, globalEngine);
        globalEngine.registerAirportBuilding(airport, radioTower, new Direction().setCoefX(-1d).setCoefY(-0d),
                (globalEngine.getEngineSettings().getBuildingMinimalDistance()+1.0d));
        airport.setRadioTower(radioTower);
        airport.setPassengerService(new PassengerService(airport));
        airport.setPlaneService(new PlaneService(airport));

        startAirport(airport);
    }

    @Autowired
    public void setAirportDAO(AirportDAO airportDAO) {
        this.airportDAO = airportDAO;
    }
}
