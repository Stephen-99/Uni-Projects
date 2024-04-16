####
# DSAGraph.py
#
# Date created:     23/04/2020
# Date last edited: 30/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This is a class that holds the Graph definition. It has beeen updated making it more spcific
#   for the assignment. some unused functionality has been removed, but small bits remain
#   NOTE: This code was originally submitted for Practical 6 on 24/04/20
#   It has since then been significantly changed, however there are still methods which are 
#   unchanged or only changed slightly
####

from SimEdge import *

from DSALinkedList import *

from Colour import *
from Connections import *
from InfectionStatus import *

import pickle   #for saving graph in serialized format
import sys

class DSAGraph():
    def __init__(self):
        self.vertices = DSALinkedList()
        self.edges = DSALinkedList()
        self.numVertices = 0
        self.numEdges = 0
    #END __init__

    def addVertex(self, label, value=None):
        if self.hasVertex(label):
            raise VertexError("Tried to add an existing vertex")
        else:
            vertex = DSAGraphVertex(label, value)
            if self.vertices.length == 0:
                self.vertices.insertAny(vertex, 0)
            else:
                ii = 0 

                #iterating to find the right spot to insert the vertex in order to maintain 
                #alphabetical order, lowercase > uppercase, so uppercase will be first
                iter1 = iter(self.vertices)
                while (ii < self.vertices.length) and (label > next(iter1).label):
                    ii += 1
                self.vertices.insertAny(vertex, ii)
            self.numVertices += 1
    #END addVertex

    #updates the infection status of a person at a given index
    def updateInfection(self, idx, newVal):
        if self.vertices.isEmpty():
            raise ListUnderflow
        if idx >= self.vertices.length or idx < 0:
            raise IndexError("Index out ouf bounds")
        
        iter1 = iter(self.vertices)
        for ii in range(idx):       
            next(iter1)
        next(iter1).value.inf.status = newVal

    
    def addEdge(self, label1, label2, directed=False, connectType=None, intamacy=None): 
        #if directed, connectType, intamacy passed will overwrite value of False
        #This allows more versatility in the edges use, however, for this simulation, connectType
        #and intamacey must always be passed
        vertex1 = self.getVertex(label1)
        vertex2 = self.getVertex(label2)

        if vertex1 == None:
            raise EdgeError("vertex " + label1 + " wasn't found")
        if vertex2 == None:
            raise EdgeError("vertex " + label2 + " wasn't found")
        if label1.lower() == label2.lower():
            raise EdgeError("tried to add an edge between the same vertex")

        if connectType != None:
            Edgelabel = str(connectType) + '-' + label1 + '-' + label2

            if self.hasEdge(Edgelabel):
                raise EdgeError("edge already exists")
            
        elif vertex1.hasEdge(label2):
            #if not directed, will find edge, if directed, direction is 
            #in order, so will find the edge
            raise EdgeError("tried to add existing edge")   

        #finally, if it passed all error checking, the edge can be added
        #Currently passing a label to the edge, however the edge specific to this simulation
        #defines is own label
        edge = SimEdge(vertex1, vertex2, label1 + label2, connectType, intamacy)
        vertex1.addEdge(vertex2, edge)
        self._addEdge(edge)

        if not directed:
            edge = SimEdge(vertex2, vertex1, label2 + label1, connectType, intamacy)
            vertex2.addEdge(vertex1, edge)
            self._addEdge(edge)
        #END if
    #END addEdge

    #private add edge method, recieves an edge object and inserts it in alphabetical order
    def _addEdge(self, edge):
        if self.edges.length == 0:
            self.edges.insertFirst(edge)
        else:
            ii = 0
            iter1 = iter(self.edges)
            while ii < self.edges.length and (edge.label > next(iter1).label):
                ii += 1
            self.edges.insertAny(edge, ii)
        self.numEdges += 1
        #END if-else
    #END _addEdge

    def hasEdge(self, label):
        label = label.lower()

        found = False
        ii = 0
        iter1 = iter(self.edges)

        while (ii < self.numEdges) and (not found):
            if next(iter1).label == label:
                found = True
            else: 
                ii += 1
    #END hasEdge

    #Ideally I would refactor the following 3 functions so as to use a find method in LL
    def hasVertex(self, label):
        label = label.lower()

        found = False
        ii = 0
        iter1 = iter(self.vertices)

        while (ii < self.numVertices) and (not found):
            if next(iter1).label.lower() == label:
                found = True
            else:
                ii += 1
        #END while

        return found
    #END hasVertex

    def getVertex(self, label):
        label = label.lower()
    
        vert = None
        ii = 0
        iter1 = iter(self.vertices)

        while (ii < self.numVertices) and (vert == None):
            vertex = next(iter1)
            if vertex.label.lower() == label:
                vert = vertex
            else:
                ii += 1

        return vert
    #END hasVertex

    def removeVertex(self, label):
        label = label.lower()

        vert = None
        ii = 0
        iter1 = iter(self.vertices)

        while (ii < self.numVertices) and (vert == None):
            vertex = next(iter1)
            if vertex.label.lower() == label:
                vert = vertex
            else:
                ii += 1
        if vert == None:
            raise VertexError("Vertex doesn't exist")
        else:
            self.removeAllConnections(vert)
            self.vertices.removeAny(ii)
            self.numVertices -= 1

    #This removes all the connections a vertex/person has, which is necassary when removing them
    #from the graph or when they die
    def removeAllConnections(self, vertex):
        for connect in vertex.connections:
            label = str(connect.type) + '-' + connect.toV.label + '-' + connect.fromV.label
            connect.toV.removeConnection(label)

            vertex.connections.removeFirst()
            self.numEdges -= 2

    #END removeAllConnections
    
    #Allows the intamacy of a connection to be changed
    def changeEdgeIntamacy(self, edge, newVal):
        #updating origional connection
        edge.fromV.updateConnection(edge.label, newVal)

        #all connections are 2-ways so need to update the corresponding edge.
        #Since we know how the edge labels are structured, can create the label for the 
        #corresponding edge
        label = str(edge.type) + '-' + edge.toV.label + '-' + edge.fromV.label
        edge.toV.updateConnection(label, newVal)
    #END changeEdgeIntamacy
    
    def removeEdge(self, label1, label2, connectionType):
        vert1 = self.getVertex(label1)
        vert2 = self.getVertex(label2)
        Edgelabel1 = str(connectionType) + '-' + label1 + '-' + label2
        Edgelabel2 = str(connectionType) + '-' + label2 + '-' + label1
       
        if vert1 == None or vert2 == None:
            raise VertexError("Could not find vertex")
        else:
            removed = vert1.removeConnection(Edgelabel1)
            if removed:
                #if one exists the other will 2, at least with this simulation, where all edges 
                #are added both ways
                vert2.removeConnection(Edgelabel2)
                self.numEdges -= 2
            else:
                raise EdgeError("Couldn't find the edge")
    #END removeEdge

    #Tests if two verticies are connected
    def isAdjacent(self, label1, label2):
        adjacent = False
        vertex1 = self.getVertex(label1)
        vertex2 = self.getVertex(label2)
        if vertex1.hasEdge(label2) or vertex2.hasEdge(label1):
            adjacent = True
        #END if
        return adjacent
    #END isAdjacent

    #displays the graph, this has been updated and is very specific to this simulation
    def display(self):
        print("\nPerson\t\t      | Number of connections")
        print(65 * "=")

        for vertex in self.vertices:
           
            #obtain the colour to print the person, depending on their infection state
            col = vertex.value.inf.statusColour()
            printVal = fg(vertex.label, col)

            print(printVal, end=(22-len(vertex.label)) * ' ' + '|   ')
            
            #obtain the number of each type of connection that person shares
            arr = self._numConnectTypes(vertex)

            #print the number of connections for each type
            sumConnections = 0
            for ii in range(9):
                #obtain the colour for the connection type
                col = Connections.connectColour(ii) 
                print(fg(str(arr[ii]), col), end=(3-len(str(arr[ii]))) * ' ')
                sumConnections += arr[ii]
            print("| Total:", fg(str(sumConnections), 208))
            #printing last one with a newline at the end
        #END for
        
        #legend matches colours to their meaning
        self._printLegend()        
        print()
    #END display
           
    #obtains the number of each type of connection is shared with the given vertex
    def _numConnectTypes(self, vertex):
        #create an aray to store the counts for each type of connection. idx 0 store number of 
        #connections of type 0 etc.
        arr = np.zeros(9, dtype=int)

        if not vertex.connections.isEmpty():
            iter1 = iter(vertex.connections)

            for ii in range(vertex.connections.length):
                connect = next(iter1)

                #Only increment for connections with more than 0 chance of transmitting
                if connect.intamacy != 0:
                    arr[connect.type] += 1

        return arr
    #END _numConnecTypes

    #prints a legend to match colours to their infection status and conection type
    def _printLegend(self):
        print("\n")
        print('+' + 38 * '-' + '+')
        print("| Legend:"+ 30 * ' ' + '|')
        print('|' + 38 * '-' + '|')

        #printing infection types colour coding
        for ii in range(5):
            I = InfectionStatus(ii)
            infType = str(I)
            col = I.statusColour()
            print("|", fg(infType, col), (36- len(infType)) * ' ' + '|')
        print('|' + 38 * '-' + '|')
        
        #printing connection types colour coding
        for ii in range(9):
            connectType = Connections.connectType2Str(ii)
            colour = Connections.connectColour(ii)
            print("|", fg(connectType, colour), (36- len(connectType)) * ' ' + '|')
        print('+' + 38 * '-' + '+')

    #END _printLegend
        
    
    def saveGraph(self, filename):
        try:
            #since the graphs can be quite large, pickling can have a recursion overflow,
            #setting the max limit higher resolves this issue
            sys.setrecursionlimit(100000)
            with open(filename, 'wb') as saveFile:
                #NOTE trying to save graphs with 900 or more nodes (created with lots of 
                #connections via createNetwork file) causes an unexplained segmentation fault
                #This is almost certainly caused by the pickle package which uses cpython. It
                #seems there is just too much for it to handle
                pickle.dump(self, saveFile)

        except Exception as err:
            raise IOError("Error trying to save graph:", err)
    #END saveGraph

    def loadGraph(filename):
        try:
            with open(filename, 'rb') as loadFile:
                graph = pickle.load(loadFile)
            return graph
        except Exception as err:
            raise IOError("Error trying to load graph:", err)
    #END loadGraph

#END class

