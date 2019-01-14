package com.johansen.dk.madimage.model;

import java.io.Serializable;
import java.util.ArrayList;

public class order implements Serializable {


    private String roomNo ="";
    private ArrayList<foodItem> basket = new ArrayList<>();
    //private foodItem[] basket = new foodItem[5];

    public order(){
        /*for(int i = 0; i < basket.length; i++){
            basket[i] = null;
        }*/
    }
    public void addItem(foodItem itemToAdd){
        basket.add(itemToAdd);
    }

    public void removeItem(int itemToDelete){
        basket.remove(itemToDelete);
    }

    public void clean(){
        /*for(int i = 0; i < basket.length; i++){
            basket[i] = null;
        }*/
    }

    public ArrayList<foodItem> getBasket(){
        return basket;
    }

    public String getRoomNo() { return roomNo; }

    public void setRoom(String room){
        roomNo = room;
    }
}
