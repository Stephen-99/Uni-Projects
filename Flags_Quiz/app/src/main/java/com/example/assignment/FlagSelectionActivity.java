package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

//Activity for selecting a Flag to answer questions about.
public class FlagSelectionActivity extends ScrollableSelection{

    private QuizData data = QuizData.getInstance();
    private FlagData flagData = data.getFlagData();

    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

    //Previous activity has finished, and likely updated some data, so we need to update our status bar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        StatusBarFragment statusBar = (StatusBarFragment)fm.findFragmentById(R.id.status_bar);
        statusBar.updateStatusBar();
    }

    @Override
    public String getScrollType() {
        return "flag";
    }

    public List<Object> getData()
    {
        return (List<Object>) (Object)flagData.getFlagList();
    }

    //Start the Question selection activity.
    @Override
    public void onClick(Object itemClicked) {
        Flag flag = (Flag)itemClicked;
        Intent intent;

        if (getResources().getBoolean(R.bool.isTablet))
        {
            intent = new Intent(FlagSelectionActivity.this, TabletQuestionActivity.class);
        }
        else
        {
            intent = new Intent(FlagSelectionActivity.this, QuestionSelectionActivity.class);
        }

        //Passing the index of the flag, so that the activity can access the flag, and obtain the relevant questions
        intent.putExtra("flag", flagData.getFlagList().indexOf(flag));
        startActivityForResult(intent, 1);
    }

    @Override
    public void rotate(boolean horizontal) {
        ((SelectionFragment)fm.findFragmentById(R.id.selection_selector)).rotate(horizontal);
    }

    @Override
    public void changeItemSpanCount(int newSpanCount) {
        ((SelectionFragment)fm.findFragmentById(R.id.selection_selector)).changeSpanCount(newSpanCount);
    }
}