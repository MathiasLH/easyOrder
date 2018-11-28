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

        sText.setText("Tak for din bestilling! \n");
        /*orderList.setText("Your Order: \n" +
                "-  OrderItemOne \n" +
                "-  OrderItemTwo \n");*/

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.done);
        mediaPlayer.start();
    }

    //from stackoverflow: https://stackoverflow.com/questions/14001963/finish-all-activities-at-a-time
    public void switchactivity()
    {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        // for info about clear task: https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_CLEAR_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //for info about new task : https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // old code
        // startActivity(new Intent(Chekmark1.this, LoginActivity.class));
    }
}
