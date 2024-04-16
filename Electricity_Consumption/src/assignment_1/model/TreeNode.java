package assignment_1.model;

import java.util.*;

/*Represents a 'parent' node in the network, can have multiple 
'child' nodes of either type TreeNode or LeafNode*/
public class TreeNode extends Node{
    private Map<String, Node> children;

    public TreeNode(String name, Node parent)
    {
        super(name, parent);
        children = new HashMap<String, Node>();
    }

    public void addChild(String name, Node child)
    {
        children.put(name, child);
    }

    @Override
    protected String getPowerUsageString() {
        //Only leaf nodes should write a powerUsageString
        return "";
    }

    //adds up all the power from its children, (who may need to do the same)
    @Override
    public double getPowerConsumption(String category) {
        double result = 0;
        for (Map.Entry<String, Node> entry : children.entrySet()){
            result += entry.getValue().getPowerConsumption(category);
        }
        return result;
    }

    @Override
    public boolean hasChildren() {
        return true;
    }
    
    public Map<String, Node> getChildren()
    {
        return Collections.unmodifiableMap(children);
    }
}
