package com.johansen.dk.madimage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /*initialize ImageButtons */
    ImageButton danishFlag;
    ImageButton englishFlag;
    ImageButton arabFlag;
    ImageButton qrCode;
    TextView top, help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*define font*/
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Orkney Regular.ttf");
        /*defining textViews*/
        top = findViewById(R.id.explqr);
        help = findViewById(R.id.help);
        /*using fonts on text fields*/
        top.setTypeface(tf);
        help.setTypeface(tf);

        /*defining image Buttons*/
        danishFlag = findViewById(R.id.danishFlag);
        englishFlag = findViewById(R.id.englishFlag);
        arabFlag = findViewById(R.id.arabFlag);
        qrCode = findViewById(R.id.qrCode);

        /*OnClick listener on image Buttons*/
        danishFlag.setOnClickListener(this);
        englishFlag.setOnClickListener(this);
        arabFlag.setOnClickListener(this);
        qrCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*Implementing on click listener to QR-code image Button*/
        switch (v.getId()){
            case R.id.qrCode:
                Intent listIntent = new Intent(this, SmørrebrødsListe.class);
                startActivity(listIntent);
                break;
        }

        if (v == danishFlag) {
            /*setting language to danish maybe default case*/
        }
        if (v == englishFlag) {
            /*setting language to english*/
        }
        if (v == arabFlag) {
            /*setting language to arbaic*/
        }
        if (v == qrCode) {
            /*if QR-Codes reader: io - stream on:*/
        }
    }
}
