package com.donotpanic.airport.domain.aircraft;

import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.airport.Airport;
import com.donotpanic.airport.domain.common.PrintService;
import com.donotpanic.airport.domain.common.TimeInMilliSec;
import com.donotpanic.airport.domain.location.AirportLocation;
import com.donotpanic.airport.domain.location.Direction;
import com.donotpanic.airport.domain.location.GeoLocationService;

import java.util.Date;

public abstract class Route {
    private String routeName;
    private AirportLocation destinationFrom;
    private AirportLocation destinationTo;
    private Double distanceKM;
    private Date flyDate;
    private Date arrivingDate;
    private Plane assignedPlane;

    private CommonServices commonServices = CommonServices.getCommonServices();

    private Direction direction;

    Route(String name){
        this.routeName = name;
    }

    public AirportLocation getDestinationFrom() {
        return destinationFrom;
    }

    public AirportLocation getDestinationTo() {
        return destinationTo;
    }

    public Double getDistance() {
        return distanceKM;
    }

    public Date getFlyDate() {
        return flyDate;
    }

    public Plane getAssignedPlane() {
        return assignedPlane;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getRouteName() {
        return routeName;
    }

    public Double getDistanceKM() {
        return distanceKM;
    }

    public Date getArrivingDate() {
        return arrivingDate;
    }

    Route setDestination(AirportLocation destinationFrom, AirportLocation destinationTo) {
        this.destinationTo = destinationTo;
        this.destinationFrom = destinationFrom;
        distanceKM = commonServices.getGeoLocationService().calculateDistance(destinationTo.getCoordinates(), destinationFrom.getCoordinates());
        direction = commonServices.getGeoLocationService().getDirection(destinationTo.getCoordinates(), destinationFrom.getCoordinates());
        return this;
    }

    private void predictArriving(Plane plane){
        this.arrivingDate = new Date();
        arrivingDate.setTime(Long.sum(arrivingDate.getTime(), Math.abs(Math.round(Double.sum(1000 * (distanceKM / plane.getPlaneMaxFlySpeed()), TimeInMilliSec.FIVEMINUTES.getTimeInMilliSecs())))));
    }

    private void setFlyDate(){
        Date timeShift = new Date();
        timeShift.setTime(Long.sum(timeShift.getTime(), TimeInMilliSec.MINUTE.getTimeInMilliSecs()));
        this.flyDate = timeShift;
    }

    private void registerRouteInAirports(){
        this.getDestinationFrom().getAirport().registerRoute(this);
        this.getDestinationTo().getAirport().registerRoute(this);
    }

    void registerRoute(Plane plane){
        registerRouteInAirports();
        this.assignedPlane = plane;
        plane.setRoute(this);
        setFlyDate();
        predictArriving(plane);
        PrintService.printRoute(this);
    }

    public boolean isArrivedDestinationAirport(Airport airport){
        return (destinationTo.getAirport().equals(airport));
    }
}
