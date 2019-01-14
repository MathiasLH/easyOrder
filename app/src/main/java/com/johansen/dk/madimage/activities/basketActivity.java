package com.johansen.dk.madimage.activities;

import android.graphics.Typeface;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.johansen.dk.madimage.R;
import com.johansen.dk.madimage.adapter.basketAdapter;
import com.johansen.dk.madimage.model.foodItem;

import java.util.ArrayList;
import java.util.Locale;

public class basketActivity extends AppCompatActivity implements View.OnClickListener{
    com.johansen.dk.madimage.model.order order;
    Button orderBtn;
    TextView basketText;
    RecyclerView foodList;
    ArrayList<foodItem> fooditems;
    ArrayList<LinearLayoutManager> LLM;
    basketAdapter niceAdapter;
    TextToSpeech myTTS;
    int lastItemClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        orderBtn = findViewById(R.id.orderbtn);
        orderBtn.setOnClickListener(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Orkney Regular.ttf");
        orderBtn.setTypeface(tf);
        basketText = findViewById(R.id.toptekst);
        basketText.setTypeface(tf);
        Intent i = getIntent();
        order = (com.johansen.dk.madimage.model.order) i.getSerializableExtra("orderObject");
        foodList = findViewById(R.id.foodList);
        foodList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        foodList.setLayoutManager(mLayoutManager);
        LLM = new ArrayList<>();
        for(int j = 0; j < order.getBasket().size(); j++){
                LLM.add(new LinearLayoutManager(this));
        }
        niceAdapter = new basketAdapter(order.getBasket(), LLM);
        niceAdapter.setOnItemClickListener(new basketAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                if(v.getTag()=="TRASH"){
                    niceAdapter.removeItemAt(position);
                }
                if(v.getTag()=="TTS"){
                    Log.e("TTS", "@@@@@@@@@@@@@@@@@@@" + Integer.toString(v.getId()));
                    readDish(position);
                }
                if(v.getTag()=="OTHER"){
                    launchEditActivity(position);
                }
            }
        });
        foodList.setAdapter(niceAdapter);

        myTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS){
                    int result = myTTS.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supportd");
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        Toast.makeText(this,Integer.toString(order.getBasket().size()),Toast.LENGTH_SHORT).show();

        if (order.getBasket().size() < 1){
            orderBtn.setBackgroundColor(getResources().getColor(R.color.grey));
        }

    }

    private void drawList(){

    }

    private void launchEditActivity(int position){
        Intent editIntent = new Intent(this, optionsActivity.class);
        editIntent.putExtra("foodItem", order.getBasket().get(position));
        lastItemClicked = position;
        //CardView cv = (CardView) foodList.findViewHolderForAdapterPosition(position).itemView;
        //ImageView iv = cv.getChildAt(0).findViewById(foodItems.get(position).getImageID());
        //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(selectionActivity.this, iv, ViewCompat.getTransitionName(iv));
        startActivityForResult(editIntent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED){

            order.removeItem(lastItemClicked);
            order.addItem((foodItem) data.getSerializableExtra("foodItem"));
            niceAdapter.notifyDataSetChanged();
        }
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
    public void onBackPressed(){
      Intent intent = new Intent();
      intent.putExtra("orderObject",order);
      setResult(2, intent);
      finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.orderbtn:

                Intent intent = new Intent(this,recieptActivity.class);
                intent.putExtra("order", order);

                startActivity(intent);
                order.clean();
                break;
        }
    }
}