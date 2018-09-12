package com.donotpanic.airport.domain.airport;

/**
 * Created by aleonets on 21.08.2017.
 */
public class PassengerService {
    private Airport linkedAirport;

    public PassengerService(Airport airport){
        this.linkedAirport = airport;
    }

    public void setLinkedAirport(Airport airport){
        this.linkedAirport = airport;
    }
}
