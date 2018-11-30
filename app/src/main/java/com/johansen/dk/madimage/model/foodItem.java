package com.johansen.dk.madimage.model;

import java.io.Serializable;

public class foodItem implements Serializable {
    private int imageID;
    private int imageResourceID;
    private String name;
    private boolean isVeg;
    public foodItem(String name, int imageResourceID, int imageID){
        this.name = name;
        this.imageID = imageID;
        this.imageResourceID = imageResourceID;
    }
    public int getImageID() {
        return imageID;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public String getName() {
        return name;
    }

    public boolean isVeg() {
        return isVeg;
    }
}
