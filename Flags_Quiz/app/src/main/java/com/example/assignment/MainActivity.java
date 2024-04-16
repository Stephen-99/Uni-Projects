package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
The main, starting activity of the app
Shows the initial and target score as well as the logo (more like a title in this case...)
 */
public class MainActivity extends AppCompatActivity {

    private QuizData data;
    private int startPoints, targetPoints;

    private TextView logo, target, start;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = QuizData.getInstance();
        startPoints = data.getTotalScore();
        targetPoints = data.getTargetScore();

        logo = findViewById(R.id.logo);
        target = findViewById(R.id.target_points);
        start = findViewById(R.id.start_points);
        startBtn = findViewById(R.id.start_btn);

        logo.setText(R.string.logo);
        start.setText("Starting score: " + startPoints);
        target.setText("Target score: " + targetPoints);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FlagSelectionActivity.class));
            }
        });

    }
}