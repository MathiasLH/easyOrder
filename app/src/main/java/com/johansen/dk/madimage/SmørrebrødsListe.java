package com.johansen.dk.madimage;

import android.app.Activity;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SmørrebrødsListe extends AppCompatActivity implements View.OnClickListener{
    LinearLayout LL;
    int id = 0;
    Order selection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smoerrebroedsliste);
        LL = findViewById(R.id.foodList);
        selection = new Order();
        foodItem dyrlaege = new foodItem("Dyrlægens natmad", R.drawable.dyrlaegensnatmad_big);
        foodItem laks = new foodItem("Laksemad", R.drawable.laks_big);
        foodItem rejemad = new foodItem("Rejemad", R.drawable.rejemad_big);
        foodItem roastbeef = new foodItem("Roastbeef", R.drawable.roastbeef_big);
        foodItem stjerneskud = new foodItem("Stjerneskud", R.drawable.stjerneskud_big);
        createCard(dyrlaege);
        createCard(laks);
        createCard(rejemad);
        createCard(roastbeef);
        createCard(stjerneskud);
    }

    //very ugly function, im sorry.
    private void createCard(foodItem item){
        CardView cv = new CardView(getApplicationContext());
        cv.setTag(item.getName());
        cv.setId(id);
        id++;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, /*LinearLayout.LayoutParams.WRAP_CONTENT*/380);
        cv.setLayoutParams(params);
        cv.setRadius(50);
        ConstraintLayout CL = new ConstraintLayout(this);
        cv.addView(CL);
        ImageView IV = createImageView(item);
        CL.addView(IV);
        TextView TV = new TextView(this);
        TV.setText(item.getName());
        TV.setId(id);
        id++;
        CL.addView(TV);
        ImageButton IB = new ImageButton(this);
        IB.setImageResource(R.drawable.ic_trash);
        IB.setId(id);
        IB.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

            }
        });
        IB.setBackgroundResource(0);
        params = new LinearLayout.LayoutParams(55, 60);
        IB.setLayoutParams(params);
        id++;
        CL.addView(IB);
        ConstraintSet CS = new ConstraintSet();
        CS.clone(CL);
        CS.connect(IV.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        CS.connect(TV.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 16);
        CS.connect(TV.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 8);
        CS.connect(TV.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 8);
        CS.connect(IB.getId(), ConstraintSet.LEFT, TV.getId(), ConstraintSet.RIGHT,300);
        CS.connect(IB.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 8);
        CS.applyTo(CL);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) cv.getLayoutParams();
        layoutParams.setMargins(0,32,0,0);
        cv.setOnClickListener(this);
        LL.addView(cv);
    }

    private ImageView createImageView(foodItem item){
        ImageView IV = new ImageView(this);
        IV.setTag(item.getName() + "Image");
        IV.setImageResource(item.getImageID());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, /*LinearLayout.LayoutParams.WRAP_CONTENT*/300);
        IV.setLayoutParams(params);
        IV.setScaleType(ImageView.ScaleType.CENTER_CROP);
        IV.setId(id);
        id++;
        return IV;
    }

    @Override
    public void onClick(View v) {

    }
}
