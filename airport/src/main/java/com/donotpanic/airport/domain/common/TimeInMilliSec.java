package com.donotpanic.airport.domain.common;

/**
 * Created by aleonets on 23.08.2017.
 */
public enum TimeInMilliSec {
    SECOND(1000), FIVESECONDS(5*1000), TENSECOND(10*1000), HALFMINUTE(30*1000), MINUTE(60*1000), TWOMINUTES(2*60*1000), FIVEMINUTES(5*60*1000),
    TENMINUTES(10*60*1000), HALFHOUR(30*60*1000), HOUR(60*60*1000);

    private int timeInMilliSecs;

    TimeInMilliSec(int milliSecs){
        this.timeInMilliSecs = milliSecs;
    }

    public int getTimeInMilliSecs(){
        return this.timeInMilliSecs;
    }

    public void sleep(){
        try{
            Thread.sleep(this.timeInMilliSecs);
        }catch(InterruptedException e){}
    }
}
