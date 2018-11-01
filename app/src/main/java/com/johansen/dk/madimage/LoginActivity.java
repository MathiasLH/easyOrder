package com.johansen.dk.madimage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton danishFlag;
    ImageButton englishFlag;
    ImageButton arabFlag;
    ImageButton qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        danishFlag = findViewById(R.id.danishFlag);
        englishFlag = findViewById(R.id.englishFlag);
        arabFlag = findViewById(R.id.arabFlag);
        qrCode = findViewById(R.id.qrCode);

        danishFlag.setOnClickListener(this);
        englishFlag.setOnClickListener(this);
        arabFlag.setOnClickListener(this);
        qrCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == danishFlag) {

        }
        if (v == englishFlag) {

        }
        if (v == arabFlag) {

        }
        if (v == qrCode) {

        }
    }
}
