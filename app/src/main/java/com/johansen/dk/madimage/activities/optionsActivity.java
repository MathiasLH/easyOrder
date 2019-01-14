package com.johansen.dk.madimage.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.model.foodItem;

import java.util.ArrayList;

public class optionsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView foodImage;
    TextView foodName;
    static com.johansen.dk.madimage.model.order order;
    com.johansen.dk.madimage.model.foodItem foodItem;
    LinearLayout LL;
    ArrayList<CheckBox> cbArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Orkney Regular.ttf");
        foodItem = (foodItem) getIntent().getSerializableExtra("foodItem");
        foodImage = findViewById(R.id.edit_foodimage);
        foodImage.setImageResource(foodItem.getImageResourceID());
        foodImage.setTransitionName(foodItem.getName()+"Trans");
        order = (com.johansen.dk.madimage.model.order) getIntent().getSerializableExtra("orderObject");
        foodName = findViewById(R.id.dish_name);
        foodName.setTypeface(tf);
        foodName.setText("Rediger " + foodItem.getName());
        Button basketbtn = findViewById(R.id.edit_addtobasketbutton);
        basketbtn.setOnClickListener(this);
        LL = findViewById(R.id.optionsList);
        cbArray = new ArrayList<>();
        createCheckboxes();
    }

    private void createCheckboxes(){
        for(int i = 0; i < foodItem.getOptions().size(); i++){
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText("Med " + foodItem.getOptions().get(i));
            cb.setTag(foodItem.getOptions().get(i));
            cb.setChecked(true);
            cb.setBackgroundResource(R.drawable.checkbox_edit);
            cb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            cb.setHeight(125);
            cbArray.add(cb);
            LL.addView(cb);
        }
    }

    public ArrayList<String> getOptions(){
        ArrayList<String> options = new ArrayList<>();
        for(int i = 0; i < cbArray.size(); i++){
            if(!cbArray.get(i).isChecked()){
                options.add(cbArray.get(i).getTag().toString());
            }
        }
        return options;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_addtobasketbutton:
                foodItem.setOptions(getOptions());
                foodImage.setTransitionName("indkoebTrans");
                order.addItem(foodItem);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("orderObject", order);
                setResult(1, resultIntent);
                boolean animationConfirmation = true;
                resultIntent.putExtra("orderObject", order);
                resultIntent.putExtra("boolean", animationConfirmation);

                // try animation
                //Pair test = Pair.create(foodImage, foodImage.getTransitionName());
                //Pair test2 = Pair.create()
                //ActivityOptions.makeSceneTransitionAnimation(this, foodImage, "indkoebTrans");

                // delete stack
                //finish();
                supportFinishAfterTransition();
        }
    }
}
