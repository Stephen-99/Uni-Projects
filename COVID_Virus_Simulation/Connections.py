####
# Connections.py
#
# Date created:     23/05/2020
# Date last edited: 30/05/2020
#
# Author: Stephen den Boer  19761257
#
# Overview:
#   This class takes care of the different types of connections between people. It uses an array
#   to store the level of intamacy each connection has. There is also an array with the default
#   values so that the intamacy for a connection can be reset. The intamacy of a connection is 
#   important since it dictates the likelihood of contracting the disease via that connection.
#   During the simulation, there are various measures which may change these likelihoods
####

import numpy as np

class Connections():
    def __init__(self): 
        self.types = np.empty(9, dtype = float)
        self.defaults = np.empty(9, dtype = float)

        self.defaults[0] = 0.9
        self.defaults[1] = 0.7
        self.defaults[2] = 0.6
        self.defaults[3] = 0.5
        self.defaults[4] = 0.5
        self.defaults[5] = 0.5
        self.defaults[6] = 0.3
        self.defaults[7] = 0.3
        self.defaults[8] = 0.2

        for ii in range(9):
            self.types[ii] = self.defaults[ii]
    
    def getValue(self, connectType):
        return self.types[connectType]

    def restoreDefault(self, connectType):
        self.types[connectType] = self.defaults[connectType]

    def connectType2Str(connectType):
        if connectType == 0:
            returnVal = "Living in the same house"
        elif connectType == 1:
            returnVal = "Work together"
        elif connectType == 2:
            returnVal = "School peer"
        elif connectType == 3:
            returnVal = "University peer"

        #This connection is not used since it doesn't tie in well with the preventative measures
        #being used. However, by the time I decided to remove it, it was already heavily tied
        #to multiple parts of the program, so its left in kinda 'floating'. It should be either
        #implemented or refactored out at a later date
        elif connectType == 4:
            returnVal = "Family member"
        elif connectType == 5:
            returnVal = "friend"
        elif connectType == 6:
            returnVal = "sports team/gym members together"
        elif connectType == 7:
            returnVal = "religious services together"
        elif connectType == 8:
            returnVal = "neighbours"
        else:
            raise Exception("Invalid connection")
        
        return returnVal

    #Returns the colour each connection should be printed in, as a number to be used by the 
    #method in the Colour class
    def connectColour(connectType):

        if connectType == 0:
            colour = 27
        elif connectType == 1:
            colour = 33
        elif connectType == 2:
            colour = 39
        elif connectType == 3:
            colour = 45
        elif connectType == 4:
            colour = 80
        elif connectType == 5:
            colour = 79
        elif connectType == 6:
            colour = 78
        elif connectType == 7:
            colour = 77
        elif connectType == 8:
            colour = 76

        return colour

    def updateValue(self, connectType, newVal):
        self.types[connectType] = newVal

    def resetValue(self, connectType):
        self.types[connectType] = self.defaults[connectType]
    
#END class
