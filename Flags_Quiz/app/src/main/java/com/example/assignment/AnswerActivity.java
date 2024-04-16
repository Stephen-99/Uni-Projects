package com.example.assignment;

import android.content.Intent;

/*
This interface is implemented in the two activities that allow the user to answer a question.
The interface allow the answer fragment to be used by both activities, since it doesn't care which
activity uses it as long as it has these methods
 */
public interface AnswerActivity {
    Question getQuestion();
    int getNumAnswers();
    void updatePoints(int amount);

    //Will be implemented in Activity base class
    void setResult(int resultCode, Intent intent);
    Intent getIntent();

    boolean getResultSet();
    void resultSet();
    void updateSelection();
    void callSpecial();
}
