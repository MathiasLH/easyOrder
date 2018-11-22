package com.johansen.dk.madimage.model;

import java.io.Serializable;

public class foodItem implements Serializable {
    private int imageID;
    private String name;
    private boolean isVeg;
    public foodItem(String name, int imageID){
        this.name = name;
        this.imageID = imageID;
    }
    public int getImageID() {
        return imageID;
    }

    public String getName() {
        return name;
    }

    public boolean isVeg() {
        return isVeg;
    }
}
