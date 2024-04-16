package assignment_1.model;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import assignment_1.view.UserInterface;

public class GenerateNetwork implements CreateNetwork {
    
    //Using a class constant for better readability and easier access to change the value
    private static final boolean REQUIRESFILE = false;

    //Used to determine the extents of the random generation
    private static final int MAXTREEDEPTH = 5;
    private static final int MINCHILDNODES = 2;
    private static final int MAXCHILDNODES = 5;

    //Used as names on different levels. May need to add some if MAXTREEDEPTH is increased
    private static final String CITYNAME = "Perth";
    private static final String LVL_2_NAME = "District";
    private static final String LVL_3_NAME = "Suburb";
    private static final String LVL_4_NAME = "Estate";
    private static final String LVL_5_NAME = "House";


    private Map<Integer, String> names;
    private Map<Integer, List<String>> namesLists;
    private int treeDepth;
    private int treeLevel;

    public boolean requiresFile() {
        return REQUIRESFILE;
    }

    @Override
    public ElectricityNetwork createNetwork(String filename) {
        Map<String, Node> nodes = new HashMap<String, Node>();

        names = new HashMap<Integer, String>();
        names.put(2, LVL_2_NAME);
        names.put(3, LVL_3_NAME);
        names.put(4, LVL_4_NAME);
        names.put(5, LVL_5_NAME);

        namesLists = new HashMap<Integer, List<String>>();
        namesLists.put(2, new ArrayList<String>());
        namesLists.put(3, new ArrayList<String>());
        namesLists.put(4, new ArrayList<String>());
        namesLists.put(5, new ArrayList<String>());

        //Generate a random depth. Max limit is exclusive, therefore we need +1
        treeDepth = ThreadLocalRandom.current().nextInt(1, MAXTREEDEPTH + 1);
        generateNames();    

        //If tree only has a root node, it must be a leaf node
        if (treeDepth == 1) {
            Node root = new LeafNode(CITYNAME, null, generatePowerConsumption());
            nodes.put(root.getName(), root);
        } else {
            TreeNode root = new TreeNode(CITYNAME, null);
            nodes.put(root.getName(), root);
            treeLevel = 1;

            generateChildren(0, getNumChildren() ,root);
        }

        return new ElectricityNetwork(nodes, CITYNAME);
    }

    //Recursive method that will create the children for a node as well as their children's children etc.
    private int generateChildren(int numCompleted, int numRequired, TreeNode parent) {
        //Condition to determine if we should keep recursing
        if (numCompleted < numRequired) {
            //We need to create nodes for the deepest level, so they must be leafNodes
            if (treeLevel == treeDepth - 1) {
                Node childNode = new LeafNode(getName(treeLevel+1), parent, generatePowerConsumption());
                parent.addChild(childNode.getName(), childNode);

                //Can't go deeper in the tree, so stay on the same level and create all the leave Nodes
                numCompleted++;
                numCompleted = generateChildren(numCompleted, numRequired, parent);    
            } else {
                //We are not on the deepest level, create a node, and go a level deeper to create its children
                treeLevel++;
                TreeNode childNode = new TreeNode(getName(treeLevel), parent);
                parent.addChild(childNode.getName(), childNode);
                //Don't wont the resulting numCompleted since it pertains to a different level
                generateChildren(0, getNumChildren(), childNode);

                //We hace just come back from creating children, so we return to the correct level and the 
                //node is completed
                treeLevel--;
                numCompleted++;
            }
            //Check if we need to create any more children
            generateChildren(numCompleted, numRequired, parent);
        }
        return numCompleted;
    }

    //Removes a node name from the list at the requested level
    private String getName(int level)
    {
        //Use of remove, means that same name won't be chosen again
        return namesLists.get(level).remove(0);
    }

    //Generating maximum numer of required names, i.e. MAXCHILDNODES^treeLevel names for each level
    private void generateNames()
    {
        String levelName;
        List<String> levelList;
        for (int ii = 2; ii < treeDepth + 1; ii++) {
            levelName = names.get(ii);
            levelList = namesLists.get(ii);
            for (int jj = 0; jj < Math.pow(MAXCHILDNODES, ii-1); jj++) {
                //Basic names, unique because of number at the end
                levelList.add(levelName + jj);
            }
        }
    }

    //Generates a map with random power consumption for each category
    private Map<String, Double> generatePowerConsumption()
    {
        Map<String, Double> consumption = new HashMap<String, Double>();

        for (Map.Entry<String, String> entry : ElectricityNetwork.CATEGORIES.entrySet()){
            double powerUsage = ThreadLocalRandom.current().nextDouble(0.0, 1000.0);
            
            //Formatting to 2 d.p.
            powerUsage = UserInterface.round(powerUsage, 2);

            consumption.put(entry.getKey(), powerUsage);
        }
        return consumption;
    }

    //Generates a random number of children
    private int getNumChildren()
    {
        return ThreadLocalRandom.current().nextInt(MINCHILDNODES, MAXCHILDNODES + 1);
    }
}
