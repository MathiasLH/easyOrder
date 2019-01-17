package com.johansen.dk.madimage.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.model.order;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.util.Locale;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton danishFlag, englishFlag, turkishFlag;
    SurfaceView cameraPreview;
    CameraSource cameraSrc;
    BarcodeDetector barcodeDetector;
    TextView loginInfo;
    Button helpBtn, moveAlongBtn, clearPerm;
    final Context context = this;
    /*me love u*/ long time = 0;
    int tempHeight, tempWidth;
    SharedPreferences prefs = null;
    Typeface tf;
    final RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance
    Vibrator vibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTablet()) {
            setContentView(R.layout.activity_login_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setContentView(R.layout.activity_login);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        loginInfo = (TextView) findViewById(R.id.explqr);
        helpBtn = (Button) findViewById(R.id.helpBtn);
        moveAlongBtn = (Button) findViewById(R.id.moveAlongBtn);
        clearPerm = (Button) findViewById(R.id.clearPermission);

        tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");
        /*using fonts on text fields*/
        loginInfo.setTypeface(tf);
        helpBtn.setTypeface(tf);
        helpBtn.setTypeface(tf);

        /*defining elements for QR-scanner*/
        //from youtubevideo: https://www.youtube.com/watch?v=ej51mAYXbKs
        cameraPreview = (SurfaceView) findViewById(R.id.cameraPreview);
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSrc = new CameraSource.Builder(this, barcodeDetector).setRequestedPreviewSize(1024, 768)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(30.0f)
                .setAutoFocusEnabled(true)
                .build();
        tempHeight = cameraPreview.getHeight();
        tempWidth = cameraPreview.getWidth();

        /*defining image Buttons*/
        danishFlag = findViewById(R.id.danishFlag);
        englishFlag = findViewById(R.id.englishFlag);
        turkishFlag = findViewById(R.id.turkishFlag);

        /*OnClick listener on Buttons*/
        moveAlongBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        danishFlag.setOnClickListener(this);
        englishFlag.setOnClickListener(this);
        turkishFlag.setOnClickListener(this);
        clearPerm.setOnClickListener(this);

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;

        hasCameraPermission();
        createQRscan();
    }

    @Override
    public void onClick(View v) {
        vibe.vibrate(100);
        /*Implementing on click listener to QR-code image Button*/
        switch (v.getId()) {
            case R.id.moveAlongBtn:
                startActivity(new Intent(loginActivity.this, selectionActivity.class));
                break;

            case R.id.helpBtn:
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.help, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(true)

                        .setPositiveButton("OK",

                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                break;
            case R.id.danishFlag:
                prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
                prefs.edit().putString("language", "da").commit();
                setLocale();
                Toast.makeText(this, "LANGUAGE SET: DK", Toast.LENGTH_SHORT).show();
                break;
            case R.id.englishFlag:
                prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
                prefs.edit().putString("language", "en").commit();
                setLocale();
                Toast.makeText(this, "LANGUAGE SET: ENG", Toast.LENGTH_SHORT).show();
                break;
            case R.id.turkishFlag:
                prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
                prefs.edit().putString("language", "tr").commit();
                setLocale();
                Toast.makeText(this, "LANGUAGE SET: TR", Toast.LENGTH_SHORT).show();
                break;
            case R.id.clearPermission:
                prefs = getSharedPreferences("permission", MODE_PRIVATE);
                prefs.edit().clear().commit();
                Toast.makeText(this, "RESTART APP", Toast.LENGTH_SHORT).show();
                break;
                default: Toast.makeText(this, "DEFAULT HIT", Toast.LENGTH_SHORT).show();
        }
    }

    private void validateQR(String input) {
        String regex = "([a-zA-Z]+[0-9]+)";
        if (input.matches(regex)) {
            for(int i=0;i<2;i++) {
                vibe.vibrate(200);
            }
            Intent niceIntent = new Intent(loginActivity.this, selectionActivity.class);
            niceIntent.putExtra("roomNo", input);
            startActivity(niceIntent);
        }
    }

    private boolean enoughTimePassed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - time > 3000) {
            return true;
        } else {
            return false;
        }
    }

    private void createQRscan() {
        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                try {
                    cameraSrc.start(holder);
                } catch (IOException e) {
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

                if (qrCodes.size() != 0) {
                    loginInfo.post(new Runnable() {
                        @Override
                        public void run() {
                            //top.setText(qrCodes.valueAt(0).displayValue);

                            if (enoughTimePassed()) {
                                time = System.currentTimeMillis();
                                validateQR(qrCodes.valueAt(0).displayValue);
                            }
                        }
                    });

                }
            }
        });
    }

    //this method of checking screensize is from stackoverflow: https://stackoverflow.com/questions/9279111/determine-if-the-device-is-a-smartphone-or-tablet
    private boolean isTablet() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;

        prefs = getSharedPreferences("screen_version", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);
        if (diagonalInches >= 6.5) {
            editor.putBoolean("tablet", true);
            editor.commit();
            return true;
        } else {
            editor.putBoolean("tablet", false);
            editor.commit();
            return false;
        }
    }

    protected void onResume() {
        super.onResume();
        prefs = getSharedPreferences("com.johansen.easyOrder", MODE_PRIVATE);
        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
            prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
            prefs.getString("da","");
            setLocale();
        }
    }

    private void setLocale() {
        prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
        String lang = prefs.getString("language", "");
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        setText();
    }

    private void setText(){
        prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
        String lang = prefs.getString("language", "");
        helpBtn.setText(getString(R.string.help_button_text));
        loginInfo.setText(getString(R.string.login_info));
    }

    //permission library: https://github.com/tbruyelle/RxPermissions
    private void askPermission(){
        // Must be done during an initialization phase like onCreate
        rxPermissions
                .request(Manifest.permission.CAMERA)
                .subscribe(granted -> {
                    if (granted) {
                        prefs.edit().putBoolean("cameraPermission", true).commit();
                        recreate();
                    } else {
                        // Oups permission denied
                    }
                });
    }

    private void hasCameraPermission(){
        prefs = getSharedPreferences("permission",MODE_PRIVATE);
        if(prefs.getBoolean("cameraPermission",false)==false){
            askPermission();
        }
    }
}
