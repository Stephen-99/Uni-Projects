package assignment_1.model;

import java.io.*;
import java.util.*;

import assignment_1.view.UserInterface;

//Reads an imput file and uses it to create a network
public class ReadNetwork implements CreateNetwork {

    //Using a class constant for better readability and easier access to change the value
    private static final boolean REQUIRESFILE = true;
    private Map<String, Node> nodes;

    public ReadNetwork() {
        nodes = new HashMap<String, Node>();
    }

    public boolean requiresFile() {
        return REQUIRESFILE;
    }

    @Override
    public ElectricityNetwork createNetwork(String filename) {
        String cityName = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {    
    
            //Crreating root node
            String line = reader.readLine();
            if (line == null) {
                throw new InvalidFileFormatException("File was empty!");
            }
            Node curNode = processRootLine(line);
            cityName = curNode.getName();
            nodes.put(curNode.getName(), curNode);

            line = reader.readLine();

            //Creating the rest of the nodes
            while(line != null)
            {   
                curNode = processLine(line);
                nodes.put(curNode.getName(), curNode);
                line = reader.readLine();
            }

            //Use all the created nodes to create a network
            return new ElectricityNetwork(nodes, cityName);

        } catch (IOException e) {
            UserInterface.displayError("Could not read the file provided");
        } catch (InvalidFileFormatException e) {
            UserInterface.displayError(e.getMessage());
        }

        /*This will only trigger if an exception is triggered in which case the 
        network will be null so the calling code knows the network was not initialised*/
        return null;
    }

    
    private Node processRootLine(String line) throws InvalidFileFormatException {
        Node returnNode = null;
        String[] parts = line.split(",");

        //rooot node needs name, empty or null parent and up to 8 categories (if its the only node)
        if (parts.length < 1 || parts.length > 10) {
            throw new InvalidFileFormatException("Inncorrect number of items on the first line");
        }
        
        //Creating a treeNode
        if (parts.length == 1) {
            returnNode = new TreeNode(parts[0], null);
        } else if (parts.length == 2) {
            //If the second parameter doesn't represent an empty parent, than it is an invalid format
            if (!(parts[1] == null || parts[1].equals("null") || parts[1].equals("")))
            {
                throw new InvalidFileFormatException("Invalid parent for the city, should be null");
            } else {
                returnNode = new TreeNode(parts[0], null);
            }
        } else {
            //Creating a leaf node, same as b4, second parameter must be empty (or null)
            if (!(parts[1] == null || parts[1].equals("null") || parts[1].equals("")))
            {
                throw new InvalidFileFormatException("Invalid parent for the city, should be null");
            }
            returnNode = new LeafNode(parts[0], null, processPowerConsumption(parts, line));
        }
        return returnNode;
    }

    //Check the line meets appropriate format and crete the appropriate node
    private Node processLine(String line) throws InvalidFileFormatException {
        TreeNode parent;
        Node returnNode = null;
        String[] parts = line.split(",");

        //needs name, parent and up to 8 categories (if leaf node) otherwise name and parent
        if (parts.length < 2 || parts.length > 10) {
            throw new InvalidFileFormatException("Inncorrect number of items for line: " + line);
        }

        //Try find the parent in the nodes that already have been added
        parent = (TreeNode)nodes.get(parts[1]);
        if (parent == null) {
            throw new InvalidFileFormatException("Missing or invalid parent for line: " + line);
        }

        //check if a node with that name already exists
        if (nodes.get(parts[0]) != null){
            throw new InvalidFileFormatException("Tried to add values with duplicate names! This line contains the duplicate: " + line);
        }

        if (parts.length == 2) {    
            returnNode = new TreeNode(parts[0], parent);
        } else {
            returnNode = new LeafNode(parts[0], parent, processPowerConsumption(parts, line));
        }

        //Add node to parents children
        parent.addChild(returnNode.getName(), returnNode);

        return returnNode;
    }

    //Processes the part of a line for a leaf node that represents the power consumption
    private Map<String, Double> processPowerConsumption(String[] parts, String line) throws InvalidFileFormatException
    {
        Map<String, Double> powerConsumption = new HashMap<String, Double>();
            String[] category;

            //Iterate over each category provided and check it matches one of the defined 
            //categories before adding it to the map
            for (int ii = 2; ii < parts.length; ii++)
            {
                category = parts[ii].split("=");
                if (ElectricityNetwork.CATEGORIES.get(category[0]) == null) {
                    throw new InvalidFileFormatException("Inncorrect category for power consumption. " 
                    + category[0] + " is not a valid category. \nFound on line: " + line);
                }
                powerConsumption.put(category[0], Double.parseDouble(category[1]));
            }
        return powerConsumption;
    }
}
