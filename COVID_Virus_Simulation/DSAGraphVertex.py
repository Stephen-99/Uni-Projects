####
# DSAGraphVertex.py
#
# Date created:     23/04/2020
# Date last edited: 30/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This is a class that holds the GraphVertex definition, for use in 
#   conjunction with DSAGraph 
#   NOTE: This file was originally submitted for practical 6 on 24/04/2020. It has since been 
#   updated but lots of the original functionallity remains the same or similar
####

from DSALinkedList import *

class DSAGraphVertex():
    def __init__(self, label, value):
        self.label = label
        self.value = value  #will be a person obj in the simulation
        self.neighbours = DSALinkedList()
        self.connections = DSALinkedList()
        self.visited = False
    #END __init__

    def addEdge(self, vertex, edge):
        if self.neighbours.length == 0:
            self.neighbours.insertLast(vertex)
        else:
            #obtaining the index to insert neighbours alphabetically
            ii = 0
            iter1 = iter(self.neighbours)

            while (ii < self.neighbours.length) and \
            (vertex.label > next(iter1).label):
                ii += 1
            self.neighbours.insertAny(vertex, ii)
        
        
        if self.connections.length == 0:
            self.connections.insertLast(edge)
        else:
            #obtaining the index to inser the connections alphabetically
            ii = 0
            iter2 = iter(self.connections)

            while (ii < self.connections.length) and (edge.label > next(iter2).label):
                ii += 1
            self.connections.insertAny(edge, ii)
    #END addEdge

    def removeConnection(self, label):
        label = label.lower()

        found = False
        ii = 0
        iter1 = iter(self.connections)

        while (ii < self.connections.length) and (not found):
            con = next(iter1)
            if con.label.lower() == label:
                found = True
            else:
                ii += 1
        if found:
            self.connections.removeAny(ii)

        return found
    #END removeConnection
            
    #Checks if there is a connection between 2 people, used by original DSAGraph
    def hasEdge(self, label):
        found = False
        ii = 0
        iter1 = iter(self.neighbours)

        while (ii < self.neighbours.length) and (not found):
            if next(iter1).label == label:
                found = True
            else:
                ii += 1
        
        return found
    #END hasEdge

    #allows connection intamacy to be updated. This allows the chance of a connection 
    #transferring the disease to change based on different measures
    def updateConnection(self, label, newIntamacy):
        found = False
        ii = 0 
        iter1 = iter(self.connections)

        while (ii < self.connections.length) and (not found):
            edge = next(iter1)
            if edge.label == label:
                found = True
            else:
                ii += 1
        if found:
            edge.intamacy = newIntamacy
        else:
            raise Exception("Edge doesn't exist!!")
    #END updateConnection
            
    #This method checks to see if the vertex is currently storing a value since sometimes all 
    #thay you want to stroe might be a label
    def hasValue(self):
        hasValue = False
        if self.value != None:
            hasValue = True
        return hasValue
    #END hasValue

#END class

