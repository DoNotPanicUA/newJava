package com.donotpanic.airport.domain.aircraft;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.awt.*;

@Entity
@Immutable
@Table(name = "ARP_COLORS")
class AircraftColor extends Color {

    @Id
    @Column(name = "color_id")
    private int color_id;

    @Column(name = "red")
    private int r;
    @Column(name = "green")
    private int g;
    @Column(name = "blue")
    private int b;

    protected AircraftColor(){super(0,0,0);}

    public Color getColor(){
        return new Color(this.r, this.g, this.b);
    }

    protected AircraftColor(int color_id, int r, int g, int b){
        super(r, g, b);
        this.color_id = color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }
}
