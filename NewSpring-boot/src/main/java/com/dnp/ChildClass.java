package com.dnp;

public class ChildClass extends MainClass implements Cloneable {

    private String str;

    ChildClass(String st){
        str = "$$$" + st;
    }

    public void setStr(String newStr){
        str = newStr;
    }

    @Override
    protected ChildClass clone() throws CloneNotSupportedException {
        return (ChildClass) super.clone();
    }

    @Override
    public String toString(){
        return super.toString() + str;
    }
}
