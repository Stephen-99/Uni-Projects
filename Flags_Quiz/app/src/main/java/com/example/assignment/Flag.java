package com.example.assignment;

import java.util.List;

/*
Flag object.
Stores a reference to its drawable representation and its list of Questions.
 */
public class Flag{
    private int drawable;
    private  List<Question> questionList;

    public Flag(int drawable, List<Question> questionList)
    {
        this.drawable = drawable;
        this.questionList = questionList;
    }

    public int getDrawable() {
        return drawable;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void increaseQuestionsValue()
    {
        for (Question question: questionList)
        {
            question.increasePoints();
        }
    }
}
