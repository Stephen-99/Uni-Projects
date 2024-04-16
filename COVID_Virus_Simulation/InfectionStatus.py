###
# InfectionStatus.py
#
# Date created:     30/04/2020
# Date last edited: 30/04/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This is a class that holds the infection status.
#    
####

class InfectionStatus():
    
    def __init__(self, status):
        self.status = status
    #END __init__

    def __str__(self):
        if self.status == 0:
            returnStr = "susceptible"
        elif self.status == 1:
            returnStr = "carrier"
        elif self.status == 2:
            returnStr = "infected"
        elif self.status == 3:
            returnStr = "recovered"
        else:   #will be 4
            returnStr = "dead"
        return returnStr
    #END __str__
    
    #This returns a number for use with the Colours module in order to print each infection type
    #in an appropriate colour
    def statusColour(self):

        if self.status == 0:
            colour = 255    #default white
        elif self.status == 1:
            colour = 226
        elif self.status == 2:
            colour = 9
        elif self.status == 3:
            colour = 46
        else:   #will be 4 
            colour = 240

        return colour
    #END statusColour


#END class
