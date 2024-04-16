package assignment_1.controller;

import java.util.*;

import assignment_1.model.*;
import assignment_1.view.*;

public class Controller 
{
    //Strores all possible input and output options
    private Map<String, CreateNetwork> inputOptions;
    private Map<String, OutputNetwork> outputOptions;
    
    //The selected input and output options:
    private CreateNetwork inOption; 
    private OutputNetwork outOption;

    //The provided filenames (empty if not reading or writing from/to a file)
    private String inFileName; 
    private String outFileName;

    //represents the commandline arguments
    private String[] args;

    private static final String USAGEINFO = "\tTo run the program commandline arguments must be specified.\n\tSpecifiy either -r "
    + "(Read from file) or -g (auto-generate) as an input \n\toption Specifiy either -w (write to file) or -d (display) "
    + "as an output \n\toption. If either -w or -r is used, immediately follow it with a valid \n\tfile name."
    + "\n\tFor example the program could be run like this: \n\tjava -jar dist/Assignmen1.jar -r filetoReadFrom.txt -d";

    public Controller(String[] args){
        this.args = args;
        inputOptions = new HashMap<String, CreateNetwork>();
        outputOptions = new HashMap<String, OutputNetwork>();

        inputOptions.put("-g", new GenerateNetwork());
        inputOptions.put("-r", new ReadNetwork());
        outputOptions.put("-d", new DisplayNetwork());
        outputOptions.put("-w", new WriteNetwork());

        inFileName = "";
        outFileName = "";
    }

    public void run()
    {
        if (args.length < 2 || args.length > 4) {
            UserInterface.displayError("Invalid number of arguments. See usage: \n\n" + USAGEINFO);
        } else {
            //Set to null so we know if it hasn't been initialised
            ElectricityNetwork network = null;
            try {
                validateArgs();
                network = inOption.createNetwork(inFileName);

                //These exceptions come from validateArgs()
            } catch (InvalidValueException e) {
                UserInterface.displayError("Provided arguments were invalid. See usage: \n\n" + USAGEINFO);
            } catch (IndexOutOfBoundsException e) { 
                //Invalid number of arguments, can happen when a filname is expected but not given.
                UserInterface.displayError("Provided arguments were invalid. \nMake sure to include a filename "
                + "where relavent. See usage: \n\n" + USAGEINFO);
            }

            if (network != null)
            {
                outOption.outputNetwork(network, outFileName);
            }
        }
    }

    /*In validateting the commandline arguments will either throw an InvalidValueException or IndexOutOfBoundsError,
    or it will initialise the class fields inOption, outOption, inFileName, outFileName, according to the provided 
    commandline arguments*/
    private void validateArgs() throws InvalidValueException
    {
        //Checking first provided option
        int argsIndex = 0;
        inOption = inputOptions.get(args[argsIndex]);
        if (inOption == null) { //Not found in input options
            outOption = outputOptions.get(args[argsIndex]);
            if (outOption == null) { //not found in outputOptions
                //first option is invalid!
                throw new InvalidValueException();
            } 

            //First arg specifies a valid output option
            //Index will be moved to the next valid location
            argsIndex = requiresFileCheck(outOption, argsIndex);
            
            //Second provided option must be an input option since first was an output option
            inOption = inputOptions.get(args[argsIndex]);
            if (inOption == null) { //not a valid input option
                throw new InvalidValueException();
            }
            
            //inOption is valid, both are valid, we don't need the index anymore
            requiresFileCheck(inOption, argsIndex);
        } else {
            //first argument was a valid input option
            argsIndex = requiresFileCheck(inOption, argsIndex);

            //Second option must be a valid output option
            outOption = outputOptions.get(args[argsIndex]);
            if (outOption == null) { //not a valid output option
                throw new InvalidValueException();
            }

            //valid output option provided
            requiresFileCheck(outOption, argsIndex);
        }
    }

    /*Method overloading to initate the filename for either option type 
    Will return the argsIndex in the appropriate location and overwrite the 
    filename if required*/
    private int requiresFileCheck(CreateNetwork option, int argsIndex) 
    {
        argsIndex++;
            if (option.requiresFile()) { 
                inFileName = args[argsIndex];
                argsIndex++; //Increment index to move past filename
            }
        return argsIndex;
    }

    private int requiresFileCheck(OutputNetwork option, int argsIndex) 
    {
        argsIndex++;
            if (option.requiresFile()) { 
                outFileName = args[argsIndex];
                argsIndex++; //Increment index to move past filename
            }
        return argsIndex;
    }
}
