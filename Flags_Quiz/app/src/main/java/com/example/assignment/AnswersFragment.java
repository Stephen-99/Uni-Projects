package com.example.assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class AnswersFragment extends Fragment {

    private AnswerActivity activity;
    private Question question;
    private int numAns;

    private ArrayList<Button> buttons = new ArrayList<>();

    private Button a1, a2, a3, a4, answerBtn;
    private TextView questionView;

    public AnswersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_answers, container, false);

        activity = (AnswerActivity)getActivity();

        questionView = view.findViewById(R.id.question);
        a1 = view.findViewById(R.id.answer1);
        a2 = view.findViewById(R.id.answer2);
        a3 = view.findViewById(R.id.answer3);
        a4 = view.findViewById(R.id.answer4);

        setUp();

        return view;
    }

    //Randomly allocate the answers to buttons
    private void setAnswers()
    {
        Random rand = new Random();
        Button curButton;
        int num = rand.nextInt(numAns);

        curButton = buttons.get(num);
        curButton.setText(question.getAnswer());
        answerBtn = curButton;
        buttons.remove(curButton);

        for (int ii = 1; ii < numAns; ii++)
        {
            num = rand.nextInt(numAns-ii);
            buttons.remove(num).setText(question.getChoices().get(ii-1));
        }
    }

    private void incorrectAnswer()
    {
        activity.updatePoints(-question.getPenalty());
        QuizData.displayPopUp("Incorrect!\nAnswer was: " + question.getAnswer(), getContext());
        questionCompleted();
    }

    private void correctAnswer()
    {
        activity.updatePoints(question.getPoints());
        QuizData.displayPopUp("Correct!", getContext());

        if (question.isSpecial()) {
            activity.callSpecial();
        }

        questionCompleted();
    }

    private void questionCompleted()
    {
        a1.setClickable(false);
        a2.setClickable(false);
        a3.setClickable(false);
        a4.setClickable(false);

        question.setAnswered();
        activity.updateSelection();

    }

    //sets up all text to display as well as on-click listeners for the buttons
    private void setUp()
    {
        question = activity.getQuestion();
        numAns = activity.getNumAnswers();

        //If this method gets called to refresh the fragment, the list needs to be reset
        buttons.clear();
        buttons.add(a1);
        buttons.add(a2);
        buttons.add(a3);
        buttons.add(a4);

        if (question != null)
        {
            questionView.setText(question.getQuestion());
        }

        //sets visibility of buttons
        switch (numAns)
        {
            case 0:
                a1.setVisibility(View.GONE);
                a2.setVisibility(View.GONE);
                a3.setVisibility(View.GONE);
                a4.setVisibility(View.GONE);
                break;
            case 2:
                a1.setVisibility(View.VISIBLE);
                a2.setVisibility(View.VISIBLE);
                a3.setVisibility(View.GONE);
                a4.setVisibility(View.GONE);
                break;
            case 3:
                a1.setVisibility(View.VISIBLE);
                a2.setVisibility(View.VISIBLE);
                a3.setVisibility(View.VISIBLE);
                a4.setVisibility(View.GONE);
                break;
            case 4:
                a1.setVisibility(View.VISIBLE);
                a2.setVisibility(View.VISIBLE);
                a3.setVisibility(View.VISIBLE);
                a4.setVisibility(View.VISIBLE);
        }

        if (numAns != 0) {
            setAnswers();
        }

        //OnClick listeners for the buttons.
        {
            a1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (a1.equals(answerBtn)) {
                        correctAnswer();
                    } else {
                        incorrectAnswer();
                    }
                }
            });
        }

        {
            a2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (a2.equals(answerBtn)) {
                        correctAnswer();
                    } else {
                        incorrectAnswer();
                    }
                }
            });
        }

        {
            a3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (a3.equals(answerBtn)) {
                        correctAnswer();
                    } else {
                        incorrectAnswer();
                    }
                }
            });
        }

        {
            a4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (a4.equals(answerBtn)) {
                        correctAnswer();
                    } else {
                        incorrectAnswer();
                    }
                }
            });
        }
    }

    public void refresh()
    {
        setUp();
    }

}