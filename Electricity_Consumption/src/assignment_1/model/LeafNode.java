package assignment_1.model;

import java.util.*;

//Represents a terminal node in the network (has no children)
public class LeafNode extends Node 
{
    private Map<String, Double> powerConsumption;

    public LeafNode(String name, Node parent, Map<String, Double> powerConsumption)
    {
        super(name, parent);
        this.powerConsumption = powerConsumption;
    }

    /*Retrieves powerusage from the map for each category and formats it into a string
    in accordance with the file format*/
    @Override
    protected String getPowerUsageString() {
        String returnStr = "";
        for (Map.Entry<String, Double> entry : powerConsumption.entrySet())
        {
            double value = entry.getValue();
            if (value != 0)
            {
                String category = entry.getKey();
                returnStr += "," + category + "=" + value;
            }
        }
        return returnStr;
    }

    //Retrieves the power consumption from a given category
    @Override
    public double getPowerConsumption(String category) {
        Double consumption = powerConsumption.get(category);

        //When there is no entry for the category, power consumption should be 0
        if (consumption == null) {
            consumption = 0.0;
        }

        return consumption;
    }

	@Override
	public boolean hasChildren() {
		return false;
	}
}