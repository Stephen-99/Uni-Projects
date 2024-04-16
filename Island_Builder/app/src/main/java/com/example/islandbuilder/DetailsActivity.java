package com.example.islandbuilder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;

public class DetailsActivity extends AppCompatActivity {

    public final int CAMERA_ACTION = 121;

    private GameData gameData;
    private MapElement element;
    private Structure structure;

    private TextView gridCoords, type, name;
    private Button camera;
    private ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        final int row, col;
        if (intent.hasExtra("row") && intent.hasExtra("col")) {
            row = intent.getIntExtra("row", 0);
            col = intent.getIntExtra("col", 0);
        } else {
            throw new NoSuchElementException();
        }

        gameData = GameData.getInstance(this);
        element = gameData.getMap().get(row, col);
        structure = element.getStructure();

        if (structure == null) {
            throw new NullPointerException();
        }

        gridCoords = findViewById(R.id.gridCoords);
        type = findViewById(R.id.structureType);
        name = findViewById(R.id.structureName);
        camera = findViewById(R.id.camera);
        pic = findViewById(R.id.structureImage);

        gridCoords.setText("Location: Row: " + row + " Col: " + col);
        type.setText("Type: " + structure.getLabel());
        name.setText(structure.getName());

        //check if there is a user set image.
        if (element.getImage() == null) {
            pic.setImageResource(structure.getDrawableId());
        } else {
            pic.setImageBitmap(element.getImage());
        }

        //Hasn't taken photo, photo aspect 'cancelled'
        setResult(Activity.RESULT_CANCELED, intent);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayEditPopUp("Please enter a new name", DetailsActivity.this);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(intent, CAMERA_ACTION);

                    intent.putExtra("row", row);
                    intent.putExtra("col", col);
                    //took photo, activity went ok
                    setResult(Activity.RESULT_OK, intent);
                } catch (SecurityException e) {
                    //Couldn't run camera, asking for permission.
                    ActivityCompat.requestPermissions(DetailsActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_ACTION);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTION && resultCode == Activity.RESULT_OK) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            pic.setImageBitmap(thumbnail);
            element.setImage(thumbnail);
        }
    }

    public void setName(String newName) {
        name.setText(newName);
        structure.setName(newName);
        //Required to store updated name in db
        element.setStructure(structure);
    }

    private void displayEditPopUp(String msg, Context context)
    {
        final Dialog popUp = new Dialog(context);
        popUp.setContentView(R.layout.pop_up_edit);

        TextView txt = popUp.findViewById(R.id.text_box);
        final EditText input = popUp.findViewById(R.id.number_input);

        //Default is numerical for the settings Activity, overwrite here to text input
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        txt.setText(msg);

        Button ok = popUp.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //They have clicked to close. Try to set the value in the input
                //Invalid values handled by setValue.
                String result = input.getText().toString();
                popUp.dismiss();
                setName(result);
            }
        });
        popUp.show();
    }

}