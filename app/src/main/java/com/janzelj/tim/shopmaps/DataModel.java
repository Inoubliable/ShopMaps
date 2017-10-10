package com.janzelj.tim.shopmaps;

/**
 * Created by TJ on 10.10.2017.
 */

public class DataModel {

    String name;
    String address;

    public DataModel(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

}
