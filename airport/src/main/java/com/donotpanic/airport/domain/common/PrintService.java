package com.donotpanic.airport.domain.common;

import com.donotpanic.airport.domain.aircraft.Route;
import com.donotpanic.airport.domain.location.CoordinateObject;
import com.donotpanic.airport.domain.location.Coordinates;

import java.util.Date;

/**
 * Created by DoNotPanic-NB on 30.10.2017.
 */
public class PrintService {
    public static void printMessage(String messageText){
        System.out.println("["+new Date()+"] " + messageText);
    }

    public static void printMessageObj(String messageText, Object obj){
        String coorString = "";
        String name = "";

        if (obj instanceof CoordinateObject){
            Coordinates coordinates = ((CoordinateObject) obj).getCoordinates();
            coorString = (coordinates != null ? String.format("%-10s","[X:" + coordinates.getShortX() +", ") + String.format("%-7s","Y:"+ coordinates.getShortY()) +"]" : "");
        }

        if (obj instanceof Named){
            name = ((Named) obj).getObjectName();
        }

        printMessage((coorString != ""? coorString + " " : coorString) + (name != "" ? name + ": " : name ) + messageText);
    }

    public static void printRoute(Route route){
        printMessage("Route(" + route.getRouteName() + ") from " + route.getDestinationFrom().getObjectName() + " to " + route.getDestinationTo().getObjectName() +
        " is assigned to " + route.getAssignedPlane().getObjectName() + ". Distance: " + Math.round(route.getDistanceKM()/10) + " km, Fly at " + route.getFlyDate().toString() + " with arriving at " + route.getArrivingDate().toString());
    }
}
