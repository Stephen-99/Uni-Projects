package assignment_1.model;

//Represents a node in the elctricity network
public abstract class Node {
    private String name;
    private Node parentNode;

    protected Node(String name, Node parent)
    {
        this.name = name;
        this.parentNode = parent;
    }

    public String getName() {
        return this.name;
    }

    public Node getParentNode() {
        return this.parentNode;
    }

    //Returns a string matching the file format
    @Override
    public String toString() 
    {
        String returnStr = name;
        if (parentNode != null) {
            returnStr += "," + parentNode.getName();
        }
        returnStr += getPowerUsageString();
        return returnStr;
    }

    public abstract double getPowerConsumption(String category);

    public abstract boolean hasChildren();

    protected abstract String getPowerUsageString();
    
}

