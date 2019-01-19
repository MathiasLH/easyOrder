package com.johansen.dk.madimage.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.model.foodItem;

import java.util.ArrayList;

public class optionsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView foodImage;
    TextView foodName;
    com.johansen.dk.madimage.model.foodItem foodItem;
    LinearLayout LL;
    ArrayList<CheckBox> cbArray;
    RadioGroup breadbuttons;
    RadioButton cbdark, cblight;
    Vibrator vibe;
    Button basketbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");
        foodItem = (foodItem) getIntent().getSerializableExtra("foodItem");
        foodImage = findViewById(R.id.edit_foodimage);
        foodImage.setImageResource(foodItem.getImageResourceID());
        foodImage.setTransitionName(foodItem.getName() + "Trans");
        foodName = findViewById(R.id.dish_name);
        foodName.setTypeface(tf);
        foodName.setText(foodItem.getName());
        basketbtn = (Button) findViewById(R.id.edit_addtobasketbutton);
        basketbtn.setOnClickListener(this);
        LL = findViewById(R.id.optionsList);
        cbArray = new ArrayList<>();
        createCheckboxes();
        breadbuttons = findViewById(R.id.radioGroup2);
        cbdark = breadbuttons.findViewById(R.id.cbdark);
        cbdark.setChecked(foodItem.isDarkBread());
        cblight = breadbuttons.findViewById(R.id.cblight);
        cblight.setChecked(!foodItem.isDarkBread());
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    private void createCheckboxes() {
        for (int i = 0; i < foodItem.getOptionNames().length; i++) {
            CheckBox cb = new CheckBox(getApplicationContext());
            cb.setTextSize(24);
            cb.setText("Med " + foodItem.getOptionNames()[i]);
            cb.setTag(foodItem.getOptionNames()[i]);
            cb.setChecked(foodItem.getOptionValues()[i]);
            cb.setBackgroundResource(R.drawable.checkbox_edit);
            cb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            cb.setHeight(170);
            cbArray.add(cb);
            LL.addView(cb);
        }
    }

    public boolean[] getOptions() {
        boolean optionValues[] = new boolean[foodItem.getOptionNames().length];
        for (int i = 0; i < cbArray.size(); i++) {
            optionValues[i] = cbArray.get(i).isChecked();
        }
        return optionValues;
    }

    @Override
    public void onClick(View v) {
        vibe.vibrate(100);
        switch (v.getId()) {
            case R.id.edit_addtobasketbutton:
                foodItem.setOptionValues(getOptions());
                foodImage.setTransitionName("indkoebTrans");
                foodItem.setDarkBread(cbdark.isChecked());
                Intent resultIntent = new Intent();
                setResult(1, resultIntent);
                boolean animationConfirmation = true;
                resultIntent.putExtra("foodItem", foodItem);
                resultIntent.putExtra("boolean", animationConfirmation);
                // try animation
                //Pair test = Pair.create(foodImage, foodImage.getTransitionName());
                //Pair test2 = Pair.create()
                //ActivityOptions.makeSceneTransitionAnimation(this, foodImage, "indkoebTrans");
                supportFinishAfterTransition();
        }
    }
}
