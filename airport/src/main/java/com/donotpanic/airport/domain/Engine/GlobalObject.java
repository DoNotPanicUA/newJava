package com.donotpanic.airport.domain.Engine;

import com.donotpanic.airport.domain.location.CoordinateObject;

import java.awt.*;

public interface GlobalObject extends CoordinateObject {
    ObjectType getObjectType();
    Color getColor();
}
