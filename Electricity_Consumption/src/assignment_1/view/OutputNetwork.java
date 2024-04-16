package assignment_1.view;

import assignment_1.model.*;

//Implementations should output the network in some form
public interface OutputNetwork extends ProcessNetwork {
    public void outputNetwork(ElectricityNetwork network, String fileName);
}