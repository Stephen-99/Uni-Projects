package com.example.islandbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//Starting activity. Calls Settings or Map activity.
public class TitleActivity extends AppCompatActivity{
    public static final int MAP_ACTIVITY = 45;

    private Button settings, map;
    private GameData gameData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        //Needs to be initiated on title screen so can ask to continue or start new game in order to
        // access the correct map and/or settings
        gameData = GameData.getInstance(this);

        settings = findViewById(R.id.settings);
        map = findViewById(R.id.map);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TitleActivity.this, SettingsActivity.class));
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(TitleActivity.this, MapActivity.class), MAP_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAP_ACTIVITY && resultCode == Activity.RESULT_OK) {

            //Returned from map activity, may want to change settings for current game or start a new game
            gameData.displayResumePopUp(GameData.RESUME_TEXT,TitleActivity.this);
        }
    }
}