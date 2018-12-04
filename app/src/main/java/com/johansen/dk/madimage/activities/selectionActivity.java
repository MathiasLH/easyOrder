package com.johansen.dk.madimage.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.adapter.selectionAdapter;
import com.johansen.dk.madimage.model.order;
import com.johansen.dk.madimage.model.foodItem;

import java.util.ArrayList;

public class selectionActivity extends AppCompatActivity implements View.OnClickListener{
    int id = 0;
    order selection;
    foodItem dyrlaege, laks, rejemad, roastbeef, stjerneskud;
    TextView text;
    ArrayList<foodItem> foodItems;
    RecyclerView foodList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
       createTestData();
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
        selection = new order();
        Intent niceIntent = getIntent();
        selection.setRoom(niceIntent.getStringExtra("roomNo"));
    }

    private void createTestData(){
        ArrayList<String> dyrlaegeOptions = new ArrayList<>();
        dyrlaegeOptions.add("sky");
        dyrlaegeOptions.add("rødløg");
        dyrlaegeOptions.add("karse");
        dyrlaege = new foodItem("Dyrlægens natmad", R.drawable.dyrlaegensnatmad_big, 100, dyrlaegeOptions);
        ArrayList<String> laksOptions = new ArrayList<>();
        laksOptions.add("dild");
        laksOptions.add("citron");
        laksOptions.add("asperges");
        laks = new foodItem("Laksemad", R.drawable.laks_big, 101, laksOptions);
        ArrayList<String> rejeOptions = new ArrayList<>();
        rejeOptions.add("krydderurt");
        rejemad = new foodItem("Rejemad", R.drawable.rejemad_big, 102, rejeOptions);
        ArrayList<String> roastbeefOptions = new ArrayList<>();
        roastbeefOptions.add("peberrod");
        roastbeefOptions.add("salat");
        roastbeefOptions.add("Syltet agurk");
        roastbeef = new foodItem("Roastbeef", R.drawable.roastbeef_big, 103, roastbeefOptions);
        ArrayList<String> stjerneskudOptions = new ArrayList<>();
        stjerneskudOptions.add("fiskeægting");
        stjerneskudOptions.add("citron");
        stjerneskudOptions.add("hvid asparges");
        stjerneskudOptions.add("grøn asparges");
        stjerneskudOptions.add("rejer");
        stjerneskud = new foodItem("Stjerneskud", R.drawable.stjerneskud_big, 10, stjerneskudOptions);
        foodItems = new ArrayList<>();
        foodItems.add(dyrlaege);
        foodItems.add(laks);
        foodItems.add(rejemad);
        foodItems.add(roastbeef);
        foodItems.add(stjerneskud);
    }

    private void launchEditActivity(int position){
        Intent editIntent = new Intent(this, optionsActivity.class);
        editIntent.putExtra("orderObject", selection);
        editIntent.putExtra("foodItem", foodItems.get(position));
        CardView cv = (CardView) foodList.findViewHolderForAdapterPosition(position).itemView;
        ImageView iv = cv.getChildAt(0).findViewById(foodItems.get(position).getImageID());
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(selectionActivity.this, iv, ViewCompat.getTransitionName(iv));
        startActivityForResult(editIntent,1, options.toBundle());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){
            if(requestCode == 1){
                selection = (order) data.getSerializableExtra("orderObject");
            }
        }
        if(resultCode == RESULT_OK){
            selection = (order) data.getSerializableExtra("orderObject");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.basketbtn:
                Intent basketIntent = new Intent(this, basketActivity.class);
                basketIntent.putExtra("orderObject", selection);
                startActivityForResult(basketIntent, 2);

        }
    }
}
