package com.donotpanic.airport.domain.aircraft;

import javax.persistence.*;
import java.awt.*;

@Entity
@Table(name = "ARP_AIRCRAFT_MODELS")
public class AircraftModel{

    @Column(name = "fly_speed")
    private double aircraftFlySpeed;

    @Column(name = "ground_speed")
    private double aircraftGroundSpeed;

    @Column(name = "model_version")
    private double modelVersion;

    @Id
    @Column(name = "model_name", length = 256)
    private final String modelName;

//    @Column(name = "color_id")
    @OneToOne
    @JoinColumn(name = "color_id")
    private AircraftColor planeColor;

    @Transient
    private Color realColor;

    protected AircraftModel(){
        this("New Model", 0.01, 0.01);
    }

    AircraftModel(String modelName, double aircraftFlySpeed, double aircraftGroundSpeed){
        modelVersion = 1.01d;
        this.aircraftFlySpeed = aircraftFlySpeed;
        this.aircraftGroundSpeed = aircraftGroundSpeed;
        this.modelName = modelName;
    }

    Color getColor(){
        if (realColor == null){
            this.realColor = planeColor.getColor();
        }
        return realColor;
    }

    String getModelName() {
        return modelName;
    }

    void setPlaneColor(AircraftColor color){
        this.planeColor = color;
    }

    double getModelVersion() {
        return modelVersion;
    }

    private void increaseModelVersion(){
        modelVersion += 0.01d;
    }

    double getAircraftFlySpeed() {
        return aircraftFlySpeed;
    }

    void setAircraftFlySpeed(double aircraftFlySpeed) {
        if (aircraftFlySpeed > 0 & this.aircraftFlySpeed != aircraftFlySpeed){
            increaseModelVersion();
            this.aircraftFlySpeed = aircraftFlySpeed;
        }
    }

    double getAircraftGroundSpeed() {
        return aircraftGroundSpeed;
    }

    void setAircraftGroundSpeed(double aircraftGroundSpeed) {
        if (aircraftGroundSpeed > 0 & this.aircraftGroundSpeed != aircraftGroundSpeed){
            increaseModelVersion();
            this.aircraftGroundSpeed = aircraftGroundSpeed;
        }
    }

    @Override
    public String toString() {
        return this.modelName + ": ver." + this.modelVersion + "; GrSpeed(" + this.aircraftGroundSpeed +
                "); FlySpeed(" + this.aircraftFlySpeed + "); Color(" + this.getColor().toString()+ ")";
    }
}
