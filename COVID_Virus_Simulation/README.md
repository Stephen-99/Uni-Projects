# COVID_Virus_Simulation
 A graph based simulation simulating the effects of the virus

The command to run a single simulation is:
>python3 HealthSim.py -s numPpl %inf trans_rate recov_rate death_rate car_rate car_inf_rate gov_prec

Or if using a file to load the network:
>python3 HealthSim.py -s netFile trans_rate recov_rate death_rate car_rate car_inf_rate gov_prec

More information can be found by running the simulation without parameters as shown below:
>python3 HealthSim.py

The parameter sweep can be run as follows:
>bash paramSweep.sh

More information on how to run the simulation cn be found in the Methodology section of the Report

For more information on how the simulation works and how the code hangs together, view Documentation.pdf
