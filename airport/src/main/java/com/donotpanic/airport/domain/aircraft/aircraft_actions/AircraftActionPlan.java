package com.donotpanic.airport.domain.aircraft.aircraft_actions;

import com.donotpanic.airport.domain.aircraft.Plane;
import com.donotpanic.airport.domain.common.PrintService;

import java.util.ArrayList;
import java.util.Collections;

public class AircraftActionPlan {
    private ArrayList<LinkedAircraftAction> linkedListOfActions = new ArrayList<>();
    private ArrayList<AircraftAction> activeActions = new ArrayList<>();
    private boolean finishedActionFlag = false;
    private boolean isPlanFinished = false;


    public boolean isPlanFinished() {
        return isPlanFinished;
    }

    private static class LinkedAircraftAction{
        private Object previousAction;
        private AircraftAction action;

        LinkedAircraftAction(Object previousAction, AircraftAction action){
            this.previousAction = previousAction;
            this.action = action;
        }

        public Object getPreviousAction() {
            return previousAction;
        }

        public AircraftAction getAction() {
            return action;
        }
    }

    public void addLinkedAction(Object previousAction, AircraftAction action){
        linkedListOfActions.add(new LinkedAircraftAction(previousAction, action));
    }

    public void resetPlan(){
        if (activeActions.isEmpty()){
            for (LinkedAircraftAction i : linkedListOfActions){
                i.getAction().resetAction();
            }
            this.isPlanFinished = false;
        }
    }

    private ArrayList<AircraftAction> getNextActions(ArrayList<Object> objects){
        ArrayList<AircraftAction> resultList = new ArrayList<>();
        for (Object obj : objects){
            for (LinkedAircraftAction i : linkedListOfActions){
                if (i.getPreviousAction().equals(obj) && !i.getAction().isFinished()){
                    if (!resultList.contains(i.getAction())) {
                        resultList.add(i.getAction());
                    }
                }
            }
        }
        return resultList;
    }

    private ArrayList<AircraftAction> getNextActionsByObject(Object object){
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(object);
        return getNextActions(arrayList);
    }

    private boolean checkInitialActionList(ArrayList<AircraftAction> initialActions){
        boolean result = (!initialActions.isEmpty());
        for (AircraftAction i : initialActions){
            if (i.isFinished() || i.isInProgress()){
                result = false;
            }
        }
        return result;
    }

    public boolean followThePlan(Plane plane) throws Throwable{
        if (!isPlanFinished()) {
            if (activeActions.isEmpty()) {
                ArrayList<AircraftAction> initialActions = getNextActionsByObject(this);
                if (checkInitialActionList(initialActions)) {
                    activeActions.addAll(initialActions);
                }else{
                    this.isPlanFinished = true;
                    PrintService.printMessageObj("Plan has been finished!", plane);
                    plane.finishRoute();
                }
            } else if (finishedActionFlag) {
                ArrayList<Object> finishedActions = new ArrayList<>();
                for (AircraftAction i : activeActions) {
                    if (i.isFinished()) {
                        finishedActions.add(i);
                    }
                }
                activeActions.addAll(getNextActions(finishedActions));
                activeActions.removeAll(finishedActions);
                finishedActionFlag = false;
                Collections.sort(activeActions);
            }

            for (AircraftAction i : activeActions) {
                if (!i.isFinished()) {
                    i.doAction(plane);
                    if (i.isFinished()) {
                        finishedActionFlag = true;
                    }
                }
            }
        }

        return !this.isPlanFinished; /*Return TRUE if plan has steps*/
    }
}
