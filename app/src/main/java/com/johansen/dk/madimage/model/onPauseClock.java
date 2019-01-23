package com.johansen.dk.madimage.model;

public class onPauseClock {

    private long timeLeft=0, timeReturned=0;

    private static final onPauseClock ourInstance = new onPauseClock();

    public static onPauseClock getInstance() {
        return ourInstance;
    }

    private onPauseClock() {
    }

    public void setTimeLeft(long timeLeft){
        this.timeLeft=timeLeft;
    }

    //setting app to reset after 10min of inactivity.
    public boolean isReset(long timeReturned){
        this.timeReturned=timeReturned;
        long difference =this.timeReturned-this.timeLeft;

        if (difference>600000){
            return true;
        }
        return false;

    }
}
