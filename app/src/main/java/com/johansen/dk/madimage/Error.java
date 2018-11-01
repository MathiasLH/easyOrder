package com.johansen.dk.madimage;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Error extends AppCompatActivity {

    TextView sText;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        (new Handler()).postDelayed(this::switchactivity, 6000);
        sText = findViewById(R.id.sorryText);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/fontawesome-webfont.ttf");
        sText.setTypeface(tf);
        //if statment for language
        //if(language == danish)
        // {sText.setText("danish Text");}3
        //if(language == arabic)
        // {sText.setText("arabic Text");}
        //else{sText.setText("english Text");}
        sText.setText("Sorry, \n" + "" +
                "but your order couldn't be handled at the moment," +
                " pleas try again later.");
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.error);

        mediaPlayer.start();
    }
    public void switchactivity()
    {
        startActivity(new Intent(Error.this, MainActivity.class));
    }
}
