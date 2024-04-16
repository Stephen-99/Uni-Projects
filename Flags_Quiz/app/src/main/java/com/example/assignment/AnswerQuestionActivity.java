package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

/*
This is the activity which allows the user to answer questions (phone only)
 */
public class AnswerQuestionActivity extends AppCompatActivity implements AnswerActivity {

    private QuizData data = QuizData.getInstance();
    private FragmentManager fm;
    private Question question = null;
    private int numAnswers = 0;
    private boolean resultSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setQuestion();
        fm = getSupportFragmentManager();
        setContentView(R.layout.activity_answer_question);

    }

    //retrieves the question to be answered from the parent activity
    private void setQuestion()
    {
        Intent intent = getIntent();
        if (intent.hasExtra("question"))
        {
            /*Using indexes of the objects in the array, so that they reference the same object
             which is inside the singleton, allowing it to be updated across activities
            Using serializable, creates a new object and so doesn't allow this*/
            int flagIndx = intent.getIntExtra("flag", -9);
            Flag flag = QuizData.getInstance().getFlagData().getFlagList().get(flagIndx);
            int questionIndx = intent.getIntExtra("question", -12);
            question = flag.getQuestionList().get(questionIndx);

            //Number of choices + 1 for the real answer
            numAnswers = question.getChoices().size() + 1;
        }
    }

    public Question getQuestion() {
        return question;
    }

    public int getNumAnswers() {
        return numAnswers;
    }

    //update the score, and the status bar to show the updated score
    public void updatePoints(int amount)
    {
        data.updateScore(amount);
        StatusBarFragment statusBar = (StatusBarFragment) fm.findFragmentById(R.id.status_bar);
        statusBar.updateStatusBar();
    }

    public void resultSet()
    {
        resultSet = true;
    }

    public boolean getResultSet()
    {
        return resultSet;
    }

    //Will cause parent activity (questionSelection activity, to update upon finishing this activity
    @Override
    public void updateSelection()
    {
        if (!resultSet) {
            Intent intent = getIntent();
            setResult(Activity.RESULT_OK, intent);
            resultSet();
        }
    }

    //Will cause parent activity to call SpecialFlagActivity upon finishing this activity
    @Override
    public void callSpecial() {
        Intent intent = getIntent();
        setResult(QuizData.SPECIALANSWERED, intent);
        resultSet();
    }
}