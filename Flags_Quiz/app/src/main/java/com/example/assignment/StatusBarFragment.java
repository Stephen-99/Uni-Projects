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


public class StatusBarFragment extends Fragment {

    private QuizData data = QuizData.getInstance();

    private TextView totalPoints, targetPoints, wonOrLost;
    private Button previous;

    public StatusBarFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_status_bar, container, false);

        totalPoints = view.findViewById(R.id.total_points);
        targetPoints = view.findViewById(R.id.target_points);
        wonOrLost = view.findViewById(R.id.won_or_lost);
        previous = view.findViewById(R.id.previous);

        //Rendering won_or_lost text to be invisible until the game is either won or lost.
        wonOrLost.setVisibility(View.INVISIBLE);

        updateStatusBar();

        final Activity activity = getActivity();

        //No previous button in flag Selection activity
        if (activity instanceof FlagSelectionActivity)
        {
            previous.setVisibility(View.GONE);
        }
        else
        {
            previous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activity instanceof AnswerQuestionActivity)
                    {
                        AnswerQuestionActivity answerActivity = (AnswerQuestionActivity) activity;
                        if (!answerActivity.getResultSet())
                        {
                            Intent intent = answerActivity.getIntent();

                            //Cancelled result, means no question answered, no need for parent activity to update
                            answerActivity.setResult(Activity.RESULT_CANCELED, intent);
                        }
                    }
                    activity.finish();
                }
            });
        }

        return view;
    }

    public void updateStatusBar()
    {
        totalPoints.setText("Total points: " + data.getTotalScore());
        targetPoints.setText("Target: " + data.getTargetScore());

        if (data.getWon())
        {
            wonOrLost.setVisibility(View.VISIBLE);
            wonOrLost.setText("Quiz Won!");
            previous.setVisibility(View.INVISIBLE);
            QuizData.displayPopUp("Congratulations, you won the quiz!", getContext());
        }
        else if (data.getLost())
        {
            wonOrLost.setVisibility(View.VISIBLE);
            wonOrLost.setText("Game over  ):");
            previous.setVisibility(View.INVISIBLE);
            QuizData.displayPopUp("Game Over!\nYou'll do better next time!", getContext());
        }
    }
}