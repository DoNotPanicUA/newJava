package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.Engine.GlobalEngine;
import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.common.PrintService;

public abstract class AircraftAction implements Comparable {
    private boolean isFinished = false;
    private boolean isInProgress = false;
    private GlobalEngine engine;

    private int actionPriority = 0; /*Default max priority*/

    public final int getActionPriority() {
        return actionPriority;
    }

    public final AircraftAction setActionPriority(int actionPriority) {
        this.actionPriority = actionPriority;
        return this;
    }

    public AircraftAction setEngine(GlobalEngine engine) {
        this.engine = engine;
        return this;
    }

    final void resetAction(){
        isFinished = false;
        isInProgress = false;
    }

    final protected GlobalEngine getEngine(){
        return this.engine;
    }

    final boolean isFinished() {
        return isFinished;
    }

    final boolean isInProgress() {
        return isInProgress;
    }

    final boolean doAction(Plane plane) throws Throwable{
        if (!isFinished)
        {
            if (!isInProgress){
                if (checkPreCondition(plane)){
                    isInProgress = true;
                    preAction(plane);
                }
            }

            if (isInProgress){
                action(plane);
                if (checkEndOfAction(plane)){
                    isInProgress = false;
                    isFinished = true;
                    postAction(plane);
                    PrintService.printMessageObj(this.getClass().getName() + "action is finished!", plane);
                }
            }
        }

        return isFinished;
    }

    protected abstract void action(Plane plane) throws Throwable;

    protected abstract boolean checkPreCondition(Plane plane);

    protected abstract void preAction(Plane plane);

    protected abstract boolean checkEndOfAction(Plane plane);

    protected abstract void postAction(Plane plane);

    @Override
    public int compareTo(Object o) {
        if (o instanceof AircraftAction){
            int result;
            /*less value more priority*/
            if (((AircraftAction) o).getActionPriority() > this.getActionPriority()){
                result = 1;
            }else if (((AircraftAction) o).getActionPriority() < this.getActionPriority()){
                result = -1;
            }else{
                result = 0;
            }
            return result;
        }else{
            return 0;
        }
    }
}
