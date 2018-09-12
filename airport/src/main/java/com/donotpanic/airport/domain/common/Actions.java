package com.donotpanic.airport.domain.common;


import com.donotpanic.airport.domain.Engine.CommonServices;
import com.donotpanic.airport.domain.Engine.GlobalEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

public enum Actions {
    STANDBY(TimeInMilliSec.TENSECOND.getTimeInMilliSecs()),
    STANDBY_5SEC(TimeInMilliSec.FIVESECONDS.getTimeInMilliSecs()),

    DRAW_FPS(TimeInMilliSec.SECOND.getTimeInMilliSecs()/5),//DRAW_FPS(TimeInMilliSec.SECOND.getTimeInMilliSecs()),

    PLANE_TAKEOFF(TimeInMilliSec.TENSECOND.getTimeInMilliSecs()),
    PLANE_REQUESTRADIOTOWER(TimeInMilliSec.FIVESECONDS.getTimeInMilliSecs()),
    PLANE_LAND(TimeInMilliSec.TENSECOND.getTimeInMilliSecs()),
    PLANE_FLYMIN(TimeInMilliSec.SECOND.getTimeInMilliSecs()/2),//PLANE_FLYMIN(TimeInMilliSec.TENSECOND.getTimeInMilliSecs()),
    PLANE_GROUNDMOVE(TimeInMilliSec.SECOND.getTimeInMilliSecs()/2),//PLANE_GROUNDMOVE(3*TimeInMilliSec.FIVESECONDS.getTimeInMilliSecs()),

    RADIOTOWER_REQUEST(3*TimeInMilliSec.FIVESECONDS.getTimeInMilliSecs());

    private int duration;
    private GlobalEngine engine = CommonServices.getCommonServices().getGlobalEngine();

//    public void setGlobalEngine(GlobalEngine globalEngine){
//        System.out.println(this.toString() + globalEngine.toString());
//        this.engine = globalEngine;
//    }

    public double getDurationSec(){
        return (double)this.duration/1000;
    }

    public int getDurationMilliSec(){
        return this.duration;
    }

    Actions(int duration){
        this.duration = duration;
    }

    public void doAction(){
        try{
            Thread.sleep(Math.round(this.duration/ engine.getEngineSettings().getGameSpeed()));
        }catch(InterruptedException e){

        }
    }
}
