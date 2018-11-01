package com.johansen.dk.madimage;

import java.io.Serializable;

public class Order implements Serializable {
    static String roomNo ="";
    private static foodItem[] basket = new foodItem[5];

    public Order(){
        /*for(int i = 0; i < basket.length; i++){
            basket[i] = null;
        }*/
    }
    public static void addItem(foodItem itemToAdd){
        for(int i = 0; i < basket.length; i++){
            if(basket[i] == null){
                basket[i] = itemToAdd;
                break;
            }
        }
    }

    public static foodItem[] getBasket(){
        return basket;
    }

    public static void setRoom(String room){
        roomNo = room;
    }
}
