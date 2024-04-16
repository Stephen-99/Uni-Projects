###
# SimEdge.py
#
# Date created:     25/05/2020
# Date last edited: 23/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This is a modified DSAEdge class to work with the simulation. It represents a connection 
#   between people.
#   NOTE the original version of this code (DSAEdge) was submitted for practical 6
#    
####

from DSAGraphVertex import *

class SimEdge():
    #intamacy represents how close the relationship is. The more intamate the relationship the 
    #greatrer the chance of spreading the virus, for lack of a better word...
    def __init__(self, fromVertex, toVertex, label, connectType, intamacy):
        self.fromV = fromVertex
        self.toV = toVertex
        self.label = str(connectType) + '-' + fromVertex.label + '-' + toVertex.label  
        self.type = connectType
        self.intamacy = intamacy
    #END __init__

    #Change type into string via connections class, also print intamacy.
    def __str__(self):
        return str(self.label) + (5-len(self.label)) * ' ' + '| '\
        + str(self.type)
    #END __str__
#END class

