###
# Population.py
#
# Date created:     23/05/2020
# Date last edited: 27/05/2020
#
# Author: Stephen den Boer  19761257
#
# Overview:
#   This is a class that holds information about the overall population, such as number of ppl
#   infected, susceptible etc. 
#    
####

#NOTE: Improvements could be had in terms of acquiring more statistics: Maybe also have things 
#like num ppl employeing each preventative measure... 

class Population():
    
    #restricitons need to be defined somehow.. maybe have an add restrictions method, but don't 
    #add any via init
    def __init__(self, numInfected, numCarrier, numSusceptible, numRecovered, numDead):
        self.numPpl = numInfected + numCarrier + numSusceptible + numRecovered + numDead
        self.infected = numInfected
        self.carrier = numCarrier
        self.susc = numSusceptible
        self.recov = numRecovered
        self.dead = numDead
        
                

    def generateStatistic(self):
        print("\nStatistics:")
        print(30 * '-')
        print("Infected:   \t", '{:.2f}'.format(self.infected / self.numPpl * 100),  "%")
        print("Carrier:    \t", '{:.2f}'.format(self.carrier / self.numPpl * 100), "%")
        print("Susceptible: \t", '{:.2f}'.format(self.susc / self.numPpl * 100), "%")
        print("Recovered:  \t", '{:.2f}'.format(self.recov / self.numPpl * 100), "%")
        print("Dead:  \t\t", '{:.2f}'.format(self.dead / self.numPpl * 100), "%")
        print()
        

#END class
