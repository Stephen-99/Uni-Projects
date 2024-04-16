package com.example.assignment;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import java.util.List;

/*
This is the question selection activity, which allows the user to select a question from a scrollable selection
 */
public class QuestionSelectionActivity extends ScrollableSelection {

    private List<Question> questions;
    private FragmentManager fm = getSupportFragmentManager();
    private Flag flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

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
            /*Retrieving flag, and hence questions, from parent activity
            It is passed using an index so it references the same flag that is in the singleton, so
            that updates to any questions will be reflected across activities.*/
            int flagIndx = intent.getIntExtra("flag", -9);
            flag = QuizData.getInstance().getFlagData().getFlagList().get(flagIndx);
            questions = flag.getQuestionList();
            returnVal = (List<Object>)(Object)questions;
        }

        return returnVal;
    }

    //Question clicked, start Answer Activity
    public void onClick(Object itemClicked)
    {
        Question question = (Question)itemClicked;

        Intent intent = new Intent(QuestionSelectionActivity.this, AnswerQuestionActivity.class);

        //Flag also has to be sent so question can be accessed via indices
        intent.putExtra("question", flag.getQuestionList().indexOf(question));
        intent.putExtra("flag", QuizData.getInstance().getFlagData().getFlagList().indexOf(flag));

        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StatusBarFragment statusBar = (StatusBarFragment)fm.findFragmentById(R.id.status_bar);

        //RESULT_OK, went ok, some data has been updated. Either question answered or questions value increased
        if (resultCode == RESULT_OK)
        {
            statusBar.updateStatusBar();
            ((SelectionFragment)fm.findFragmentById(R.id.selection_selector)).notifyDataChanged();
        }
        //Special question successfully answered, need to update points and start special activity
        else if ( requestCode == 2 && resultCode == QuizData.SPECIALANSWERED)
        {
            statusBar.updateStatusBar();
            ((SelectionFragment)fm.findFragmentById(R.id.selection_selector)).notifyDataChanged();
            Intent intent = new Intent(QuestionSelectionActivity.this, SpecialFlagSelectionActivity.class);
            startActivityForResult(intent, 3);
        }
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