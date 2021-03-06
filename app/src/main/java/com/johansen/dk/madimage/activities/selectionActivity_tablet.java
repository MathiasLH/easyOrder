package com.johansen.dk.madimage.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.adapter.selectionAdapter;
import com.johansen.dk.madimage.model.foodItem;
import com.johansen.dk.madimage.model.onPauseClock;
import com.johansen.dk.madimage.model.order;
import com.johansen.dk.madimage.optionsActivityFragment;

import java.util.ArrayList;
import java.util.Locale;

public class selectionActivity_tablet extends AppCompatActivity implements View.OnClickListener {

    private order selection;
    private boolean doubleBackToExitPressedOnce = false;
    private foodItem dyrlaege, laks, rejemad, roastbeef, stjerneskud;
    private TextView leftText, rightText;
    private ArrayList<foodItem> foodItems;
    private RecyclerView foodList;
    private TextToSpeech myTTS;
    private ImageButton basketBtn;
    private Animation basketAnimation;
    private Vibrator vibe;
    private SharedPreferences prefs = null;
    private boolean booFirstFrag = true;
    private  Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createTestData();
        setContentView(R.layout.activity_selection_tablet);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");

        foodList = findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        foodList.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter niceAdapter = new selectionAdapter(foodItems, tf);
        ((selectionAdapter) niceAdapter).setOnItemClickListener(new selectionAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Log.e("TTS", "@@@@@@@@@@@@@@@@@@@" + v.getTag());

                if (v.getTag() == "TTS") {
                    Log.e("TTS", "@@@@@@@@@@@@@@@@@@@" + Integer.toString(v.getId()));
                    readDish(position);
                }

                if (v.getTag() == "OTHER") {
                    launchEditActivity(position);
                }
            }
        });

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // language da = danish ; en = english ; turkish = tr ; russisk = ru
                    prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
                    int result = myTTS.setLanguage(new Locale(prefs.getString("language","en"), ""));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supportd");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        foodList.setAdapter(niceAdapter);
        basketBtn = findViewById(R.id.basketbtn);
        basketBtn.setOnClickListener(this);
        selection = new order();
        Intent niceIntent = getIntent();
        selection.setRoom(niceIntent.getStringExtra("roomNo"));
        findViewById(R.id.basketbtn).setTransitionName("indkoebTrans");

        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        setText();

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
        String stjerneskudOptions[] = {"rogn", "citron", "hvid asparges", "grøn asparges", "rejer"};
        stjerneskud = new foodItem("Stjerneskud", R.drawable.stjerneskud_big, 10, stjerneskudOptions);
        foodItems = new ArrayList<>();
        foodItems.add(dyrlaege);
        foodItems.add(laks);
        foodItems.add(rejemad);
        foodItems.add(roastbeef);
        foodItems.add(stjerneskud);
    }

    private void launchEditActivity(int position) {
        prefs = getSharedPreferences("options_number", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("number", position).apply();

        Fragment optionsFrag = new optionsActivityFragment();
        FragmentManager transaction = getSupportFragmentManager();
        transaction.beginTransaction().replace(R.id.optionsHolder, optionsFrag).commit();
    }

    public foodItem getFoodData(int position) {
        return foodItems.get(position);
    }

    public order getSelection() {
        return selection;
    }

    public void updateTopIcon() {

        basketAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinkanim);
        basketBtn.startAnimation(basketAnimation);

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

    public void addItemToBasket(foodItem foodItem) {
        selection.addItem(foodItem);
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
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 1) {
                selection.addItem((foodItem) data.getSerializableExtra("foodItem"));
            }
        }
        if (resultCode == 2) {
            selection = (order) data.getSerializableExtra("orderObject");
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
        long time = System.currentTimeMillis();
        onPauseClock.getInstance().setTimeLeft(time);
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
        updateTopIcon();
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

    public boolean isFirstFrag(){
        return booFirstFrag;
    }

    public void setFirstFrag(){
        booFirstFrag = false;
    }

    private void setText() {
        leftText = findViewById(R.id.texttop);
        rightText = findViewById(R.id.texttop2);

        leftText.setTypeface(tf);
        rightText.setTypeface(tf);

    }
}
