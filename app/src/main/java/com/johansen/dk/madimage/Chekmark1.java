package com.johansen.dk.madimage;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;


public class Chekmark1 extends AppCompatActivity {
    TextView sText;
    TextView orderList;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chekmark1);
        (new Handler()).postDelayed(this::switchactivity, 3000);
        sText = findViewById(R.id.sucText);
        orderList = findViewById(R.id.orderListView);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Orkney Regular.ttf");
        sText.setTypeface(tf);
        orderList.setTypeface(tf);
        //if statment for language
        //if(language == danish)
        // {sText.setText("danish Text");}
        //if(language == arabic)
        // {sText.setText("arabic Text");}
        //else{sText.setText("english Text");}

        sText.setText("Thanks For your Order \n");
        orderList.setText("Your Order: \n" +
                "-  OrderItemOne \n" +
                "-  OrderItemTwo \n");

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.done);
        mediaPlayer.start();
    }

    public void switchactivity()
    {
        startActivity(new Intent(Chekmark1.this, LoginActivity.class));
    }
}
