package com.johansen.dk.madimage.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.model.order;

public class recieptActivity extends AppCompatActivity {
    private TextView sText;
    private TextView orderList;
    private MediaPlayer mediaPlayer;
    private order items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isTablet()) {
            setContentView(R.layout.activity_reciept_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setContentView(R.layout.activity_reciept);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        // Don't know what is does but it fixes the problem (do not uncomment the following line) WHAT COULD GO WRONG?
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reciept);
        (new Handler()).postDelayed(this::switchactivity, 3000);
        sText = findViewById(R.id.sucText);
        orderList = findViewById(R.id.orderListView);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");
        sText.setTypeface(tf);
        orderList.setTypeface(tf);
        Intent i = getIntent();
        items = (order) i.getSerializableExtra("order");
        orderList.setText(getResources().getString(R.string.reciept_order_text) +"\n");
        for (int a = 0; a < items.getBasket().size(); a++) {
            orderList.append(items.getBasket().get(a).getName() + "\n");
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.done);
        mediaPlayer.start();
    }

    private boolean isTablet() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;

        return Math.sqrt(xInches * xInches + yInches * yInches) >= 6.5;
    }

    //from stackoverflow: https://stackoverflow.com/questions/14001963/finish-all-activities-at-a-time
    public void switchactivity() {
        mediaPlayer.stop();
        Intent intent = new Intent(getApplicationContext(), loginActivity.class);
        // for info about clear task: https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_CLEAR_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //for info about new task : https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_NEW_TASK
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
