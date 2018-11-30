package com.johansen.dk.madimage;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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

public class basketActivity extends AppCompatActivity implements View.OnClickListener{
    Order order;
    Button orderBtn;
    TextView basketText;
    RecyclerView foodList;
    ArrayList<foodItem> fooditems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        orderBtn = findViewById(R.id.orderbtn);
        orderBtn.setOnClickListener(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");
        orderBtn.setTypeface(tf);
        basketText = findViewById(R.id.toptekst);
        basketText.setTypeface(tf);
        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("orderObject");
        foodList = findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        foodList.setLayoutManager(mLayoutManager);
        fooditems = new ArrayList<>();
        for(int j = 0; j < order.getBasket().length; j++){
            if(order.getBasket()[j] != null){
                fooditems.add(order.getBasket()[j]);
            }

        }
        RecyclerView.Adapter niceAdapter = new selectionAdapter(fooditems);
        foodList.setAdapter(niceAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.orderbtn:

                Intent intent = new Intent(this,Chekmark1.class);
                intent.putExtra("order", order);

                startActivity(intent);
                order.clean();
                break;
        }
    }
}