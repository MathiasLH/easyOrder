package com.johansen.dk.madimage.model;

import android.support.v7.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;

public class foodItem implements Serializable {
    private int imageID;
    private int imageResourceID;
    private String name;
    private ArrayList<String> options;




    public foodItem(String name, int imageResourceID, int imageID, ArrayList<String> opttions){
        this.options = opttions;
        this.name = name;
        this.imageID = imageID;
        this.imageResourceID = imageResourceID;
    }

    public ArrayList<String> getOptions() { return options; }

    public void setOptions(ArrayList<String> options){
        this.options = options;
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

}
