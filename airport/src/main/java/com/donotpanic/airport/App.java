package com.donotpanic.airport;

import com.donotpanic.airport.dao.AirportDAO;
import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.airport.Airport;
import com.donotpanic.airport.domain.airport.AirportFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class App {
    private static List<Airport> knownAirports;
    private static List<Plane> knownPlanes;
    public static void runApplication(ApplicationContext mainContext){
//        ApplicationContext mainContext = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        CommonServices.getCommonServices().setMainContext(mainContext);
        CommonServices.getCommonServices().getGlobalEngine().startVisualisation();
        try{
            knownAirports = CommonServices.getCommonServices().getAirportFactory().getAirports();//createAirports(CommonServices.getCommonServices().getAirportFactory());
            knownPlanes = createPlanes();
        }catch (Throwable e){
            e.printStackTrace();
        }

        ((AirportDAO)mainContext.getBean("OracleDAO")).registerCoordinate(CommonServices.getCommonServices().getCoordinateFactory().getRandomCoordinates());
    }

//    private static List<Airport> createAirports(AirportFactory airportFactory) throws Throwable{
//        List<Airport> airports = new ArrayList<>();
//
//        airports.add(airportFactory.getInstanceAirport("Borispol"));
//        airports.add(airportFactory.getInstanceAirport("John F. Kennedy"));
//        airports.add(airportFactory.getInstanceAirport("Heathrow"));
//        airports.add(airportFactory.getInstanceAirport("Seattle-Tacoma"));
//
//        return airports;
//    }

    public static Airport getRandomKnownAirport(){
        Airport result = null;

        int randInt = new Random().nextInt(knownAirports.size());

        if (randInt >= 0){
            result = knownAirports.get(randInt);
        }

        return  result;
    }

    private static List<Plane> createPlanes() throws Throwable{
        List<Plane> planes = new ArrayList<>();

        Plane newPlane = CommonServices.getCommonServices().getAircraftFactory().getPassengerPlane("APEK-005", knownAirports.get(0), "Boeing 737-100");
        planes.add(newPlane);

        newPlane = CommonServices.getCommonServices().getAircraftFactory().getPassengerPlane("FNAS-005", knownAirports.get(1), "Boeing 747-8");
        planes.add(newPlane);

        newPlane = CommonServices.getCommonServices().getAircraftFactory().getPassengerPlane("FD-221", knownAirports.get(2), "AN-2");
        planes.add(newPlane);

        newPlane = CommonServices.getCommonServices().getAircraftFactory().getPassengerPlane("RR-521", knownAirports.get(3), "AN-2");
        planes.add(newPlane);

        newPlane = CommonServices.getCommonServices().getAircraftFactory().getPassengerPlane("KV-552", knownAirports.get(0), "AN-2");
        planes.add(newPlane);

        return planes;
    }
}
