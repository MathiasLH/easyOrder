package com.johansen.dk.madimage.model;

import java.io.Serializable;

public class order implements Serializable {


    private String roomNo ="";
    private foodItem[] basket = new foodItem[5];

    public order(){
        for(int i = 0; i < basket.length; i++){
            basket[i] = null;
        }
    }
    public void addItem(foodItem itemToAdd){
        for(int i = 0; i < basket.length; i++){
            if(basket[i] == null){
                basket[i] = itemToAdd;
                break;
            }
        }
    }

    public void clean(){
        /*for(int i = 0; i < basket.length; i++){
            basket[i] = null;
        }*/
    }

    public foodItem[] getBasket(){
        return basket;
    }

    public String getRoomNo() { return roomNo; }

    public void setRoom(String room){
        roomNo = room;
    }
}
