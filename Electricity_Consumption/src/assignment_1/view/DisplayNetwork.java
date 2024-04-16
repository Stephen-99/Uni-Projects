package assignment_1.view;

import java.util.*;

import assignment_1.model.*;

//This will display the network to the screen and will display a table of power usage
public class DisplayNetwork implements OutputNetwork {
    /*NOTE: we are in the view because we need to display the model, but we can't know about the model...
        i.e.  needs to have low-coupling, model may need some methods to represent how to display itself
    */

    //Using a class constant for better readability and easier access to change the value
    private static final boolean REQUIRESFILE = false;

    private static final int MAXCATEGORYLENGTH = 20;

    //Keep track of which nodes we've come accross at each level, so we can determine which 
    //level a child is on
    private List<Map<String, Integer>> treeLevels;


    public boolean requiresFile() 
    {
        return REQUIRESFILE;
    }

    public DisplayNetwork() 
    {
        treeLevels = new ArrayList<Map<String, Integer>>();
    }

    @Override
    public void outputNetwork(ElectricityNetwork network, String fileName) 
    {
        displayTree(network);

        Node root = network.iterator().next();
        displayPowerConsumption(root);
    }

    private void displayTree(ElectricityNetwork network)
    {
        Iterator<Node> itr = network.iterator();

        //Add first level
        treeLevels.add(new HashMap<String, Integer>());
        int curLevel = 0;

        //root node
        Node curNode =  itr.next();
        treeLevels.get(curLevel).put(curNode.getName(), curLevel);
        System.out.println(curNode.getName());

        //add second level
        curLevel++;
        treeLevels.add(new HashMap<String, Integer>());
        
        String displayName;

        //continue while there is still nodes in the tree, iterator returns a parent
        //b4 its corresponding children
        while (itr.hasNext())
        {
            curNode = itr.next();

            //Check if parent is in previous level
            Integer parent = null;
            parent = treeLevels.get(curLevel-1).get(curNode.getParentNode().getName());
            if (parent == null) {
                //Couldn't find parent, we either moved a level deeper or 1 or more levels back
                curLevel = findCurLevel(curLevel, curNode, parent);
            }
            //add the node to the current level
            treeLevels.get(curLevel).put(curNode.getName(), curLevel);

            //4 spaces indentation per level
            displayName = getRepeatedCharacter(curLevel*4, ' ') + curNode.getName();
            System.out.println(displayName);

        }
        System.out.println("");   
    }

    //Adds up the powerConsumption for the network and displays it in a table-like format
    private void displayPowerConsumption(Node root)
    {
        System.out.println("\nPOWER CONSUMPTION:\n\n");
        System.out.println("Category" + getRepeatedCharacter(MAXCATEGORYLENGTH-8, ' ') + ": Consumption");
        System.out.println(getRepeatedCharacter(33, '-'));

        
        String displayString;
        double consumption;

        //For each category, get the consumption, format it and print it
        for (Map.Entry<String, String> entry : ElectricityNetwork.CATEGORIES.entrySet()) {
            consumption = root.getPowerConsumption(entry.getKey());
            consumption = UserInterface.round(consumption, 1);

            displayString = entry.getValue();
            displayString += getRepeatedCharacter(MAXCATEGORYLENGTH-displayString.length(), ' ');
            displayString += ": " + consumption;

            System.out.println(displayString);
        }

    }

    //Checks through previous levels to see where the curNode's parent is
    private int findCurLevel(int curLevel, Node curNode, Integer parent)
    {
        int checkingLevel = curLevel-2;

        while (parent == null && checkingLevel >= 0) {
            parent = treeLevels.get(checkingLevel).get(curNode.getParentNode().getName());
            checkingLevel--;
        }
        if (parent == null) {
            //parent must be in current level, since it wasn't in any previous level.
            //We know this because we can assume a valide network since the network is already created
            curLevel++;
            treeLevels.add(new HashMap<String, Integer>());
        } else {
            //found parent on a previous level
            /*curLevel = checkingLevel+2, since checkingLevel was curLevel-2 and then 
            decremented the number of times it took to find the parent level*/
            curLevel = checkingLevel+2;
        }
        return curLevel;
    }


    //returns a string with the passed character repeated the specified number of times
    public String getRepeatedCharacter(int numRepeats, char character)
    {
        String returnString = "";

        for (int ii = 0; ii < numRepeats-1; ii++) {
            returnString += character;
        }
        return returnString;
    }
}
