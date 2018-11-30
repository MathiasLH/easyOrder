package com.johansen.dk.madimage;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.johansen.dk.madimage.model.Order;
import com.johansen.dk.madimage.model.foodItem;

public class editSmoerrebroed extends AppCompatActivity implements View.OnClickListener {
    ImageView foodImage;
    TextView foodName;
    static Order order;
    com.johansen.dk.madimage.model.foodItem foodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_smoerrebroed);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Orkney Regular.ttf");
        foodItem = (foodItem) getIntent().getSerializableExtra("foodItem");
        foodImage = findViewById(R.id.edit_foodimage);
        foodImage.setImageResource(foodItem.getImageID());
        order = (Order) getIntent().getSerializableExtra("orderObject");
        foodName = findViewById(R.id.dish_name);
        foodName.setTypeface(tf);
        foodName.setText("Rediger " + foodItem.getName());
        Button basketbtn = findViewById(R.id.edit_addtobasketbutton);
        basketbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_addtobasketbutton:
                order.addItem(foodItem);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("orderObject", order);
                setResult(1, resultIntent);
                finish();
        }
    }
}
