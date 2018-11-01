package com.johansen.dk.madimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class editSmoerrebroed extends AppCompatActivity implements View.OnClickListener {
    ImageView foodImage;
    Order order;
    foodItem foodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_smoerrebroed);
        foodItem = (foodItem) getIntent().getSerializableExtra("foodItem");
        foodImage = findViewById(R.id.edit_foodimage);
        foodImage.setImageResource(foodItem.getImageID());
        order = (Order) getIntent().getSerializableExtra("order");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_addtobasketbutton:
                order.addItem(foodItem);
        }
    }
}
