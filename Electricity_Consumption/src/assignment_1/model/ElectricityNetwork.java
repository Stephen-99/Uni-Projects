package assignment_1.model;

import java.util.*;

//This class stores the elctricity network
public class ElectricityNetwork implements Iterable<Node>
{
    private Map<String, Node> network;

    //Allows us to travesrse the network from the root
    private String city;

    //Maps the abbreviations to their appropriate categories:
    public static final Map<String, String> CATEGORIES = initCategories();

    public ElectricityNetwork(Map<String, Node> network, String city)
    {
        this.network = network;
        this.city = city;
    }

    //This initialises the map and makes it unmodifiable so it can act as a constant
    private static Map<String, String> initCategories()
    {
        Map<String, String> categories = new HashMap<String, String>();
        categories.put("dm", "Weekday morning");
        categories.put("da", "Weekday afternoon");
        categories.put("de", "Weekday evening");
        categories.put("em", "Weekend morning");
        categories.put("ea", "Weekend afternoon");
        categories.put("ee", "Weekend evening");
        categories.put("h", "Heatwave");
        categories.put("s", "Special event");

        return Collections.unmodifiableMap(categories);
    }

    public String getCity() {
        return city;
    }

    @Override
    public Iterator<Node> iterator() {
        return new PowerNetworkIterator(network.get(city));
    }

    /*Implementing an iterator to comlete roughly a pre-order traversal of the network 
    i.e. child nodes will be added before 'brother/sister' nodes*/
    private class PowerNetworkIterator implements Iterator<Node>
    {
        //Stack allows us to use LIFO (Last In, First Out)
        private Stack<Node> stack;

        public PowerNetworkIterator(Node root) {
            stack = new Stack<Node>();
            stack.add(root);
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = true;
            if (stack.isEmpty()) {
                hasNext = false;
            }
            return hasNext;
        }

        //Will return null if iterator has reached the end of the network
        //Otherwise will return the next node
        @Override
        public Node next() {
            Node curNode = null;
            if (hasNext()) {
                curNode = stack.pop();

                //If a node has children, add them to the stack
                if (curNode.hasChildren())
                {
                    for (Map.Entry<String, Node> entry : ((TreeNode)(curNode)).getChildren().entrySet())
                    {
                        stack.push(entry.getValue());
                    }
                }
            }
            
            return curNode;
        }
        
    }
}