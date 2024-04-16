package edu.curtin.saed.assignment1;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application 
{   
    //These are constants and traditionally, convention is to have constants in all CAPS
    @SuppressWarnings("PMD")
    public final int GRIDWIDTH = 9;
    @SuppressWarnings("PMD")
    public final int GRIDHEIGHT = 9;
    public static void main(String[] args) 
    {
        launch();        
    }
    
    @Override
    public void start(Stage stage) 
    {
        TextArea logger = new TextArea();
        ScoreManager scoreManager = new ScoreManager();
        JFXArena arena = new JFXArena(GRIDWIDTH, GRIDHEIGHT);
        MovementManager movementManager = new MovementManager();
        GridManager gridManager = new GridManager(GRIDWIDTH, GRIDHEIGHT, arena, movementManager, scoreManager, stage, logger);
        RobotCreator roboCreator = new RobotCreator(gridManager);
        WallManager wallManager = new WallManager(gridManager, logger);
        gridManager.setupGrid(wallManager);
    
        Alert endScreen = new Alert(AlertType.INFORMATION);

        //If they close the window, tell all the threads to end and show the final score
        stage.setOnCloseRequest(eventHandler -> 
        {
            roboCreator.stopCreating();
            movementManager.shutdown();
            gridManager.killRobots();
            wallManager.noMoreWalls();
            int score = scoreManager.finaliseScore();

            endScreen.setContentText("Game over!\nYour score was: " + score);
            endScreen.show();
        });

        arena.addListener((x, y) ->
        {
            //The create wall method might block which we don't want to happen in the gui so we run it in a new thread.
            new Thread(wallManager.new WallCreator(x, y), "Create wall thread").start();
        });

        stage.setTitle("Robot 6 Siege");
        Label scoreLabel = new Label("Score: 999");
        Label wallsLabel = new Label("Number of walls in queue: 99");
        ToolBar toolbar = new ToolBar();
    
        //This is for alligning the label a bit away from the score
        Platform.runLater(() ->
        {
            wallsLabel.setMinWidth(toolbar.getWidth()/3);
            wallsLabel.setAlignment(Pos.CENTER_RIGHT);
        });
        
        //binding the labels in order to update the labels as the properties change
        scoreLabel.textProperty().bind(scoreManager.scoreText);
        wallsLabel.textProperty().bind(wallManager.wallsInQueueText);

        toolbar.getItems().addAll(scoreLabel, wallsLabel);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(arena, logger);
        arena.setMinWidth(300.0);
        
        BorderPane contentPane = new BorderPane();
        contentPane.setTop(toolbar);
        contentPane.setCenter(splitPane);
        
        Scene scene = new Scene(contentPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }
}
