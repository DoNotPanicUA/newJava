package com.dontpanic.ejb;

import javax.ejb.Remote;

@Remote
public interface DataMiner {
    int getCount();
    void remove();
}
