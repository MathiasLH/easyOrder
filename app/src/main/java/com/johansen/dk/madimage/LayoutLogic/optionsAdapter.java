package com.johansen.dk.madimage.LayoutLogic;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.johansen.dk.madimage.R;


import java.util.ArrayList;

public class optionsAdapter extends RecyclerView.Adapter<optionsAdapter.myViewHolder> {
    private ArrayList<String> dataset;
    public static class myViewHolder extends RecyclerView.ViewHolder{
        public CardView niceCard;
        public myViewHolder(CardView cv){
            super(cv);
            niceCard = cv;
        }
    }
    public optionsAdapter(ArrayList<String> myDataSet){dataset =myDataSet;}

    public myViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        CardView cv =(CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.basketoption, parent, false);
        myViewHolder vh = new myViewHolder(cv);
        return vh;
    }

    public void onBindViewHolder(myViewHolder holder, int position){
        TextView tv = holder.niceCard.findViewById(R.id.optionText);
        tv.setText("Uden " + dataset.get(position));

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
