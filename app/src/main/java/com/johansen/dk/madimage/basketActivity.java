package com.johansen.dk.madimage;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.johansen.dk.madimage.model.Order;
import com.johansen.dk.madimage.model.foodItem;

public class basketActivity extends AppCompatActivity implements View.OnClickListener{
    LinearLayout LL;
    private int id = 0;
    Order order;
    Button orderBtn;
    TextView basketText;

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
        order = (Order) i.getSerializableExtra("order");
        LL = findViewById(R.id.list);
        processOrder(order);
    }

    private void processOrder(Order order){
        for(int i = 0; i < order.getBasket().length; i++){
            if(order.getBasket()[i] != null){
                createCard(order.getBasket()[i]);
            }
        }
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
                View view = (View) findViewById(v.getId()).getParent().getParent();
                String nameToDelete = view.getTag().toString();
                for(int i = 0; i < order.getBasket().length; i++){
                    if(order.getBasket()[i] != null){
                        if(order.getBasket()[i].getName().equals(nameToDelete)){
                            order.getBasket()[i] = null;
                        }
                    }
                }
                LL.removeView((View) findViewById(v.getId()).getParent().getParent());
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
        switch (v.getId())
        {
            case R.id.orderbtn:
                order.clean();
                Intent intent = new Intent(this,Chekmark1.class);
                startActivity(intent);
                break;
        }
    }
}