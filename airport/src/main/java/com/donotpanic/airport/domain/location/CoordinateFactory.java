package com.donotpanic.airport.domain.location;

import com.donotpanic.airport.domain.Engine.GlobalEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CoordinateFactory {

    private GlobalEngine globalEngine;

    @Bean("CoordinateFactory")
    @Autowired
    private CoordinateFactory newCoordinateFactory(@Qualifier("GlobalEngine") GlobalEngine globalEngine){
        return new CoordinateFactory(globalEngine);
    }

    private CoordinateFactory(GlobalEngine globalEngine){
        this.globalEngine = globalEngine;
    }

    private class FactoryCoordinates extends Coordinates{
        private FactoryCoordinates(double x, double y){
            super(x,y);
        }

        private FactoryCoordinates(Coordinates coordinates){
            super(coordinates.getX(),coordinates.getY());
        }
    }

    public Coordinates getCoordinates(double x, double y){
        return new FactoryCoordinates(x, y);
    }

    private double getRandom(double maxValue, double limit){
        double result = new Random().nextDouble() * maxValue;
        if (result - limit <= 0d){
            /*too close to left border*/
            result += limit;
        }

        if (result + limit >= maxValue){
            /*too close to right border*/
            result -= limit;
        }

        return result;
    }

    public Coordinates getRandomCoordinates(){
        return new FactoryCoordinates(getRandom(globalEngine.getEngineSettings().getGlobalMapWidth(), globalEngine.getEngineSettings().getRestrictedLine()),
                                      getRandom(globalEngine.getEngineSettings().getGlobalMapHeight(), globalEngine.getEngineSettings().getRestrictedLine()));
    }
}
