package com.johansen.dk.madimage.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.model.foodItem;
import java.util.ArrayList;
import java.util.Arrays;

public class basketAdapter extends RecyclerView.Adapter<basketAdapter.myViewHolder> {
    private static ClickListener clickListener;
    private ArrayList<foodItem> dataset;
    //private ArrayList<LinearLayoutManager> LLM;
    private Context context;
    Typeface tf2;




    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView niceCard;
        public Button TTSBtn;
        public ImageButton TRASHBtn;
        private TextView foodName, breadType;
        private ImageView foodImage;
        public myViewHolder(@NonNull CardView cv){
            super(cv);
            this.TTSBtn = (Button) cv.findViewById(R.id.basket_TTSBtn);
            TTSBtn.setOnClickListener(this);
            TTSBtn.setTag("TTS");
            this.TRASHBtn = (ImageButton) cv.findViewById(R.id.trashButton);
            TRASHBtn.setOnClickListener(this);
            TRASHBtn.setTag("TRASH");
            foodName = cv.findViewById(R.id.cardName);
            foodName.setOnClickListener(this);
            foodName.setTag("OTHER");
            foodImage = cv.findViewById(R.id.cardImage);
            foodImage.setOnClickListener(this);
            foodImage.setTag("OTHER");
            breadType = cv.findViewById(R.id.breadType);

            niceCard = cv;

            //RecyclerView rv = cv.findViewById(R.id.optionsList);
        }

        //@Override
        public void onClick(View v) {clickListener.onItemClick(getAdapterPosition(), v); }
    }

    public basketAdapter(ArrayList<foodItem> myDataset, Context context , Typeface tf){
        //this.LLM = LLM;
        this.context = context;
        dataset = myDataset;
        tf2=tf;
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
        TextView tv2 = holder.niceCard.findViewById(R.id.breadType);
        tv.setText(dataset.get(position).getName());
        tv.setTypeface(tf2);
        if(dataset.get(position).isDarkBread()){
            tv2.setText("Med mørkt brød");
        } else {
            tv2.setText("Med lyst brød");
        }
        tv2.setTypeface(tf2);
        tv2.setTextSize(18);

        ImageView iv = holder.niceCard.findViewById(R.id.cardImage);
        iv.setImageResource(dataset.get(position).getImageResourceID());
        optionsAdapter niceAdapter = new optionsAdapter(getOptionsToDraw(position),tf2, dataset.get(position).isDarkBread());
        RecyclerView rv = holder.niceCard.findViewById(R.id.optionsList);
        rv.setAdapter(niceAdapter);
        rv.setHasFixedSize(true);
        //LinearLayoutManager mLayoutManager = LLM.get(position);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv.setLayoutManager(mLayoutManager);
    }

    private ArrayList<String> getOptionsToDraw(int position){
        ArrayList<String> nameArray = new ArrayList<>();
        for(int i = 0 ; i < dataset.get(position).getOptionValues().length; i++){
            if(!dataset.get(position).getOptionValues()[i]){
                nameArray.add(dataset.get(position).getOptionNames()[i]);
            }
        }
        return nameArray;
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