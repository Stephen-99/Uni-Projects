package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

//Combined question selection and answer activity into 1 tablet activity
public class TabletQuestionActivity extends ScrollableSelection implements AnswerActivity {

    private FragmentManager fm = getSupportFragmentManager();

    //Question selector vars
    private List<Question> questions;
    private Flag flag;

    //Answer question vars
    private QuizData data = QuizData.getInstance();
    private Question question = null;
    private int numAnswers = 0;
    private boolean resultSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablet_question);
    }


    //Question selector stuff
    @Override
    public String getScrollType() {
        return "question";
    }

    public List<Object> getData()
    {
        List<Object> returnVal = null;

        Intent intent = getIntent();
        if (intent.hasExtra("flag"))
        {
            int flagIndx = intent.getIntExtra("flag", -9);
            flag = QuizData.getInstance().getFlagData().getFlagList().get(flagIndx);
            questions = flag.getQuestionList();
            returnVal = (List<Object>)(Object)questions;
        }

        return returnVal;
    }

    @Override
    public void onClick(Object itemClicked) {
        question = (Question)itemClicked;

        //Number of choices, +1 for the actual answer
        numAnswers = question.getChoices().size() + 1;

        AnswersFragment ansFrag = (AnswersFragment)fm.findFragmentById(R.id.answer_selector);

        //data for fragment is set now, so refresh it to show this change.
        ansFrag.refresh();
    }

    @Override
    public void rotate(boolean horizontal) {
        ((SelectionFragment)fm.findFragmentById(R.id.question_selector)).rotate(horizontal);
    }

    @Override
    public void changeItemSpanCount(int newSpanCount) {
        ((SelectionFragment)fm.findFragmentById(R.id.question_selector)).changeSpanCount(newSpanCount);
    }




    //Answer question stuff
    @Override
    public Question getQuestion() {
        return question;
    }

    @Override
    public int getNumAnswers() {
        return numAnswers;
    }

    @Override
    public void updatePoints(int amount) {
        data.updateScore(amount);
        StatusBarFragment statusBar = (StatusBarFragment) fm.findFragmentById(R.id.status_bar);
        statusBar.updateStatusBar();
    }

    @Override
    public boolean getResultSet() {
        return resultSet;
    }

    @Override
    public void resultSet() {
        resultSet = true;
    }

    @Override
    public void updateSelection() {
        ((SelectionFragment)fm.findFragmentById(R.id.question_selector)).notifyDataChanged();
    }

    @Override
    public void callSpecial() {
        Intent intent = new Intent(TabletQuestionActivity.this, SpecialFlagSelectionActivity.class);
        startActivityForResult(intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Special activity returning, update questions in case current flag was selected
        if (requestCode == 4)
        {
            updateSelection();
        }
    }
}