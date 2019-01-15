package com.johansen.dk.madimage.model;

import android.support.v7.widget.LinearLayoutManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class foodItem implements Serializable {
    private int imageID;
    private int imageResourceID;
    private String name;
    private String optionNames[];
    private boolean optionValues[];
    //private ArrayList<option> options;
    private boolean darkBread = true;

    public foodItem(String name, int imageResourceID, int imageID, String[] options){
        this.optionNames = options;
        this.optionValues = new boolean[optionNames.length];
        for(int i = 0; i < optionValues.length; i++){
            optionValues[i] = true;
        }
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

    public String[] getOptionNames() {
        return optionNames;
    }

    public void setOptionNames(String[] optionNames) {
        this.optionNames = optionNames;
    }

    public boolean[] getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(boolean[] optionValues) {
        this.optionValues = optionValues;
    }

    public boolean isDarkBread() { return darkBread;  }

    public void setDarkBread(boolean darkBread) { this.darkBread = darkBread;  }

    public String toString() {
        String out = name + ",";
        if(darkBread){
            out += "Med mørkt brød" + ",";
        } else {
            out += "Med lyst brød" + ",";
        }
        for(int i = 0; i<getOptionNames().length; i++){
            if (!getOptionValues()[i]) {
                out+= "Uden " + getOptionNames()[i] + ",\n";
            }
        }
        return out;
    }

}




