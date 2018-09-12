package com.donotpanic.airport.domain.Engine;

import com.donotpanic.airport.domain.location.Coordinates;

import java.awt.*;

class DrawTemplates {
    private DrawEngine drawEngine;

    public DrawTemplates setDrawEngine(DrawEngine drawEngine) {
        this.drawEngine = drawEngine;
        return this;
    }

    public void drawObj(Graphics g, GlobalObject object){
        switch(object.getObjectType()){
            case AIRCRAFT: drawAircraft(g, object);
                break;
            case AIRSTRIP: drawAirstrip(g, object);
                break;
            case PLANE_PARKING: drawPlaneParking(g, object);
                break;
            case AIRPORT : drawAirport(g, object);
                 break;
            case RADIO_TOWER:
                break;
        }
    }

    private void drawAirport(Graphics g, GlobalObject object){
        g.setColor(Color.GREEN);
        Coordinates objCoor = object.getCoordinates();
        int x = drawEngine.getFrameIntFromCoor(objCoor.getX()) - 11;
        int y = drawEngine.getFrameIntFromCoor(objCoor.getY()) - 11;

        g.drawRect(x, y, 21, 21);
        g.drawString("A", x+7, y+15);
    }

    private void drawPlaneParking(Graphics g, GlobalObject object) {
        g.setColor(Color.GREEN);
        Coordinates objCoor = object.getCoordinates();
        int x = drawEngine.getFrameIntFromCoor(objCoor.getX()) - 6;
        int y = drawEngine.getFrameIntFromCoor(objCoor.getY()) - 6;

        g.drawRect(x, y, 11, 11);
        //g.drawString("P", x + 3, y + 7);
    }

    private void drawAircraft(Graphics g, GlobalObject object){
        g.setColor(object.getColor());
        Coordinates objCoor = object.getCoordinates();
        int x = drawEngine.getFrameIntFromCoor(objCoor.getX())-2;
        int y = drawEngine.getFrameIntFromCoor(objCoor.getY())+8;

        g.drawString("*", x, y);
    }

    private void drawAirstrip(Graphics g, GlobalObject object){
        g.setColor(Color.GREEN);
        Coordinates objCoor = object.getCoordinates();
        int x = drawEngine.getFrameIntFromCoor(objCoor.getX()) - 16;
        int y = drawEngine.getFrameIntFromCoor(objCoor.getY()) - 11;

        g.drawRect(x, y, 31, 21);

        x += 2;
        y += 11;
        g.drawLine(x, y, x+6, y);

        x += 10;
        g.drawLine(x, y, x+7, y);

        x += 11;
        g.drawLine(x, y, x+6, y);
    }
}
