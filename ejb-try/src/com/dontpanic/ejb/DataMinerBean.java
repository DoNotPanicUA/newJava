package com.dontpanic.ejb;

import javax.ejb.Remove;
import javax.ejb.Stateless;

@Stateless
public class DataMinerBean implements DataMiner {
    @Override
    public int getCount() {
        return 42;
    }

    @Remove
    public void remove() {

    }
}

