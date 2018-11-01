package com.johansen.dk.madimage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.johansen.dk.madimage.ui.main.MainFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
            /*System.out.println("git!");
            Button loginbtn = findViewById(R.id.loginbtn);
            loginbtn.setOnClickListener(this);
            Button listbtn = findViewById(R.id.listebtn);
            listbtn.setOnClickListener(this);
            Button basketbtn = findViewById(R.id.basketbtn);
            basketbtn.setOnClickListener(this);
            Button editbtn = findViewById(R.id.editbtn);
            editbtn.setOnClickListener(this);
            Button confirmbtn = findViewById(R.id.confirmationtbtn);
            confirmbtn.setOnClickListener(this);*/
        }
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()){
            case R.id.loginbtn:
                //Intent loginIntent = new Intent(this, )
                break;
            case R.id.listebtn:
                Intent listIntent = new Intent(this, smørrebrødsliste.class);
                break;
            case R.id.basketbtn:
                //
                break;
            case R.id.editbtn:
                //
                break;
            case R.id.confirmationtbtn:
                //
                break;
        }*/
    }
}
