package com.donotpanic.airport.domain.Engine;

import com.donotpanic.airport.domain.aircraft.AircraftFactory;
import com.donotpanic.airport.domain.airport.AirportFactory;
import com.donotpanic.airport.domain.location.CoordinateFactory;
import com.donotpanic.airport.domain.location.GeoLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class CommonServices {

    private static CommonServices commonServices;
    /*Services*/
    private CoordinateFactory  coordinateFactory;
    private GeoLocationService geoLocationService;
    private AirportFactory     airportFactory;
    private AircraftFactory    aircraftFactory;
    private GlobalEngine       globalEngine;
    private DrawEngine         drawEngine;

    private ApplicationContext mainContext;

    @Bean(name = "CommonServices")
    CommonServices instCommonServices(){
        commonServices = new CommonServices();
        System.out.println("CommonS");
        return commonServices;
    }

    public static CommonServices getCommonServices(){
        return commonServices;
    }

    private CommonServices(){

    }

    /*Factories*/
    public CoordinateFactory getCoordinateFactory() {
        return coordinateFactory;
    }

    public AircraftFactory getAircraftFactory() {
        return aircraftFactory;
    }

    public AirportFactory getAirportFactory() {
        return airportFactory;
    }

    /*Global services*/
    public GeoLocationService getGeoLocationService() {
        return geoLocationService;
    }

    public GlobalEngine getGlobalEngine() {
        return globalEngine;
    }

    public ApplicationContext getMainContext() { return mainContext; }

    /*Local services*/
    DrawEngine getDrawEngine() { return drawEngine; }

    /*Setters*/
    @Autowired
    public void setGeoLocationService(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }

    @Autowired
    public void setCoordinateFactory(CoordinateFactory coordinateFactory) {
        this.coordinateFactory = coordinateFactory;
    }

    @Autowired
    public void setAirportFactory(AirportFactory airportFactory) {
        this.airportFactory = airportFactory;
    }

    @Autowired
    public void setAircraftFactory(AircraftFactory aircraftFactory) {
        this.aircraftFactory = aircraftFactory;
    }

    @Autowired
    public void setGlobalEngine(GlobalEngine globalEngine) {
        this.globalEngine = globalEngine;
    }

    @Autowired
    public void setDrawEngine(DrawEngine drawEngine) {
        this.drawEngine = drawEngine;
    }

    public void setMainContext(ApplicationContext mainContext) {
        this.mainContext = mainContext;
    }
}
