package com.johansen.dk.madimage;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.johansen.dk.madimage.LayoutLogic.basketAdapter;
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
        RecyclerView.Adapter niceAdapter = new basketAdapter(fooditems);
        ((basketAdapter) niceAdapter).setOnItemClickListener(new basketAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
               removeItem(position);
               updateList();
            }
        });
        foodList.setAdapter(niceAdapter);
    }

    public void updateList(){
        fooditems = new ArrayList<>();
        for(int j = 0; j < order.getBasket().length; j++){
            if(order.getBasket()[j] != null){
                fooditems.add(order.getBasket()[j]);
            }
        }
        RecyclerView.Adapter niceAdapter = new basketAdapter(fooditems);
        foodList.setAdapter(niceAdapter);
    }

    private void removeItem(int position){
        CardView cv = (CardView) foodList.findViewHolderForAdapterPosition(position).itemView;
        TextView tv = cv.getChildAt(0).findViewById(R.id.cardName);
        String toDelete = tv.getText().toString();
        for(int i = 0; i < order.getBasket().length; i++){
            if(order.getBasket()[i] != null){
                if(order.getBasket()[i].getName() == toDelete){
                    order.getBasket()[i] = null;
                }
            }
        }
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