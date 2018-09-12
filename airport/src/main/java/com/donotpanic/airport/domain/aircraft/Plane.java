package com.donotpanic.airport.domain.aircraft;

import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.Engine.GlobalObject;
import com.donotpanic.airport.domain.Engine.ObjectType;
import com.donotpanic.airport.domain.aircraft.aircraft_actions.AircraftActionPlan;
import com.donotpanic.airport.domain.airport.*;
import com.donotpanic.airport.domain.common.Actions;
import com.donotpanic.airport.domain.common.MovableObject;
import com.donotpanic.airport.domain.common.Named;
import com.donotpanic.airport.domain.common.PrintService;
import com.donotpanic.airport.domain.location.*;

import java.awt.*;

public class Plane implements Named, CoordinateObject, Runnable, MovableObject, GlobalObject {
    private AircraftStates state;
    private Route route;
    private RadioTower linkedRadioTower;
    private final String boardName;
    private final double planeMaxFlySpeed/*Realistic 0.2 km/sec*/;
    private final double planeMaxGroundSpeed;
    private ObjectType objType = ObjectType.AIRCRAFT;
    private final GlobalEngine engine;
    private Actions moveAction = Actions.PLANE_FLYMIN;
    private Airport currentAirport = null;

    private double planeCurrentSpeed = 0d;
    //private ArrayList<PlaneAirportLocation> reservedLocations = new ArrayList<>();

    private Coordinates destinationFrom = null;
    private Coordinates destinationTo = null;
    private AircraftActionPlan actionPlan = null;

    private Color planeColor = Color.GREEN;

    void setColor(Color color){
        this.planeColor = color;
    }

    @Override
    public Color getColor() {
        return this.planeColor;
    }

    public AircraftStates getState() {
        return state;
    }

//    void setState(AircraftStates state) {
//        this.state = state;
//    }

    private void iniPlane() throws Throwable{
        if (this.state == AircraftStates.PENDING_INI){
            if (currentAirport != null){
                PlaneParkingPlace parking = currentAirport.getRadioTower().requestPlaneLocation(this, AirportObjects.PLANEPARKINGPLACE);
                if (parking != null) {
                    engine.registerGlobalObject(this, parking.getCoordinates());
                    this.state = AircraftStates.PARK;
                    this.planeCurrentSpeed = this.planeMaxGroundSpeed;
                }
            }else{
                PrintService.printMessageObj("Required initialization", this);
            }
        }
    }

    void setActionPlan(AircraftActionPlan actionPlan){
        this.actionPlan = actionPlan;
    }

    @Override
    public ObjectType getObjectType() {
        return objType;
    }

    private double getDistancePerAction(Actions action){
        return getPlaneMaxFlySpeed() * action.getDurationSec();
    }

    @Override
    public double getSpeed() {
        return (this.planeCurrentSpeed);
    }

    Plane(String boardName, Double planeMaxFlySpeed, double planeMaxGroundSpeed, GlobalEngine globalEngine){
        this.boardName = boardName;
        this.planeMaxFlySpeed = planeMaxFlySpeed;
        this.planeMaxGroundSpeed = planeMaxGroundSpeed;
        this.state = AircraftStates.PENDING_INI;
        this.engine = globalEngine;
        PrintService.printMessageObj("The plane has been initialized.", this);
        //setCurrentAirportLocation(location);
    }

    void iniByAirport(Airport airport){
        this.currentAirport = airport;
    }

    void setRoute(Route route){
        this.route = route;
        PrintService.printMessageObj("A new rout from " + route.getDestinationFrom().getObjectName() + " to " +
                route.getDestinationTo().getObjectName() + " has been assigned", this);
    }

    void setCurrentAirport(Airport airport){
        this.currentAirport = airport;
    }

    public Route getCurrentRoute(){
        return this.route;
    }

    public Airport getCurrentAirport(){
        return currentAirport;
    }

    @Override
    public Coordinates getCoordinates() {
        return engine.getObjectCoordinates(this);
    }

    private boolean requestRadioTower(){
        Actions.PLANE_REQUESTRADIOTOWER.doAction();
        Airport airport = getCurrentAirport();
        if (airport != null){
            linkedRadioTower = airport.getRadioTower();
            PrintService.printMessageObj("Linked with local Radio Tower" + linkedRadioTower.getObjectName(), this);
        }else if (route != null){
            linkedRadioTower = route.getDestinationTo().getAirport().getRadioTower();
            PrintService.printMessageObj("Linked with foreign Radio Tower" + linkedRadioTower.getObjectName(), this);
        }
        if (linkedRadioTower == null){
            PrintService.printMessageObj("An radio tower doesn't reply.", this);
        }
        return (linkedRadioTower != null);
    }

    public double getPlaneMaxFlySpeed(){
        return this.planeMaxFlySpeed;
    }

    private void requestRoute(){
        if (this.route == null & getCurrentAirport() != null){
            getCurrentAirport().getRadioTower().requestPlaneRoute(this);
            this.actionPlan.resetPlan();
        }
    }

    @Override
    public String getObjectName() {
        return "Plane("+boardName+")";
    }

    public void crashPlane(){
        Thread.currentThread().interrupt();
        PrintService.printMessageObj(this.getObjectName() + " is crashed!!!", this);
    }

    public void setState(AircraftStates state) {
        PrintService.printMessageObj("State has been changed from " + this.state + " to " + state, this);
        this.state = state;
    }

    public void updateDestinationFrom(){
        this.destinationFrom = engine.getObjectCoordinates(this);
    }

    public void setDestinationTo(Coordinates destinationTo) {
        this.destinationTo = destinationTo;
    }

    public Coordinates getDestinationFrom() {
        return destinationFrom;
    }

    public Coordinates getDestinationTo() {
        return destinationTo;
    }

    public Actions getMoveAction(){
        return moveAction;
    }

    public double getDistancePerAction(){
        return planeCurrentSpeed * moveAction.getDurationSec();
    }

    public Airstrip getReservedAirstrip(){
        if (currentAirport != null){
            return currentAirport.getRadioTower().getReservedLocation(this, AirportObjects.AIRSTRIP);
        }else{
            return route.getDestinationTo().getAirport().getRadioTower().getReservedLocation(this, AirportObjects.AIRSTRIP);
        }

    }

    public void requestAirstrip(){
        if (currentAirport != null){
            currentAirport.getRadioTower().requestPlaneLocation(this, AirportObjects.AIRSTRIP);
        }else{
            route.getDestinationTo().getAirport().getRadioTower().requestPlaneLocation(this, AirportObjects.AIRSTRIP);
        }
    }

    public PlaneParkingPlace getReservedParking(){
        if (currentAirport != null){
            return currentAirport.getRadioTower().getReservedLocation(this, AirportObjects.PLANEPARKINGPLACE);
        }else{
            return route.getDestinationTo().getAirport().getRadioTower().getReservedLocation(this, AirportObjects.PLANEPARKINGPLACE);
        }

    }

    public void requestParking(){
        if (currentAirport != null){
            currentAirport.getRadioTower().requestPlaneLocation(this, AirportObjects.PLANEPARKINGPLACE);
        }else{
            route.getDestinationTo().getAirport().getRadioTower().requestPlaneLocation(this, AirportObjects.PLANEPARKINGPLACE);
        }
    }

    public void freeParking(){
        if (currentAirport != null){
            currentAirport.getRadioTower().freePlaneLocations(this, AirportObjects.PLANEPARKINGPLACE);
        }
    }

    public void freeAirstrip(){
        if (currentAirport != null){
            currentAirport.getRadioTower().freePlaneLocations(this, AirportObjects.AIRSTRIP);
        }
    }

    public void takeOff(){
        planeCurrentSpeed = planeMaxFlySpeed;
    }

    public void land(){
        planeCurrentSpeed = planeMaxGroundSpeed;
        GlobalObject currentBuilding = engine.getBuilding(engine.getObjectCoordinates(this));
        if (currentBuilding != null && currentBuilding instanceof Airstrip){
            currentAirport = ((Airstrip) currentBuilding).getAirport();
        }
    }

    public void leaveAirport(){
        freeAirstrip();
        currentAirport = null;
    }

    public void finishRoute(){
        this.destinationTo = null;
        route = null;
    }


    @Override
    public void run() {
        while (true){

            try{
                iniPlane();

                if (this.route == null){
                    requestRoute();
                }else{
                    actionPlan.followThePlan(this);
                }

                if (this.route == null) {
                    Actions.STANDBY.doAction();
                }
            }catch(Throwable e){
                System.out.println("Plane thread failed: " + e.getMessage());
                e.printStackTrace(System.out);
            }
        }
    }
}
