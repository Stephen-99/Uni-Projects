package com.example.assignment;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

/*
This is the singleton class
It stores the single instance of the Flag and hence question data, as well as the score and whether
the game is won or lost
 */
public class QuizData {

    public static final int  SPECIALANSWERED = 99;

    private int targetScore;
    private int totalScore;
    private boolean won;
    private boolean lost;
    private static FlagData flagData;

    private static QuizData instance = null;

    private Random rand = new Random();

    public static QuizData getInstance()
    {
        if (instance == null)
        {
            instance = new QuizData();
        }
        return instance;
    }

    public QuizData()

    {
        won = false;
        lost = false;
        //setting starting score 1-100
        totalScore = rand.nextInt(100) + 1;

        //setting target score 50-200 above totalScore
        targetScore = rand.nextInt(151) + 50 + totalScore;
        flagData = new FlagData();
    }

    public void quizWon()
    {
        won = true;
    }

    public void quizLost()
    {
        lost = true;
    }

    //add points to the score. Use -ve amount to subtract
    public void updateScore(int amount)
    {
        totalScore += amount;
        if (totalScore <= 0)
        {
            totalScore = 0;
            quizLost();
        }
        else if (totalScore >= targetScore)
        {
            quizWon();
        }
    }


    //Getters
    public int getTotalScore() {
        return totalScore;
    }

    public int getTargetScore() {
        return targetScore;
    }

    public boolean getWon()
    {
        return won;
    }

    public boolean getLost()
    {
        return lost;
    }

    public FlagData getFlagData() {
        return flagData;
    }

    //This is a useful method put here for central access for all other classes
    //Displays a simple popUp with provided text
    public static void displayPopUp(String msg, Context context)
    {
        final Dialog popUp = new Dialog(context);
        popUp.setContentView(R.layout.pop_up);

        TextView txt = popUp.findViewById(R.id.text_box);
        txt.setText(msg);

        Button ok = popUp.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUp.dismiss();
            }
        });
        popUp.show();
    }

}

