package assignment_1;

import assignment_1.controller.*;
import assignment_1.view.UserInterface;

/*This is the main class, the starting point of the program.
It will catch any unexpected Exceptions*/
public class Assignment_1 {
    public static void main(String[] args)
    {
        try{
            Controller controller = new Controller(args);
            controller.run();
        } catch(Exception e) {
            UserInterface.displayError("Unexcpected Error: \n\t" + e.getMessage());
            e.printStackTrace();
        }

    }
}
