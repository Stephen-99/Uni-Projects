package edu.curtin.saed.assignment1;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ScoreManager 
{
    //using a property for updating a value in a label was suggested here: https://stackoverflow.com/questions/13227809/displaying-changing-values-in-javafx-label 
    public StringProperty scoreText;
    private int score;
    private Thread scoreThread;
    
    public ScoreManager()
    {
        score = 0;
        scoreText = new SimpleStringProperty();
        setScoreText();
        scoreThread = new Thread(this::updateScoreEverySecond, "scoreThread");
        scoreThread.start();
    }

    public void robotDied()
    {
        score += 100;
        setScoreText();
    }

    public int finaliseScore()
    {
        scoreThread.interrupt();
        return score;
    }

    private void updateScoreEverySecond()
    {
        try
        {
            while (true)
            {
                Thread.sleep(1000);
                incrementScore();
            }
        }
        catch (InterruptedException e)
        {
            //Time for the thread to finish :D
        }
    }

    private void incrementScore()
    {
        score += 10;
        setScoreText();
    }

    private void setScoreText()
    {
        //This triggers a GUI update so needs to happen on the GUI thread.
        Platform.runLater(() ->
        {
            scoreText.set("Score: " + score);    
        });
    }
}
