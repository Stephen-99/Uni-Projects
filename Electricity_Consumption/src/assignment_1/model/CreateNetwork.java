package assignment_1.model;

//All implementations must create an ElectricityNetwork
public interface CreateNetwork extends ProcessNetwork {
    public ElectricityNetwork createNetwork(String filename);
}
