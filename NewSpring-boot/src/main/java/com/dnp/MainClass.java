package com.dnp;

public class MainClass implements Cloneable {

    @Override
    protected MainClass clone() throws CloneNotSupportedException {
        return (MainClass) super.clone();
    }
}
