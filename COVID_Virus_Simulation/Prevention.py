####
# Prevention.py
#
# Date created:     30/04/2020
# Date last edited: 01/06/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This is a class that stores the preventative actions being taken.
#    
####

from DSALinkedList import*
import numpy as np

#NOTE: methods 2-4 only apply if the government doesn't enforce it

class Prevention():
    
    def __init__(self, method):
        #methods stored in an array holding a value of true or false, so if index 2 is true 
        #then measure 2 is being employed etc/
        self.methods = np.full(5, False, dtype = bool)

        self.methods[method] = True
    #END __init__

    def __str__(self):  
        returnStr = "Prevantative measures employed:"
        if self.methods[0]:
            returnStr += " None\n"
        else:
            if self.methods[1]:
                returnStr += " Washes hand regularly\n"
            if self.methods[2]:
                returnStr += " Social Distancing\n"
            if self.methods[3]:
                returnStr += " Working from home\n"  #incl. school
            if self.methods[4]:
                returnStr += " Complete lockdown\n"

        return returnStr

    def hasMethod(self, method):
        return self.methods[method]

    def addMethod(self, method):
        if self.methods[0]:
            self.methods[0] = False
        self.methods[method] = True
    #END addMethod

    def removeMethod(self, method):
        self.methods[method] = False
        
        anyMethods = False
        for ii in range(5):
            if self.methods[ii]:
                anyMethods = True

        if not anyMethods:
            self.methods[0] = True
    #END removeMethod

    #methods are updated in a similar way to the Govenrment class, where if a higher measure is 
    #employed, all lower measures will be employed. Rebeliousness is squared so that it has a 
    #greater influence than the percentage infected
    def updateMethods(self, rebel, precentInf):
        if (((1-rebel) ** 2) * precentInf * 7) > 2:
            self._updateMethods(4)
        elif (((1-rebel) ** 2) * precentInf * 7) > 0.5:
            self._updateMethods(3)
        elif (((1-rebel) ** 2) * precentInf * 7) > 0.20:
            self._updateMethods(2)
        elif (((1-rebel) ** 2) * precentInf * 7) > 0.05:
            self._updateMethods(1)
        elif (((1-rebel) ** 2) * precentInf * 7) > 0.01:
            self._updateMethods(0)
    #END updateMethods

    def _updateMethods(self, num):

        #set measures above current measure to false
        for ii in range(num + 1, 5):
            self.methods[ii] = False

        #set measures below and including current measure to true
        for ii in range(0, num + 1):
            self.methods[ii] = True


    def getRiskFactor(self):
        #only first preventative measure is an overarching one that applies to all relationships
        #the others are dealt with when running the simulation
        risk = 1
        if self.methods[1]:
            risk = 0.8
        return risk
    #END getRiskFactor


