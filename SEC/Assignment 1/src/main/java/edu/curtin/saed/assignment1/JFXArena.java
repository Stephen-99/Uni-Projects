package edu.curtin.saed.assignment1;

import javafx.scene.canvas.*;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.io.*;
import java.util.*;

/**
 * A JavaFX GUI element that displays a grid on which you can draw images, text and lines.
 */
public class JFXArena extends Pane
{
    // Represents an image to draw, retrieved as a project resource.
    private static final String ROBO_IMAGE = "droid2.png";
    private static final String WALL_IMAGE = "wall.png";
    private static final String DAMAGED_WALL_IMAGE = "wall2.png";
    private static final String CITADEL_IMAGE = "citadel.png";
    private Image robot1;
    private Image wall1;
    private Image wall2;
    private Image citadel;
    
    private int gridWidth, gridHeight, citadelX, citadelY;
    private Map<Integer, Position> robots;
    private Map<Integer, Wall> walls;

    private double gridSquareSize; // Auto-calculated
    private Canvas canvas; // Used to provide a 'drawing surface'.

    private List<ArenaListener> listeners = null;
    
    /**
     * Creates a new arena object, loading the imaged and initialising a drawing surface.
     */
    public JFXArena(int gridWidth, int gridHeight)
    {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        citadelX = (gridWidth - 1) / 2;
        citadelY = (gridHeight - 1) / 2;
        robots = new HashMap<>();
        walls = new HashMap<>();
        
        //Robot image
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(ROBO_IMAGE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + ROBO_IMAGE);
            }
            robot1 = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + ROBO_IMAGE, e);
        }
        
        //wall image
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(WALL_IMAGE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + WALL_IMAGE);
            }
            wall1 = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + WALL_IMAGE, e);
        }
        
        //damaged wall image
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(DAMAGED_WALL_IMAGE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + DAMAGED_WALL_IMAGE);
            }
            wall2 = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + DAMAGED_WALL_IMAGE, e);
        }

        //citadel image
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(CITADEL_IMAGE))
        {
            if(is == null)
            {
                throw new AssertionError("Cannot find image file " + CITADEL_IMAGE);
            }
            citadel = new Image(is);
        }
        catch(IOException e)
        {
            throw new AssertionError("Cannot load image file " + CITADEL_IMAGE, e);
        }

        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
    }
    
    public void moveRobot(int roboId, double xInc, double yInc)
    {
        Position pos = robots.get(roboId);
        pos.increment(xInc, yInc);
        requestLayout();
    }
    
    public void addRobot(int id, Position roboPos)
    {
        robots.put(id, roboPos);
        requestLayout();
    }

    public void removeRobot(int id)
    {
        robots.remove(id);
        requestLayout();
    }

    public void addWall(int id, Position pos)
    {
        walls.put(id, new Wall(pos, id));
        requestLayout();
    }

    public void wallChanged(int id)
    {
        if (walls.get(id).takeDamage())
        {
            //could successfully take damage
            requestLayout();
        }
        else
        {
            //too damaged to take more damage
            removeWall(id);
        }
    }

    private void removeWall(int id)
    {
        walls.remove(id);
        requestLayout();
    }

    /**
     * Adds a callback for when the user clicks on a grid square within the arena. The callback 
     * (of type ArenaListener) receives the grid (x,y) coordinates as parameters to the 
     * 'squareClicked()' method.
     */
    public void addListener(ArenaListener newListener)
    {
        if(listeners == null)
        {
            listeners = new LinkedList<>();
            setOnMouseClicked(event ->
            {
                int gridX = (int)(event.getX() / gridSquareSize);
                int gridY = (int)(event.getY() / gridSquareSize);
                
                if(gridX < gridWidth && gridY < gridHeight)
                {
                    for(ArenaListener listener : listeners)
                    {   
                        listener.squareClicked(gridX, gridY);
                    }
                }
            });
        }
        listeners.add(newListener);
    }
        
        
    /**
     * This method is called in order to redraw the screen, either because the user is manipulating 
     * the window, OR because you've called 'requestLayout()'.
     *
     * You will need to modify the last part of this method; specifically the sequence of calls to
     * the other 'draw...()' methods. You shouldn't need to modify anything else about it.
     */
    @Override
    public void layoutChildren()
    {
        super.layoutChildren(); 
        GraphicsContext gfx = canvas.getGraphicsContext2D();
        gfx.clearRect(0.0, 0.0, canvas.getWidth(), canvas.getHeight());
        
        // First, calculate how big each grid cell should be, in pixels. (We do need to do this
        // every time we repaint the arena, because the size can change.)
        gridSquareSize = Math.min(
            getWidth() / (double) gridWidth,
            getHeight() / (double) gridHeight);
            
        double arenaPixelWidth = gridWidth * gridSquareSize;
        double arenaPixelHeight = gridHeight * gridSquareSize;
            
            
        // Draw the arena grid lines. This may help for debugging purposes, and just generally
        // to see what's going on.
        gfx.setStroke(Color.DARKGREY);
        gfx.strokeRect(0.0, 0.0, arenaPixelWidth - 1.0, arenaPixelHeight - 1.0); // Outer edge

        for(int gridX = 1; gridX < gridWidth; gridX++) // Internal vertical grid lines
        {
            double x = (double) gridX * gridSquareSize;
            gfx.strokeLine(x, 0.0, x, arenaPixelHeight);
        }
        
        for(int gridY = 1; gridY < gridHeight; gridY++) // Internal horizontal grid lines
        {
            double y = (double) gridY * gridSquareSize;
            gfx.strokeLine(0.0, y, arenaPixelWidth, y);
        }

        drawImage(gfx, citadel, citadelX, citadelY); //Citadel

        for (Map.Entry<Integer, Position> position : robots.entrySet()) // robots
        {
            Position pos = position.getValue();
            drawImage(gfx, robot1, pos.getX(), pos.getY());
            drawLabel(gfx, "robo_" + position.getKey(), pos.getX(), pos.getY());
        }

        for (Map.Entry<Integer, Wall> wallEntry : walls.entrySet()) // walls
        {
            Wall wall = wallEntry.getValue();
            if (wall.isDamaged())
            {
                drawImage(gfx, wall2, wall.getX(), wall.getY());
            }
            else
            {
                drawImage(gfx, wall1, wall.getX(), wall.getY());
            }
        }
    }
    
    /** 
     * Draw an image in a specific grid location. *Only* call this from within layoutChildren(). 
     *
     * Note that the grid location can be fractional, so that (for instance), you can draw an image 
     * at location (3.5,4), and it will appear on the boundary between grid cells (3,4) and (4,4).
     *     
     * You shouldn't need to modify this method.
     */
    private void drawImage(GraphicsContext gfx, Image image, double gridX, double gridY)
    {
        // Get the pixel coordinates representing the centre of where the image is to be drawn. 
        double x = (gridX + 0.5) * gridSquareSize;
        double y = (gridY + 0.5) * gridSquareSize;
        
        // We also need to know how "big" to make the image. The image file has a natural width 
        // and height, but that's not necessarily the size we want to draw it on the screen. We 
        // do, however, want to preserve its aspect ratio.
        double fullSizePixelWidth = robot1.getWidth();
        double fullSizePixelHeight = robot1.getHeight();
        
        double displayedPixelWidth, displayedPixelHeight;
        if(fullSizePixelWidth > fullSizePixelHeight)
        {
            // Here, the image is wider than it is high, so we'll display it such that it's as 
            // wide as a full grid cell, and the height will be set to preserve the aspect 
            // ratio.
            displayedPixelWidth = gridSquareSize;
            displayedPixelHeight = gridSquareSize * fullSizePixelHeight / fullSizePixelWidth;
        }
        else
        {
            // Otherwise, it's the other way around -- full height, and width is set to 
            // preserve the aspect ratio.
            displayedPixelHeight = gridSquareSize;
            displayedPixelWidth = gridSquareSize * fullSizePixelWidth / fullSizePixelHeight;
        }

        // Actually put the image on the screen.
        gfx.drawImage(image,
            x - displayedPixelWidth / 2.0,  // Top-left pixel coordinates.
            y - displayedPixelHeight / 2.0, 
            displayedPixelWidth,              // Size of displayed image.
            displayedPixelHeight);
    }
    
    
    /**
     * Displays a string of text underneath a specific grid location. *Only* call this from within 
     * layoutChildren(). 
     *     
     * You shouldn't need to modify this method.
     */
    private void drawLabel(GraphicsContext gfx, String label, double gridX, double gridY)
    {
        gfx.setTextAlign(TextAlignment.CENTER);
        gfx.setTextBaseline(VPos.TOP);
        gfx.setStroke(Color.BLUE);
        gfx.strokeText(label, (gridX + 0.5) * gridSquareSize, (gridY + 1.0) * gridSquareSize);
    }

    //NOTE: removed drawLine() as it was unused.
}
