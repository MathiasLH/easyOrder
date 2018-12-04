package com.johansen.dk.madimage.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.johansen.dk.madimage.R;

public class loadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        (new Handler()).postDelayed(this::switchactivity, 6000);
    }
    public void switchactivity()
    {
        startActivity(new Intent(loadingActivity.this, loginActivity.class));
    }

}
