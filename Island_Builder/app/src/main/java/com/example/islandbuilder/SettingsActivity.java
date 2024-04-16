package com.example.islandbuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SettingsActivity extends AppCompatActivity {

    private GameData gameData;
    private Settings settings;
    private TextView name, mapWidth,  mapHeight, initMoney, famSize, shopSize, salary, taxRate, serviceCost, houseCost, commercialCost, roadCost;
    private Button done;
    private HashMap<String, TextView> textViews = new HashMap<String, TextView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        gameData = GameData.getInstance(this);
        settings = gameData.getSettings();

        name = findViewById(R.id.name);
        mapWidth = findViewById(R.id.map_width);
        mapHeight = findViewById(R.id.map_height);
        initMoney = findViewById(R.id.initial_money);
        famSize = findViewById(R.id.family_size);
        shopSize = findViewById(R.id.shop_size);
        salary = findViewById(R.id.salary);
        taxRate = findViewById(R.id.tax_rate);
        serviceCost = findViewById(R.id.service_cost);
        houseCost = findViewById(R.id.house_cost);
        commercialCost = findViewById(R.id.commercial_cost);
        roadCost = findViewById(R.id.road_cost);
        done = findViewById(R.id.done);

        //Store all the TextViews in a Map so that they can be used in a loop for setting the text
        //and onClickListeners using reflection.
        textViews.put("Name", name);
        textViews.put("MapWidth", mapWidth);
        textViews.put("MapHeight", mapHeight);
        textViews.put("InitMoney", initMoney);
        textViews.put("FamilySize", famSize);
        textViews.put("ShopSize", shopSize);
        textViews.put("Salary", salary);
        textViews.put("TaxRate", taxRate);
        textViews.put("ServiceCost", serviceCost);
        textViews.put("HouseCost", houseCost);
        textViews.put("CommercialCost", commercialCost);
        textViews.put("RoadCost", roadCost);

        setTextViews();
        setOnClickListeners();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    //Gets all the values from the settings object using reflection to access the correct getter
    // method, and displays the retrieved values in the TextView.
    private void setTextViews()
    {
        //Setting the text for each textView
        for (Map.Entry<String, TextView> entry : textViews.entrySet())
        {
            String name = entry.getKey();
            Class settingsClass = settings.getClass();
            Method getter = null;
            try {
                getter = settingsClass.getDeclaredMethod("get" + name);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                Log.i("TAG HERE!", "onCreate: ERROR OBTAINING GETTER in " + name);
            }

            try {
                String value = String.valueOf(getter.invoke(settings));
                //Getting a prefix from strings.xml
                value = getResources().getText(getResources().getIdentifier(name, "string", "com.example.islandbuilder"))  + " " + value;
                //Formatting to length of 20 spaces.
                value = String.format("%-20s", value);
                entry.getValue().setText(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                Log.i("TAG HERE!", "onCreate: ERROR SETTING TEXT in " + name);
            }
        }
    }

    //Same as above, but only for 1 value, for when it gets updated via the onClickListener.
    private void setTextView(String name)
    {
        Class settingsClass = settings.getClass();
        Method getter = null;
        try {
            getter = settingsClass.getDeclaredMethod("get" + name);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            String value = String.valueOf(getter.invoke(settings));
            //Getting a prefix from strings.xml
            value = getResources().getText(getResources().getIdentifier(name, "string", "com.example.islandbuilder"))  + " " + value;
            //Formatting to length of 20 spaces.
            value = String.format("%-20s", value);

            //Find the corresponding textView and set the value
            textViews.get(name).setText(value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //sets all the onClickListeners for the TextViews
    private void setOnClickListeners()
    {

        for (Map.Entry<String, TextView> entry : textViews.entrySet())
        {
            final String name = entry.getKey();
            final String displayName = getResources().getText(getResources().getIdentifier(name, "string", "com.example.islandbuilder")) + " ";
            TextView curView = entry.getValue();
            curView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                displayEditPopUp("Please enter a new value for " + displayName, SettingsActivity.this, name);
                }
            });
        }
    }

    //Display a popUp asking the user to enter a new value for the selected setting
    private void displayEditPopUp(String msg, Context context, final String settingName)
    {
        final Dialog popUp = new Dialog(context);
        popUp.setContentView(R.layout.pop_up_edit);

        TextView txt = popUp.findViewById(R.id.text_box);
        final EditText input = popUp.findViewById(R.id.number_input);

        //The rest of the settings take numerical input.
        if (settingName.equals("Name")) {
            input.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        txt.setText(msg);

        Button ok = popUp.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //They have clicked to close. Try to set the value in the input
                //Invalid values handled by setValue.
                String result = input.getText().toString();
                popUp.dismiss();
                setValue(result, settingName);
            }
        });
        popUp.show();
    }

    //Attempts to set the new value for the TextView, using reflection to access the setter, catching any invalid values.
    private void setValue(String newValue, String name)
    {
        Class settingsClass = settings.getClass();
        Method setter = null;

        if ((name.equals("MapWidth") || name.equals("MapHeight")) && gameData.isContinuing()) {
            GameData.displayPopUp("Cannot change this value when in the middle of a game", SettingsActivity.this);
        } else {
            try {
                setter = settingsClass.getMethod("set" + name, String.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                Log.i("TAG HERE!", "ERROR OBTAINING SETTER in " + name);
            }
            try {
                //Since we are using reflection, it will throw a InvocationTargetException instead of the IllegalArgumentException thrown in Settings
                setter.invoke(settings, newValue);
                setTextView(name);
            } catch (IllegalAccessException | InvocationTargetException e) {
                GameData.displayPopUp("The number entered was invalid", SettingsActivity.this);
            }
        }
    }
}