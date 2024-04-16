package com.example.assignment;

import java.util.List;

/*
Stores all the necessary information about each question
 */
public class Question{
    private int points, penalty;
    private boolean isSpecial, answered;
    private String label, question, answer;
    private List<String> choices;

    public Question(int points, int penalty, boolean isSpecial, String label, String question, String answer, List<String> choices)
    {
        this.points = points;
        this.penalty = penalty;
        this.isSpecial = isSpecial;
        this.label = label;
        this.question = question;
        this.answer = answer;
        this.choices = choices;
        answered = false;
    }

    public int getPoints() {
        return points;
    }

    public int getPenalty() {
        return penalty;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public String getLabel() {
        return label;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void increasePoints()
    {
        points += 10;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered() {
        answered = true;
    }
}
