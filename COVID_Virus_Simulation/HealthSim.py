####
# HealthSim.py
#
# Date created:     27/05/2020
# Date last edited: 01/06/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This is the starting point of the application. It takes the command line parameters and calls
#   the appropriate fucntions
#    
####

from CreateNetwork import *
from sim import *
from Interactive import *

import sys
import time

def main():
    if len(sys.argv) < 2:
    #No command line parameters, printing usage
        print("The simulation an be run in interactive or simulation mode. To run in "\
        + "interactive mode, add the flag -i.\nSimulation mode can be run either with or "
        + "without a file (use the interactive option to create a file)\n\nWith a file the "\
        + "command should look like this:\n"\
        + "python3 HealthSim.py -s netfile trans_rate recov_rate death_rate car_rate "\
        + "car_inf_rate govPrec\n\nWithout a file it looks like this:\n"\
        + "python3 HealthSim.py -s numPpl %inf trans_rate recov_rate death_rate car_rate "\
        + "car_inf_rate gov_prec\n\nA description of the parameters is as follows:\n - "\
        + "netfile:\t\t\tthe file where the network has been saved\n - numPpl:\t\t\tthe number "
        + "of people to create in the network, should be between 0 and 850\n - %inf:\t\t\tThe "\
        + "percentage of people initially infected, should be between 0 and 100\n - recov_rate:"\
        + "\t\t\tThe chance of an infected person recovering, should be between 0 and 1\n - "\
        + "death_rate:\t\t\tThe chance of an infected person dying, should be between 0 and 1\n"\
        + " - car_rate:\t\t\tThe chance that a newly infected person is a hidden carrier of "\
        + "the disease, should be between 0 and 1\n - car_inf_rate:\t\tThe chance that a "\
        + "carrier of the disease becomes a known infection, should be between 0 and 1\n - "\
        + "gov_prec:\t\t\tThe level of government precaution, a higher precaution means they "\
        + "are more likely to put measures in place to curb the spread of the virus, should be "\
        + "betweem 0 and 2")
    elif sys.argv[1] == '-i':
        #interactive menu
        Menu()
    elif sys.argv[1] == '-s':
        #simulation mode

        #I like to time my simulations :D
        t1 = time.time()

        #how the commad line parameters should be set up: (h is short for HealthSim.py)
        #python3 h -s netfile trans_rate recov_rate death_rate car_rate car_inf_rate gov_prec 
        #   or
        #h -s numPpl, %inf, trans_rate, recov_rate death_rate car_rate car_inf_rate gov_prec 

        #9 arguments is the correct number for using a file as the input graph
        if len(sys.argv) == 9:
            try:
                graph = DSAGraph.loadGraph(sys.argv[2])
                loaded = True
            except:
                print("Couldn't load the file correctly")
                loaded = False
            if loaded:
                try:
                    simulation(graph, float(sys.argv[3]), float(sys.argv[4]), \
                    float(sys.argv[5]), float(sys.argv[6]), float(sys.argv[7]), \
                    float(sys.argv[8]))
                except:
                    print("Couldn't run simulation, check youur parameters are valid")

        #10 arguments is the correct number for creating an input graph
        elif len(sys.argv) == 10:
            #no file - auto-generate file
            try:
                graph = init(int(sys.argv[2]), float(sys.argv[3]) / 100)
                created = True
            except:
                print("Couldn't create the network, make sure your parameters are correct!")
                created = False

            if created:
                try:
                    simulation(graph, float(sys.argv[4]), float(sys.argv[5]), \
                    float(sys.argv[6]), float(sys.argv[7]), float(sys.argv[8]), \
                    float(sys.argv[9]))
                except :
                    print("Couldn't run simulation, check youur parameters are valid")


        else:
            print("Invalid number of arguments!") 

        t2 = time.time() - t1
        print("\nTime take in seconds:", t2, '\n')
    else:
        print("expected -i or -s, but got ", sys.argv[1])
#END main

if __name__ == '__main__':
    main()

