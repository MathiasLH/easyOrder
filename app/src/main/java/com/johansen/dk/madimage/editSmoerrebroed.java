package com.johansen.dk.madimage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class editSmoerrebroed extends AppCompatActivity implements View.OnClickListener {
    ImageView foodImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_smoerrebroed);
        int imageID = getIntent().getIntExtra("imageID", 0);
        //foodImage = findViewById(edit_foodImage);
        //foodImage.setImageResource(imageID);
    }

    @Override
    public void onClick(View v) {

    }
}
