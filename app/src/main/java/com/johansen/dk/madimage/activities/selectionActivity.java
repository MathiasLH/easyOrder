package com.johansen.dk.madimage.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.adapter.selectionAdapter;
import com.johansen.dk.madimage.model.order;
import com.johansen.dk.madimage.model.foodItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class selectionActivity extends AppCompatActivity implements View.OnClickListener{
    order selection;
    boolean animationConfirmation;
    foodItem dyrlaege, laks, rejemad, roastbeef, stjerneskud;
    TextView text;
    ArrayList<foodItem> foodItems;
    RecyclerView foodList;
    TextToSpeech myTTS;
    ImageButton basketBtn;
    Animation basketAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        createTestData();
        foodList = findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        foodList.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter niceAdapter = new selectionAdapter(foodItems);
        ((selectionAdapter) niceAdapter).setOnItemClickListener(new selectionAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Log.e("TTS", "@@@@@@@@@@@@@@@@@@@" + v.getTag());

                if(v.getTag()=="TTS") {
                    Log.e("TTS", "@@@@@@@@@@@@@@@@@@@" + Integer.toString(v.getId()));
                    readDish(position);
                }

                if(v.getTag()=="OTHER") {
                    launchEditActivity(position);
                }


            }
        });

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    // language da = danish ; en = english ; turkish = tr ; russisk = ru
                    int result = myTTS.setLanguage(new Locale("da", ""));
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supportd");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        foodList.setAdapter(niceAdapter);
        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Orkney Regular.ttf");
        text = (TextView) findViewById(R.id.texttop);
        text.setTypeface(tf);
        basketBtn = (ImageButton) findViewById(R.id.basketbtn);
        basketBtn.setOnClickListener(this);
        selection = new order();
        Intent niceIntent = getIntent();
        selection.setRoom(niceIntent.getStringExtra("roomNo"));
        findViewById(R.id.basketbtn).setTransitionName("indkoebTrans");
    }

    private void createTestData(){
        /*ArrayList<String> dyrlaegeOptions = new ArrayList<>();
        dyrlaegeOptions.add("sky");
        dyrlaegeOptions.add("rødløg");
        dyrlaegeOptions.add("karse");*/
        String dyrlaegeOptions[] = {"sky", "rødløg", "karse"};
        dyrlaege = new foodItem("Dyrlægens natmad", R.drawable.dyrlaegensnatmad_big, 100, dyrlaegeOptions);
        /*ArrayList<String> laksOptions = new ArrayList<>();
        laksOptions.add("dild");
        laksOptions.add("citron");
        laksOptions.add("asperges");*/
        String laksOptions[] = {"dild", "citron", "asparges"};
        laks = new foodItem("Lakse mad", R.drawable.laks_big, 101, laksOptions);
        /*ArrayList<String> rejeOptions = new ArrayList<>();
        rejeOptions.add("krydderurt");*/
        String rejeOptions[] = {"krydderUrt"};
        rejemad = new foodItem("Rejemad", R.drawable.rejemad_big, 102, rejeOptions);
        /*ArrayList<String> roastbeefOptions = new ArrayList<>();
        roastbeefOptions.add("peberrod");
        roastbeefOptions.add("salat");
        roastbeefOptions.add("Syltet agurk");*/
        String roastbeefOptions[] = {"peberrod", "salat", "syltet agurk"};
        roastbeef = new foodItem("Roastbeef", R.drawable.roastbeef_big, 103, roastbeefOptions);
        /*ArrayList<String> stjerneskudOptions = new ArrayList<>();
        stjerneskudOptions.add("fiskeægting");
        stjerneskudOptions.add("citron");
        stjerneskudOptions.add("hvid asparges");
        stjerneskudOptions.add("grøn asparges");
        stjerneskudOptions.add("rejer");*/
        String stjerneskudOptions[] = {"rogn", "citron", "hvid asparges", "grøn asparges", "rejer"};
        stjerneskud = new foodItem("Stjerneskud", R.drawable.stjerneskud_big, 10, stjerneskudOptions);
        foodItems = new ArrayList<>();
        foodItems.add(dyrlaege);
        foodItems.add(laks);
        foodItems.add(rejemad);
        foodItems.add(roastbeef);
        foodItems.add(stjerneskud);
    }

    private void launchEditActivity(int position){
        Intent editIntent = new Intent(this, optionsActivity.class);
        editIntent.putExtra("orderObject", selection);
        editIntent.putExtra("foodItem", foodItems.get(position));
        CardView cv = (CardView) foodList.findViewHolderForAdapterPosition(position).itemView;
        ImageView iv = cv.getChildAt(0).findViewById(foodItems.get(position).getImageID());
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(selectionActivity.this, iv, ViewCompat.getTransitionName(iv));
        startActivityForResult(editIntent,1, options.toBundle());
    }

    private void readDish(int position){
        CardView cv = (CardView) foodList.findViewHolderForAdapterPosition(position).itemView;
        TextView tv = cv.getChildAt(0).findViewById(R.id.cardName);
        String text = tv.getText().toString();

        //https://stackoverflow.com/questions/30706780/texttospeech-deprecated-speak-function-in-api-level-21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            myTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            myTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageButton basketBtn = findViewById(R.id.basketbtn);
        if(resultCode != RESULT_CANCELED){
            if(requestCode == 1){
                selection.addItem((foodItem) data.getSerializableExtra("foodItem"));
                //selection = (order) data.getSerializableExtra("orderObject");
                animationConfirmation = (boolean) data.getSerializableExtra("boolean");
            }
        }
        if(resultCode == 2){
            selection = (order) data.getSerializableExtra("orderObject");
        }
        if(animationConfirmation == true){
            basketAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blinkanim);
            basketBtn.startAnimation(basketAnimation);
            animationConfirmation = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.basketbtn:
                Intent basketIntent = new Intent(this, basketActivity.class);
                basketIntent.putExtra("orderObject", selection);
                startActivityForResult(basketIntent, 2);
                break;
            default:
                Toast.makeText(v.getContext(),"DEFAULT HIT",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    protected void onDestroy() {
        if (myTTS != null){
            myTTS.stop();
            myTTS.shutdown();
        }
        super.onDestroy();
    }
}
