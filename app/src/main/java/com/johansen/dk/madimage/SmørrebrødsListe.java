package com.johansen.dk.madimage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.johansen.dk.madimage.LayoutLogic.selectionAdapter;
import com.johansen.dk.madimage.model.Order;
import com.johansen.dk.madimage.model.foodItem;

import java.util.ArrayList;

public class SmørrebrødsListe extends AppCompatActivity implements View.OnClickListener{
    int id = 0;
    Order selection;
    foodItem dyrlaege, laks, rejemad, roastbeef, stjerneskud;
    TextView text;
    ArrayList<foodItem> foodItems;
    RecyclerView foodList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smoerrebroedsliste);
        dyrlaege = new foodItem("Dyrlægens natmad", R.drawable.dyrlaegensnatmad_big, 100);
        laks = new foodItem("Laksemad", R.drawable.laks_big, 101);
        rejemad = new foodItem("Rejemad", R.drawable.rejemad_big, 102);
        roastbeef = new foodItem("Roastbeef", R.drawable.roastbeef_big, 103);
        stjerneskud = new foodItem("Stjerneskud", R.drawable.stjerneskud_big, 104);
        foodItems = new ArrayList<>();
        foodItems.add(dyrlaege);
        foodItems.add(laks);
        foodItems.add(rejemad);
        foodItems.add(roastbeef);
        foodItems.add(stjerneskud);
        foodList = findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        foodList.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter niceAdapter = new selectionAdapter(foodItems);
        ((selectionAdapter) niceAdapter).setOnItemClickListener(new selectionAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                launchEditActivity(position);
            }
        });
        foodList.setAdapter(niceAdapter);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Orkney Regular.ttf");
        text = findViewById(R.id.texttop);
        text.setTypeface(tf);
        ImageButton basketBtn = findViewById(R.id.basketbtn);
        basketBtn.setOnClickListener(this);
        selection = new Order();
        int i = 0;
    }

    private void launchEditActivity(int position){
        Intent editIntent = new Intent(this, editSmoerrebroed.class);
        editIntent.putExtra("orderObject", selection);
        editIntent.putExtra("foodItem", foodItems.get(position));
        CardView cv = (CardView) foodList.findViewHolderForAdapterPosition(position).itemView;
        ImageView iv = cv.getChildAt(0).findViewById(foodItems.get(position).getImageID());
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SmørrebrødsListe.this, iv, ViewCompat.getTransitionName(iv));
        startActivityForResult(editIntent,1, options.toBundle());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){
            if(requestCode == 1){
                selection = (Order) data.getSerializableExtra("orderObject");
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.basketbtn:
                Intent basketIntent = new Intent(this, basketActivity.class);
                basketIntent.putExtra("orderObject", selection);
                startActivity(basketIntent);

        }
    }
}
