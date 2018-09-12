package com.donotpanic.airport.domain.Engine;

import com.donotpanic.airport.dao.AirportDAO;
import com.donotpanic.airport.domain.aircraft.AircraftFactory;
import com.donotpanic.airport.domain.airport.Airport;
import com.donotpanic.airport.domain.airport.AirportFactory;
import com.donotpanic.airport.domain.common.Actions;
import com.donotpanic.airport.domain.common.MovableObject;
import com.donotpanic.airport.domain.common.PrintService;
import com.donotpanic.airport.domain.location.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public final class GlobalEngine {
    private ArrayList<Airport> airports = new ArrayList<>();
    private HashMap<GlobalObject, Coordinates> globalMap = new HashMap<>();
    private HashMap<Coordinates, GlobalObject> interceptObjects = new HashMap<>();
    private HashMap<Coordinates, GlobalObject> buildings = new HashMap<>();
    private EngineSettings engineSettings;
    private AirportDAO airportDAO;

//    private GlobalEngine(){
//        getGlobalEngine();
//    }

//    private GlobalEngine globalEngine;
//    private DrawEngine drawEngine;

    @Bean(name = "GlobalEngine")
    @Scope("singleton")
    GlobalEngine globalEngine(){
        System.out.println("Ini");
        return this;
    }

    private GlobalEngine(){
        EngineSettings settings = new EngineSettings();
        settings.setGlobalMapHeight(200d);
        settings.setGlobalMapWidth(380d);
        settings.setAirportMinimalDistance(50d);
        settings.setBuildingMinimalDistance(1.6d);
        settings.setRestrictedLine(20d);
        settings.setGameSpeed(1);
        settings.setCircleSides(18);
        this.setEngineSettings(settings);
//        this.drawEngine = new DrawEngine();
//        System.out.println(this);
//        this.drawEngine.iniFrame("AirRadar", this.engineSettings.getGlobalMapWidth(), this.engineSettings.getGlobalMapHeight());
    }

    //public static GlobalEngine getGlobalEngine(){
        //ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        //System.out.println("Execute!!!");
    //    return context.getBean("GlobalEngine", GlobalEngine.class);
    //}

    private void setEngineSettings(EngineSettings engineSettings) {
        this.engineSettings = engineSettings;
    }

    public void startVisualisation(){
        CommonServices.getCommonServices().getDrawEngine().iniFrame("AirRadar", this.engineSettings.getGlobalMapWidth(), this.engineSettings.getGlobalMapHeight());
    }

    public EngineSettings getEngineSettings() {
        return engineSettings;
    }

    public Coordinates getObjectCoordinates(GlobalObject object){
        return globalMap.get(object);
    }

    private boolean checkAirportCoordinates(Coordinates coordinates){
        boolean result = true;
        for (Airport airport : airports){
            if (CommonServices.getCommonServices().getGeoLocationService().calculateDistance(coordinates, getObjectCoordinates(airport)) <= engineSettings.getAirportMinimalDistance()){
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean checkBuildingCoordinates(Coordinates coordinates){
        boolean result = true;
        for (Coordinates existingCoordinates : buildings.keySet()){
            if (CommonServices.getCommonServices().getGeoLocationService().calculateDistance(coordinates, existingCoordinates) < engineSettings.getBuildingMinimalDistance()){
                System.out.println(CommonServices.getCommonServices().getGeoLocationService().calculateDistance(coordinates, existingCoordinates) + " is less than " +engineSettings.getBuildingMinimalDistance());
                result = false;
                break;
            }
        }
        return result;
    }

    private boolean checkInterceptObjects(Coordinates coordinates){
        return true;//(interceptObjects.get(coordinates) == null);
    }

    public synchronized void registerAirport(Airport airport) throws InvalidCoordinatesException{
        Coordinates coordinates = null;
        for (int i = 0; i < 10000 ; i++){
            if (coordinates == null){
                 coordinates = CommonServices.getCommonServices().getCoordinateFactory().getRandomCoordinates();
            }
            if (checkAirportCoordinates(coordinates)){
               break;
            }else{
                coordinates = null;
            }
        }

        if (coordinates != null){
            airports.add(airport);
            registerCoordinates(coordinates, airport);
        }else {
            throw new InvalidCoordinatesException("There is no free coordinates for airport");
        }
    }

    public synchronized void registerAirportBuilding(Airport airport, GlobalObject building, Direction direction, double distance) throws InvalidCoordinatesException{
        Coordinates airportCoor = getObjectCoordinates(airport);
        if (airportCoor != null){
            Coordinates objectCoordinates = CommonServices.getCommonServices().getGeoLocationService().calculateNewCoordinates(airportCoor, direction, distance);
            registerGlobalObject(building, objectCoordinates);
        }else{
            throw new InvalidCoordinatesException("Airport is not registered");
        }
    }

    public synchronized void registerGlobalObject(GlobalObject object, Coordinates objectCoordinates) throws InvalidCoordinatesException{
        if (globalMap.get(object) != null){
            throw new InvalidCoordinatesException("Object location is already registered");
        }else if (object.getObjectType().getIsBuilding() && !checkBuildingCoordinates(objectCoordinates)){
            throw new InvalidCoordinatesException("Too close to another building");
        }else if (!object.getObjectType().getIsBuilding() && object.getObjectType().isCrashIfTwo() && !checkInterceptObjects(objectCoordinates)){
            throw new InvalidCoordinatesException("Already occupied coordinates by another object");
        }else{
            registerCoordinates(objectCoordinates, object);
        }
    }

    private synchronized void updateInterceptCoordinates(Coordinates oldCoor, Coordinates newCoor, GlobalObject object) throws InvalidCoordinatesException{
        if (!object.getObjectType().isCrashIfTwo()){
            /*Non intercepting object*/
            return;
        }

        interceptObjects.remove(oldCoor);

        if (!checkInterceptObjects(newCoor)){
            //CRASH OBJECTS
            throw new InvalidCoordinatesException("CRASH!");
        }else{
            interceptObjects.put(newCoor, object);
        }
    }

    private void updateExistingCoordinates(Coordinates coordinates, GlobalObject object) throws InvalidCoordinatesException{
        if (object.getObjectType().getIsBuilding()){
            throw new InvalidCoordinatesException("Can't move building");
        }
        globalMap.put(object, coordinates);
    }

    private void registerCoordinates(Coordinates coordinates, GlobalObject object) throws InvalidCoordinatesException{
        if (globalMap.containsKey(object)){
            /*Update for Existing object*/
            Coordinates currentCoordinates = globalMap.get(object);
            if (!currentCoordinates.equals(coordinates)){
                updateExistingCoordinates(coordinates, object);
                updateInterceptCoordinates(currentCoordinates, coordinates, object);
                PrintService.printMessageObj("Moved to new coordinates", object);
            }
        }else{
            /*register new object*/
            globalMap.put(object, coordinates);
            updateInterceptCoordinates(null, coordinates, object);
            PrintService.printMessageObj("Registered as new object", object);
            if (object.getObjectType().getIsBuilding()){
                buildings.put(coordinates, object);
            }
            CommonServices.getCommonServices().getDrawEngine().addGlobalObject(object);
        }
    }

    public Coordinates moveObjectByDirection(MovableObject object, Actions moveAction, Direction direction) throws Throwable{
        Coordinates currentCoordinates = getObjectCoordinates(object);
        if (currentCoordinates == null){
            throw new InvalidCoordinatesException("Object is not registered");
        }

        Coordinates newCoordinates = CommonServices.getCommonServices().getCoordinateFactory().getCoordinates(currentCoordinates.getX() + direction.getCoefX() * object.getSpeed() * moveAction.getDurationSec(),
                currentCoordinates.getY() + direction.getCoefY() * object.getSpeed() * moveAction.getDurationSec());

        moveAction.doAction();

        registerCoordinates(newCoordinates, object);

        return newCoordinates;
    }

    public boolean moveObjectToCoordinates(MovableObject object, Actions moveAction, Coordinates destination) throws Throwable{
        if (CommonServices.getCommonServices().getGeoLocationService().calculateDistance(destination,getObjectCoordinates(object)) <= object.getSpeed()*moveAction.getDurationSec()){
            moveAction.doAction();
            registerCoordinates(destination, object);
            /*Object reached destination coordinates*/
            return true;
        }else {
            moveObjectByDirection(object, moveAction, CommonServices.getCommonServices().getGeoLocationService().getDirection(destination, getObjectCoordinates(object)));
            return false;
        }
    }

    public GlobalObject getBuilding(Coordinates coordinates){
        return buildings.get(coordinates);
    }

    @Autowired
    public void setAirportDAO(AirportDAO airportDAO) {
        this.airportDAO = airportDAO;
    }

}
