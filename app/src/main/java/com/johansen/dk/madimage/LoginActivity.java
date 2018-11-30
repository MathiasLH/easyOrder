package com.johansen.dk.madimage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
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

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    /*initialize ImageButtons */
    ImageButton danishFlag, englishFlag, arabFlag;
    SurfaceView cameraPreview;
    CameraSource cameraSrc;
    BarcodeDetector barcodeDetector;
    TextView top;
    Button helpBtn, moveAlongBtn;

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
        cameraSrc = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(800, 800).build();
        tempHeight = cameraPreview.getHeight();
        tempWidth = cameraPreview.getWidth();

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
                        Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        vibrator.vibrate(1000);
                        top.setText(qrCodes.valueAt(0).displayValue);
                        }
                    });

                }
            }
        });

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

    }

    @Override
    public void onClick(View v) {
        /*Implementing on click listener to QR-code image Button*/
        switch (v.getId()){
            case R.id.moveAlongBtn:
                Intent listIntent = new Intent(this, SmørrebrødsListe.class);
                startActivity(listIntent);
                break;
            case R.id.helpBtn:
                Toast.makeText(this, "NOT IMPLEMENTED", Toast.LENGTH_SHORT).show();
                //Intent listIntent = new Intent(this, SmørrebrødsListe.class);
                //startActivity(listIntent);
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
}
