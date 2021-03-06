package com.johansen.dk.madimage.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.adapter.selectionAdapter;
import com.johansen.dk.madimage.model.onPauseClock;
import com.johansen.dk.madimage.model.order;
import com.johansen.dk.madimage.model.foodItem;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class selectionActivity extends AppCompatActivity implements View.OnClickListener {
    private order selection;
    private boolean animationConfirmation, doubleBackToExitPressedOnce = false;
    private foodItem dyrlaege, laks, rejemad, roastbeef, stjerneskud;
    private TextView topText;
    private ArrayList<foodItem> foodItems;
    private RecyclerView foodList;
    private TextToSpeech myTTS;
    private ImageButton basketBtn;
    private Animation basketAnimation;
    private final Context context = this;
    private boolean clickAllowed = true;
    private Vibrator vibe;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_selection);
        createTestData();
        foodList = findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        foodList.setLayoutManager(mLayoutManager);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");
        RecyclerView.Adapter niceAdapter = new selectionAdapter(foodItems, tf);
        ((selectionAdapter) niceAdapter).setOnItemClickListener(new selectionAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Log.e("TTS", "@@@@@@@@@@@@@@@@@@@" + v.getTag());

                if (v.getTag() == "TTS" && clickAllowed) {
                    Log.e("TTS", "@@@@@@@@@@@@@@@@@@@" + Integer.toString(v.getId()));
                    readDish(position);
                }

                if (v.getTag() == "OTHER" && clickAllowed) {
                    clickAllowed = false;
                    if (selection.getBasket().size() < 5) {
                        launchEditActivity(position);
                    } else {
                        LayoutInflater li = LayoutInflater.from(context);
                        View promptsView = li.inflate(R.layout.limit, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                context);

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        alertDialogBuilder.setTitle(getString(R.string.popup_limit_title));

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
                    }
                }


            }
        });

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // language da = danish ; en = english ; turkish = tr ; russisk = ru
                    prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
                    int result = myTTS.setLanguage(new Locale(prefs.getString("language",Locale.getDefault().getDisplayLanguage()), ""));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supportd");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        foodList.setAdapter(niceAdapter);

        topText = findViewById(R.id.texttop);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(topText, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        topText.setTypeface(tf);

        basketBtn = findViewById(R.id.basketbtn);
        basketBtn.setOnClickListener(this);
        selection = new order();
        Intent niceIntent = getIntent();
        selection.setRoom(niceIntent.getStringExtra("roomNo"));
        findViewById(R.id.basketbtn).setTransitionName("indkoebTrans");

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        onPauseClock.getInstance().setTimeLeft(System.currentTimeMillis());
    }

    private void createTestData() {
        String dyrlaegeOptions[] = {"sky", "rødløg", "karse"};
        dyrlaege = new foodItem("Dyrlægens natmad", R.drawable.dyrlaegensnatmad_big, 100, dyrlaegeOptions);
        String laksOptions[] = {"dild", "citron", "asparges"};
        laks = new foodItem("Lakse mad", R.drawable.laks_big, 101, laksOptions);
        String rejeOptions[] = {"krydderUrt"};
        rejemad = new foodItem("Rejemad", R.drawable.rejemad_big, 102, rejeOptions);
        String roastbeefOptions[] = {"peberrod", "salat", "syltet agurk"};
        roastbeef = new foodItem("Roastbeef", R.drawable.roastbeef_big, 103, roastbeefOptions);
        String stjerneskudOptions[] = {"citron", "hvid asparges", "grøn asparges", "rejer"};
        stjerneskud = new foodItem("Stjerneskud", R.drawable.stjerneskud_big, 10, stjerneskudOptions);
        foodItems = new ArrayList<>();
        foodItems.add(dyrlaege);
        foodItems.add(laks);
        foodItems.add(rejemad);
        foodItems.add(roastbeef);
        foodItems.add(stjerneskud);
    }

    private void launchEditActivity(int position) {
        Intent editIntent = new Intent(this, optionsActivity.class);
        editIntent.putExtra("orderObject", selection);
        editIntent.putExtra("foodItem", foodItems.get(position));
        CardView cv = (CardView) foodList.findViewHolderForAdapterPosition(position).itemView;
        ImageView iv = cv.getChildAt(0).findViewById(foodItems.get(position).getImageID());
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(selectionActivity.this, iv, ViewCompat.getTransitionName(iv));
        startActivityForResult(editIntent, 1, options.toBundle());
    }

    private void readDish(int position) {
        CardView cv = (CardView) foodList.findViewHolderForAdapterPosition(position).itemView;
        TextView tv = cv.getChildAt(0).findViewById(R.id.cardName);
        String text = tv.getText().toString();

        //https://stackoverflow.com/questions/30706780/texttospeech-deprecated-speak-function-in-api-level-21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        clickAllowed = true;
        ImageButton basketBtn = findViewById(R.id.basketbtn);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 1) {
                selection.addItem((foodItem) data.getSerializableExtra("foodItem"));
                animationConfirmation = (boolean) data.getSerializableExtra("boolean");
            }
        }
        if (resultCode == 2) {
            selection = (order) data.getSerializableExtra("orderObject");
        }
        if (animationConfirmation == true) {
            basketAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinkanim);
            basketBtn.startAnimation(basketAnimation);
            animationConfirmation = false;
        }
        switch (selection.getBasket().size()) {
            case 0:
                basketBtn.setImageResource(R.drawable.serveringsbakke_new);
                break;
            case 1:
                basketBtn.setImageResource(R.drawable.serveringsbakke_1_new);
                break;
            case 2:
                basketBtn.setImageResource(R.drawable.serveringsbakke_2_new);
                break;
            case 3:
                basketBtn.setImageResource(R.drawable.serveringsbakke_3_new);
                break;
            case 4:
                basketBtn.setImageResource(R.drawable.serveringsbakke_4_new);
                break;
            case 5:
                basketBtn.setImageResource(R.drawable.serveringsbakke_5_new);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        vibe.vibrate(100);
        switch (v.getId()) {
            case R.id.basketbtn:
                Intent basketIntent = new Intent(this, basketActivity.class);
                basketIntent.putExtra("orderObject", selection);
                startActivityForResult(basketIntent, 2);
                break;
            default:
                Toast.makeText(v.getContext(), "DEFAULT HIT", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (myTTS.isSpeaking()) {
            myTTS.stop();
            myTTS.shutdown();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myTTS.isSpeaking()) {
            myTTS.stop();
        }
        onPauseClock.getInstance().setTimeLeft(System.currentTimeMillis());
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(onPauseClock.getInstance().isReset(System.currentTimeMillis())){
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            // for info about clear task: https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_CLEAR_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //for info about new task : https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    //dont want to reinvent the wheel: https://stackoverflow.com/questions/8430805/clicking-the-back-button-twice-to-exit-an-activity
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.toast_backToLogin), Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
