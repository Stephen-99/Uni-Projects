package assignment_1.view;

import java.io.*;
import java.util.Iterator;

import assignment_1.model.*;

/*Doesn't need to know anything about the model and it outputs the model
so it belongs in the view class although the 'view' it creates can only be
accessed outside fo the program (or if its read in on a subsequent run of 
the program)
*/

public class WriteNetwork implements OutputNetwork {
    
    //Using a class constant for better readability and easier access to change the value
    private static final boolean REQUIRESFILE = true;

    @Override
    public void outputNetwork(ElectricityNetwork network, String fileName) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) { 
        
            Iterator<Node> itr = network.iterator();     
        
            //Iterate through each node and print its string representation, which 
            //will be formatted to adhere to the file format
            while (itr.hasNext())
            {
                writer.println(itr.next().toString());
            }
        } catch (IOException e) {
            UserInterface.displayError("Could not write to file " + fileName);
        }

    }
    
    public boolean requiresFile() {
        return REQUIRESFILE;
    }



}
