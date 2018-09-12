package com.donotpanic.airport.domain.airport;

import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.Engine.GlobalObject;
import com.donotpanic.airport.domain.Engine.ObjectType;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.common.Actions;
import com.donotpanic.airport.domain.common.PrintService;
import com.donotpanic.airport.domain.location.*;
import com.donotpanic.airport.domain.location.AirportLocation;

import java.awt.*;

public class RadioTower implements AirportLocation, GlobalObject {
    private Airport linkedAirport;
    private GlobalEngine globalEngine;

    private ObjectType objType = ObjectType.RADIO_TOWER;

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public ObjectType getObjectType() {
        return this.objType;
    }

    RadioTower setCoordinates(){
        return this;
    }

    @Override
    public Airport getAirport() {
        return this.linkedAirport;
    }

    @Override
    public String getObjectName() {
        return "Radio Tower(" + linkedAirport.getAirportName()+ ")";
    }

    @Override
    public Coordinates getCoordinates() {
        return globalEngine.getObjectCoordinates(this);
    }

    RadioTower(Airport airport, GlobalEngine globalEngine){
        this.linkedAirport = airport;
        this.globalEngine = globalEngine;
    }

    void setLinkedAirport(Airport airport){
        this.linkedAirport = airport;
    }

    public <T extends PlaneAirportLocation> T requestPlaneLocation(Plane plane, AirportObjects requestedObject){
        T resultObject = null;

        if (plane.getCurrentRoute() != null && !linkedAirport.checkRouteRegistration(plane.getCurrentRoute())){
            PrintService.printMessageObj("Route("+plane.getCurrentRoute().getRouteName()+") is not registered!", this);
            return null;
        }

        if (requestedObject == AirportObjects.AIRSTRIP){
            PrintService.printMessageObj(plane.getObjectName() +" has requested an airstrip!", this);
            Actions.RADIOTOWER_REQUEST.doAction();
            resultObject = (T)linkedAirport.getPlaneService().requestAirstrip(plane);
        }else if (requestedObject == AirportObjects.PLANEPARKINGPLACE){
            PrintService.printMessageObj(plane.getObjectName() +" has requested a parking place!", this);
            Actions.RADIOTOWER_REQUEST.doAction();
            resultObject = (T)linkedAirport.getPlaneService().requestParking(plane);
        }

        if (resultObject == null){
            PrintService.printMessageObj("There is not a free requested location for the " + plane.getObjectName() +"!", this);
        }

        return resultObject;
    }

    public <T extends PlaneAirportLocation> T getReservedLocation(Plane plane, AirportObjects requestedObject){
        T resultObject = null;
        if (requestedObject == AirportObjects.AIRSTRIP){
         //   Actions.RADIOTOWER_REQUEST.doAction();
            resultObject = (T)linkedAirport.getPlaneService().requestReservedAirstrip(plane);
        }else if (requestedObject == AirportObjects.PLANEPARKINGPLACE){
         //   Actions.RADIOTOWER_REQUEST.doAction();
            resultObject = (T)linkedAirport.getPlaneService().requestReservedParking(plane);
        }
      return resultObject;
    }

    public void freePlaneLocations(Plane plane, AirportObjects requestedObject){
        PlaneAirportLocation reservedLocation = getReservedLocation(plane, requestedObject);
        if (reservedLocation != null){
            linkedAirport.getPlaneService().freePlaneLocation(reservedLocation);
        }
    }

    public void requestPlaneRoute(Plane plane){
        linkedAirport.getPlaneService().requestRoute(plane);
    }

}
