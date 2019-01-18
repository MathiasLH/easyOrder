package com.johansen.dk.madimage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.johansen.dk.madimage.activities.selectionActivity_tablet;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class optionsActivityFragment extends Fragment implements View.OnClickListener {

    private ImageView foodImage;
    private TextView foodName;
    private com.johansen.dk.madimage.model.foodItem foodItem;
    private LinearLayout LL;
    private ArrayList<CheckBox> cbArray;
    private RadioGroup breadbuttons;
    private RadioButton cbdark, cblight;
    private Vibrator vibe;
    private SharedPreferences prefs = null;
    private selectionActivity_tablet myActivity;
    private View view;
    private ImageButton basketbtn;
    private TextView btnText;
    private Typeface tf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_options_activity_tablet, container, false);

        if(myActivity.isFirstFrag()){
            foodItem = myActivity.getFoodData(0);
            myActivity.setFirstFrag();
        } else {
            prefs = getContext().getSharedPreferences("options_number", MODE_PRIVATE);
            foodItem = myActivity.getFoodData(prefs.getInt("number",2));
        }

        foodImage = view.findViewById(R.id.edit_foodimage);
        foodImage.setImageResource(foodItem.getImageResourceID());
        foodName = view.findViewById(R.id.dish_name);
        foodName.setTypeface(tf);
        foodName.setText(foodItem.getName());
        LL = view.findViewById(R.id.optionsList);
        cbArray = new ArrayList<>();
        createCheckboxes();
        breadbuttons = view.findViewById(R.id.radioGroup2);
        cbdark = breadbuttons.findViewById(R.id.cbdark);
        cbdark.setChecked(foodItem.isDarkBread());
        cblight = breadbuttons.findViewById(R.id.cblight);
        cblight.setChecked(!foodItem.isDarkBread());

        basketbtn = view.findViewById(R.id.editfrag_addtobasketbutton);
        basketbtn.setOnClickListener(this);

        btnText = view.findViewById(R.id.editfrag_buttonText);
        btnText.setOnClickListener(this);

        vibe = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Orkney Regular.ttf");

        myActivity = (selectionActivity_tablet) getActivity();
    }

    private void createCheckboxes() {
        for (int i = 0; i < foodItem.getOptionNames().length; i++) {
            CheckBox cb = new CheckBox(getActivity().getApplicationContext());
            cb.setTextSize(24);
            cb.setText("Med " + foodItem.getOptionNames()[i]);
            cb.setTag(foodItem.getOptionNames()[i]);
            cb.setChecked(foodItem.getOptionValues()[i]);
            cb.setBackgroundResource(R.drawable.checkbox_edit);
            cb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            cb.setHeight(80);
            cbArray.add(cb);
            LL.addView(cb);
        }
    }

    public boolean[] getOptions() {
        boolean optionValues[] = new boolean[foodItem.getOptionNames().length];
        for (int i = 0; i < cbArray.size(); i++) {
            optionValues[i] = cbArray.get(i).isChecked();
        }
        return optionValues;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getContext(),Integer.toString(v.getId()),Toast.LENGTH_SHORT).show();
        switch (v.getId()) {
            case R.id.editfrag_buttonText:
            case R.id.editfrag_addtobasketbutton:
                foodItem.setOptionValues(getOptions());
                foodItem.setDarkBread(cbdark.isChecked());
                if(myActivity.getSelection().getBasket().size()<5) {
                    myActivity.addItemToBasket(foodItem);
                    myActivity.updateTopIcon();
                } else {
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View promptsView = li.inflate(R.layout.limit, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getContext());
                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);
                        alertDialogBuilder.setTitle("For mange smørrebrød");
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
                vibe.vibrate(100);
                break;
            default:
                Toast.makeText(getContext(),"DEFUALT HIT",Toast.LENGTH_SHORT).show();
        }
    }
}
