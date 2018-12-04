package com.johansen.dk.madimage.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.johansen.dk.madimage.R;

import java.io.IOException;
import java.util.regex.Pattern;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    /*initialize ImageButtons */
    ImageButton danishFlag, englishFlag, arabFlag;
    SurfaceView cameraPreview;
    CameraSource cameraSrc;
    BarcodeDetector barcodeDetector;
    TextView top;
    Button helpBtn, moveAlongBtn;
    /*me love u*/long time = 0;

    int tempHeight,tempWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*define font*/
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");
        /*defining textViews*/
        top = findViewById(R.id.explqr);
        /*defining buttons*/
        helpBtn = findViewById(R.id.helpBtn);
        moveAlongBtn = findViewById(R.id.moveAlongBtn);
        /*using fonts on text fields*/
        top.setTypeface(tf);
        helpBtn.setTypeface(tf);

        /*defining elements for QR-scanner*/
        //from youtubevideo: https://www.youtube.com/watch?v=ej51mAYXbKs
        cameraPreview = findViewById(R.id.cameraPreview);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSrc = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(800, 500).build();
        tempHeight = cameraPreview.getHeight();
        tempWidth = cameraPreview.getWidth();

        /*defining image Buttons*/
        danishFlag = findViewById(R.id.danishFlag);
        englishFlag = findViewById(R.id.englishFlag);
        arabFlag = findViewById(R.id.arabFlag);

        /*OnClick listener on Buttons*/
        moveAlongBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        danishFlag.setOnClickListener(this);
        englishFlag.setOnClickListener(this);
        arabFlag.setOnClickListener(this);

        createQRscan();

    }

    @Override
    public void onClick(View v) {
        /*Implementing on click listener to QR-code image Button*/
        switch (v.getId()){
            case R.id.moveAlongBtn:
                startActivity(new Intent(loginActivity.this, selectionActivity.class));
                break;
            case R.id.helpBtn:
                Toast.makeText(this, "NOT IMPLEMENTED", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(loginActivity.this, LoginHelp.class));
                break;
            case R.id.danishFlag:
                Toast.makeText(this, "NOT IMPLEMENTED", Toast.LENGTH_SHORT).show();
                break;
            case R.id.englishFlag:
                Toast.makeText(this, "NOT IMPLEMENTED", Toast.LENGTH_SHORT).show();
                break;
            case R.id.arabFlag:
                Toast.makeText(this, "NOT IMPLEMENTED", Toast.LENGTH_SHORT).show();
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
    }

    private void validateQR(String input){
        String regex = "([a-zA-Z]+[0-9]+)";
        if(input.matches(regex)){
            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(100);
            Intent niceIntent = new Intent(loginActivity.this, selectionActivity.class);
            niceIntent.putExtra("roomNo", input);
            startActivity(niceIntent);

        }
    }

    private boolean enoughTimePassed(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - time > 1000){
            return true;
        }else{
            return false;
        }
    }

    private void createQRscan(){
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSrc.start(holder);
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSrc.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> qrCodes = detections.getDetectedItems();

                if(qrCodes.size() != 0 ){
                    top.post(new Runnable() {
                        @Override
                        public void run() {
                            //top.setText(qrCodes.valueAt(0).displayValue);

                            if(enoughTimePassed()){
                                time = System.currentTimeMillis();
                                validateQR(qrCodes.valueAt(0).displayValue);
                            }
                        }
                    });

                }
            }
        });
    }
}