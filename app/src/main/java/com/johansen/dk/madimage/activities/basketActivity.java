package com.johansen.dk.madimage.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.adapter.basketAdapter;
import com.johansen.dk.madimage.model.foodItem;
import com.johansen.dk.madimage.model.onPauseClock;

import java.util.Locale;

public class basketActivity extends AppCompatActivity implements View.OnClickListener {
    private com.johansen.dk.madimage.model.order order;
    private Button orderBtn;
    private TextView topText;
    private RecyclerView foodList;
    private basketAdapter niceAdapter;
    private TextToSpeech myTTS;
    private int lastItemClicked;
    private LottieAnimationView emptyBasketGif;
    private boolean isOrderAvailable;
    private Vibrator vibe;
    /*instans of database*/
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference("Beboere");
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedprefs = getSharedPreferences("screen_version", MODE_PRIVATE);
        if (sharedprefs.getBoolean("tablet", false)) {
            setContentView(R.layout.activity_basket_tablet);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setContentView(R.layout.activity_basket);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        emptyBasketGif = findViewById(R.id.empty_basket_gif);
        orderBtn = findViewById(R.id.orderbtn);
        orderBtn.setOnClickListener(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");

        topText = findViewById(R.id.basket_toptext);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(topText, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        topText.setTypeface(tf);

        orderBtn.setTypeface(tf);
        Intent i = getIntent();
        order = (com.johansen.dk.madimage.model.order) i.getSerializableExtra("orderObject");
        foodList = findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        foodList.setLayoutManager(mLayoutManager);
        niceAdapter = new basketAdapter(order.getBasket(), this, tf);
        niceAdapter.setOnItemClickListener(new basketAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (v.getTag() == "TRASH") {
                    niceAdapter.removeItemAt(position);
                    v.setOnClickListener(null);
                    isOrderAvailable();
                }
                if (v.getTag() == "TTS") {
                    Log.e("TTS", "@@@@@@@@@@@@@@@@@@@" + Integer.toString(v.getId()));
                    readDish(position);
                }
                if (v.getTag() == "OTHER") {
                    vibe.vibrate(100);
                    launchEditActivity(position);
                }
            }
        });

        foodList.setAdapter(niceAdapter);

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    prefs = getSharedPreferences("setLanguage", MODE_PRIVATE);
                    int result = myTTS.setLanguage(new Locale(prefs.getString("language", Locale.getDefault().getDisplayLanguage()), ""));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supportd");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });
        isOrderAvailable();
    }

    private void launchEditActivity(int position) {
        Intent editIntent = new Intent(this, optionsActivity.class);
        editIntent.putExtra("foodItem", order.getBasket().get(position));
        lastItemClicked = position;
        startActivityForResult(editIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            order.removeItem(lastItemClicked);
            order.addItem((foodItem) data.getSerializableExtra("foodItem"));
            niceAdapter.notifyDataSetChanged();
        }
        isOrderAvailable();
    }

    private void readDish(int position) {
        //https://stackoverflow.com/questions/30706780/texttospeech-deprecated-speak-function-in-api-level-21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myTTS.speak(order.getBasket().get(position).toString(), TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            myTTS.speak(order.getBasket().get(position).toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("orderObject", order);
        setResult(2, intent);
        finish();
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
        if (myTTS.isSpeaking()) {
            myTTS.stop();
        }
        long time = System.currentTimeMillis();
        onPauseClock.getInstance().setTimeLeft(time);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (onPauseClock.getInstance().isReset(System.currentTimeMillis())) {
            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
            // for info about clear task: https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_CLEAR_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //for info about new task : https://developer.android.com/reference/android/content/Intent.html#FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        vibe.vibrate(100);
        switch (v.getId()) {
            case R.id.orderbtn:
                if (isOrderAvailable) {
                    addOrderToDatabase();

                    Intent intent = new Intent(this, recieptActivity.class);
                    intent.putExtra("order", order);
                    startActivity(intent);
                    order.clean();
                } else
                    Toast.makeText(this, getString(R.string.toast_emptyBasket), Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(this, "DEFAULT HIT IN ONCLICK SWITCH", Toast.LENGTH_LONG).show();
        }
    }

    public void isOrderAvailable() {

        if (order.getBasket().size() < 1) {
            emptyBasketGif.setVisibility(View.VISIBLE);
            orderBtn.setBackgroundColor(getResources().getColor(R.color.grey));
            isOrderAvailable = false;
        } else {
            emptyBasketGif.setVisibility(View.GONE);
            orderBtn.setBackgroundColor(getResources().getColor(R.color.btnColor));
            isOrderAvailable = true;
        }
    }

    public void addOrderToDatabase() {
        String id = mRootRef.push().getKey();
        mRootRef.child(id).setValue(order.getBasket().toString());
    }
}