package com.donotpanic.airport.domain.airport;

import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.aircraft.RouteFactory;

import java.util.Iterator;

/**
 * Created by aleonets on 21.08.2017.
 */
public class PlaneService {
    private Airport linkedAirport;

    PlaneService(Airport airport){
        this.linkedAirport = airport;
    }

    void setLinkedAirport(Airport airport){
        this.linkedAirport = airport;
    }

    Airstrip requestAirstrip(Plane plane){
        return (Airstrip) requestPlaneLocation(plane, linkedAirport.getAirstrips().iterator());
    }

    PlaneParkingPlace requestParking(Plane plane){
        return (PlaneParkingPlace) requestPlaneLocation(plane, linkedAirport.getParkingPlaces().iterator());
    }

    Airstrip requestReservedAirstrip(Plane plane){
        return (Airstrip) getReservedLocation(plane, linkedAirport.getAirstrips().iterator());
    }

    PlaneParkingPlace requestReservedParking(Plane plane){
        return (PlaneParkingPlace)getReservedLocation(plane, linkedAirport.getParkingPlaces().iterator());
    }

    private <T extends PlaneAirportLocation> PlaneAirportLocation getReservedLocation(Plane plane, Iterator<T> iterator){
        T resultObject = null;

        while(iterator.hasNext()){
            resultObject = iterator.next();
            if (!resultObject.checkReserveBy(plane)){
                resultObject = null;
            }else{
                break;
            }
        }
        return resultObject;
    }

    private synchronized <T extends PlaneAirportLocation> PlaneAirportLocation requestPlaneLocation(Plane plane, Iterator<T> iterator){
        PlaneAirportLocation resultObject = null;
        PlaneAirportLocation freePlace = null;

        while (iterator.hasNext() & resultObject == null){
            PlaneAirportLocation planeLocation = iterator.next();
            if (planeLocation.checkReserveBy(plane)){
                resultObject = planeLocation;
            }

            if (planeLocation.checkIsLocationFree() && freePlace == null){
                freePlace = planeLocation;
            }
        }

        if (resultObject == null && freePlace != null){
            freePlace.reserveLocation(plane);
            resultObject = freePlace;
        }

        return resultObject;
    }

    synchronized void freePlaneLocation(PlaneAirportLocation planeAirportLocation){
        planeAirportLocation.freeReserveLocation();
    }

    synchronized void requestRoute(Plane plane){
        RouteFactory.requestRouteFrom(this.linkedAirport, plane);
    }

}
