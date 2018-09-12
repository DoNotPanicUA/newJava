package com.donotpanic.airport.domain.Engine;

import com.donotpanic.airport.domain.common.Actions;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
@Service
final class DrawEngine extends Thread{
    private JFrame mainFrame;
    private int frameWidth = 4000;
    private int frameHeight = 2100;
    private double coordinateCoef;
    private JPanel radarComponent;
    private DrawTemplates drawTemplates;
    private ArrayList<GlobalObject> allObjects = new ArrayList<>();
    private final String ARRAY_OBJ_LOCK = "ARRAY_OBJ_LOCK";

    @Bean(name = "DrawEngine")
    DrawEngine getDrawEngine(){
        return new DrawEngine();
    }

    public void run() {
        while (true){
            mainFrame.repaint();

            Actions.DRAW_FPS.doAction();
        }
    }

    void addGlobalObject(GlobalObject object){
        synchronized (ARRAY_OBJ_LOCK){
            for (GlobalObject globalObject : allObjects){
                if (globalObject.equals(object)){
                    return; /*Already exists*/
                }
            }
            allObjects. add(object);
            ARRAY_OBJ_LOCK.notify();
        }

    }

    DrawEngine(){
        drawTemplates = new DrawTemplates().setDrawEngine(this);
    }

    private static class radarDrawArea extends JPanel{
        private DrawEngine drawEngine;

        private radarDrawArea(DrawEngine drawEngine){
            this.drawEngine = drawEngine;
        }

        @Override
        public void paint(Graphics g) {

            g.clearRect(0, 0, drawEngine.getFrameWidth(), drawEngine.getFrameHeight());
            g.setColor(Color.GRAY);
            g.fillRect(0,0,drawEngine.getFrameWidth(), drawEngine.getFrameHeight());

            synchronized (drawEngine.ARRAY_OBJ_LOCK) {
                for (GlobalObject drawObject : drawEngine.allObjects) {
                    drawEngine.drawTemplates.drawObj(g, drawObject);
                }
                drawEngine.ARRAY_OBJ_LOCK.notify();
            }
        }
    }

    void iniFrame(String title, double maxSideCoor, double maxTopCoor){
        calculateCoef(maxSideCoor, maxTopCoor);
        frameWidth = getFrameIntFromCoor(maxSideCoor);
        frameHeight = getFrameIntFromCoor(maxTopCoor);
        mainFrame = new JFrame(title);
        mainFrame.setSize(frameWidth, frameHeight);
        mainFrame.setLocation(0, 0);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        radarComponent = new radarDrawArea(this);
        mainFrame.add(radarComponent);
        radarComponent.repaint();
        mainFrame.setVisible(true);
        this.start();
    }


    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    private void calculateCoef(double maxSideCoor, double maxTopCoor){
        coordinateCoef = Math.round(100*frameWidth/maxSideCoor)/100;
        double verticalCoef = Math.round(100*frameHeight/maxTopCoor)/100;

        if (coordinateCoef > verticalCoef){
            coordinateCoef = verticalCoef;
        }
        System.out.println("Draw coef: " + coordinateCoef);
    }

    int getFrameIntFromCoor(double value){
       return (int)Math.round(value*coordinateCoef);
    }
}
