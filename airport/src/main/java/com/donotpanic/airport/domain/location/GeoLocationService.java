package com.donotpanic.airport.domain.location;

import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class GeoLocationService {

    private GlobalEngine globalEngine;

    @Bean("GeoLocationService")
    @Autowired
    GeoLocationService getGeoLocationService(@Qualifier("GlobalEngine") GlobalEngine globalEngine){
        return new GeoLocationService(globalEngine);
    }

    GeoLocationService(GlobalEngine globalEngine){
        this.globalEngine = globalEngine;
    }

    public Double calculateDistance(Coordinates coordinatesTo, Coordinates coordinatesFrom){
        return Math.sqrt(Math.pow(coordinatesTo.getX() - coordinatesFrom.getX(), 2)+
                Math.pow(coordinatesTo.getY() - coordinatesFrom.getY(), 2));
    }

    public Direction getDirection(Coordinates coordinatesTo, Coordinates coordinatesFrom){
        double sideX = coordinatesTo.getX() - coordinatesFrom.getX();
        double sideY = coordinatesTo.getY() - coordinatesFrom.getY();
        double hypo = calculateDistance(coordinatesTo, coordinatesFrom);
        return new Direction().setCoefX(sideX/hypo).setCoefY(sideY/hypo);
    }

    public Coordinates calculateNewCoordinates(Coordinates currentCoordinates, Direction direction, double distance){
        double x = direction.getCoefX() * distance + currentCoordinates.getX();
        double y = direction.getCoefY() * distance + currentCoordinates.getY();
        return CommonServices.getCommonServices().getCoordinateFactory().getCoordinates(x, y);
    }

    public Direction shiftCircleDirection(Direction currentDirection, int numberOfCircleSides){

        double x = currentDirection.getCoefX();
        double y = currentDirection.getCoefY();

        double hypo = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        double angle;

        angle = Math.round(Math.toDegrees(Math.acos(x/hypo)));

        if (angle < 0){
            angle = 360 + angle;
        }

        if (y < 0){
            angle = 360-angle;
        }

        // Get new angle
        angle = angle - 360/numberOfCircleSides;

        if (angle < 0){
            angle = 360 + angle;
        }

        return new Direction().setCoefX(Math.cos(Math.toRadians(angle))*hypo).setCoefY(Math.sin(Math.toRadians(angle))*hypo);
    }
}
