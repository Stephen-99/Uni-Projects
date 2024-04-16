####
# Person.py
#
# Date created:     30/04/2020
# Date last edited: 01/06/2020
#
# Author: Stephen den Boer  19761257
#
# Overview:
#   This is a class that holds the definition of a person in the simulation. Holds general 
#   information about the person as well as their infection status
#   They should be added as a node in the graph
#   
####

from InfectionStatus import *
from HealthStatus import *
from Prevention import *

class Person():
    
    def __init__(self, name, age, rebelliousness, healthStatus, infectionStatus, \
    preventativeMeasure):
        self.name = name
        self.age = age

        #measure of how likely they are to adhere to government restrictions
        self.rebel = rebelliousness 
        #are they rebelling against government restrictions
        self.rebelWork = False
        self.rebelLock = False

        self.health = HealthStatus(healthStatus)
        self.inf = InfectionStatus(infectionStatus)
        self.prev = Prevention(preventativeMeasure)
        
        #if they become one of these categories, then these values will be updated to store the
        #date on which the became one of those categories
        self.carDate = None
        self.infDate = None
        self.recovDate = None
        self.deathDate = None
    #END __init__
    
    def __str__(self):
        returnStr = "\nName: " + self.name
        returnStr += "\nAge: " + str(self.age)
        returnStr += "\n" + str(self.health)
        returnStr += "\nInfection Status: " + str(self.inf)
        returnStr += "\n" + str(self.prev)

        if self.carDate != None:
            returnStr += "\nBecame a carrier in week: " + str(self.carDate)
        if self.infDate != None:
            returnStr += "\nBecame infected in week: " + str(self.infDate)
        if self.recovDate != None:
            returnStr += "\nRecovered from the virus in week: " + str(self.recovDate)
        if self.deathDate != None:
            returnStr += "\nDied from the virus in week: " + str(self.deathDate)
        
        returnStr += '\n'
        
        return returnStr

    def getRiskFactor(self):
        #Calculate vulnerability to the virus. 
        #Based on age, health and preventative measures. Other elements effect the chances on 
        #a relational basis, whereas this measure applies to all connections

        #under 40 less risk, over 40 more risk
        risk = self.age / 40
        risk *= self.health.getRiskFactor()
        risk *= self.prev.getRiskFactor()

        return risk
#END class
