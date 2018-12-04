package com.johansen.dk.madimage.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.model.foodItem;
import java.util.ArrayList;

public class basketAdapter extends RecyclerView.Adapter<basketAdapter.myViewHolder> {
    private static ClickListener clickListener;
    private ArrayList<foodItem> dataset;
    private ArrayList<LinearLayoutManager> LLM;

    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView niceCard;
        public myViewHolder(@NonNull CardView cv){
            super(cv);
            niceCard = cv;
            ImageButton ib = niceCard.findViewById(R.id.trashButton);
            //RecyclerView rv = cv.findViewById(R.id.optionsList);
            ib.setOnClickListener(this);
        }

        //@Override
        public void onClick(View v) {clickListener.onItemClick(getAdapterPosition(), v); }
    }

    public basketAdapter(ArrayList<foodItem> myDataset, ArrayList<LinearLayoutManager> LLM){
        this.LLM = LLM;
        dataset = myDataset;

    }

    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.basketcard, parent, false);
        cv.setRadius((float) 50.0);
        RecyclerView rv = cv.findViewById(R.id.optionsList);
        myViewHolder vh = new myViewHolder(cv);

        return vh;
    }

    public void onBindViewHolder(myViewHolder holder, int position){
        TextView tv = holder.niceCard.findViewById(R.id.cardName);
        tv.setText(dataset.get(position).getName());
        ImageView iv = holder.niceCard.findViewById(R.id.cardImage);
        iv.setImageResource(dataset.get(position).getImageResourceID());
        optionsAdapter niceAdapter = new optionsAdapter(dataset.get(position).getOptions());
        RecyclerView rv = holder.niceCard.findViewById(R.id.optionsList);
        rv.setAdapter(niceAdapter);
        rv.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = LLM.get(position);
        rv.setLayoutManager(mLayoutManager);
    }

    public void removeItemAt(int position){
        dataset.remove(position);
        notifyItemRemoved(position);
    }

    public int getItemCount(){return dataset.size();}

    public void setOnItemClickListener(basketAdapter.ClickListener clickListener) {
        basketAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}