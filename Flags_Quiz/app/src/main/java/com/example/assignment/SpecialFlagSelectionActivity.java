package com.example.assignment;

import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

/*
This uses same layout as FlagSelectionActivity, but this is used to handle the return of a a special question
They should really both inherit from an abstract class...
 */
public class SpecialFlagSelectionActivity extends ScrollableSelection{

    private FragmentManager fm = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        QuizData.displayPopUp("Please select a flag to increase the points of each question for that Country", this);
    }

    @Override
    public String getScrollType() {
        return "flag";
    }

    @Override
    public List<Object> getData() {
        return (List<Object>) (Object)QuizData.getInstance().getFlagData().getFlagList();
    }

    @Override
    public void onClick(Object itemClicked) {
        Flag flag = (Flag)itemClicked;
        flag.increaseQuestionsValue();
        Intent intent = getIntent();
        this.setResult(RESULT_OK, intent);
        this.finish();
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