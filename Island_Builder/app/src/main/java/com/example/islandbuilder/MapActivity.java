package com.example.islandbuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
    }

    public FragmentSelector getSelector()
    {
        FragmentManager fm = getSupportFragmentManager();

        return (FragmentSelector) fm.findFragmentById(R.id.selectorFrag);
    }

    public FragmentDisplay getDisplayPanel()
    {
        FragmentManager fm = getSupportFragmentManager();

        return (FragmentDisplay) fm.findFragmentById(R.id.displayFrag);
    }

    //This allows title screen to ask if they want to continue or start a new game.
    @Override
    public void onBackPressed() {
        Intent intent =  getIntent();
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}