####
# CreateNetwork.py
#
# Date created:     24/04/2020
# Date last edited: 30/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This holds the infromation for creating a random network
#    
####

from DSAGraph import *
from Connections import *
from Person import *
from Population import *
from ReadNames import *

from random import *



def init(numPpl, percentInfected):

    connections = Connections()   
    graph = DSAGraph()

    createPeople(graph, numPpl, connections)
    

#NOTE would like numbers to be higher but taking a looong time to add all these connections so 
#cut numbers down in favour of having more ppl in the simulation
    addGroup(graph, 1, 5, 20, int(0.4 * numPpl), connections)
    addGroup(graph, 2, 10, 30, int(0.2 * numPpl), connections)
    addGroup(graph, 3, 10, 30, int(0.15 * numPpl), connections)
    addGroup(graph, 5, 1, 5, int(0.1 * numPpl), connections)
    addGroup(graph, 6, 5, 10, int(0.1 * numPpl), connections)
    addGroup(graph, 7, 5, 20, int(0.1 * numPpl), connections)
    addGroup(graph, 8, 1, 10, int(0.4 * numPpl), connections)

    #add the initial infections
    infectPpl(graph, int(numPpl * percentInfected), numPpl, 0)
    
    return graph

#END init


def createPeople(graph, numPpl, connections):    

    #read the csv file with 7000 names
    names = getNames()
    #There is no duplicate names in the file. This is important since the names are used as the 
    #vertex labels.     
    #Only 7000 names, simulation limited to 7000 ppl
        #realistically the simulation is limited to less since the graph cannot be saved using 
        #pickle with over 900 nodes since it causes an unexplained segfault


    #family is just  group of ppl living together, even though not everyone lives with their fam
    #anymore... can be anywhere from 1-7 people, yes ik there are families with more than 7 ppl
    family = np.empty(7, dtype=object)    #rlly dtype=string but otherwise no arbitary length

    #creating 'families'
    ii = 0
    while ii < numPpl:
        #if there's less then 7 left, make family num of ppl left
        if (numPpl - ii) < 7:
            famSize = numPpl - ii
        else:
            famSize = randint(1, 7)
        for jj in range(famSize):
            #NOTE used a LL because, can't remove indicies from an array, so making sure no 
            #duplicates becomes rlly slow, so using LL and removing a name once retireved.
                #will get same sequence of names each time.
                    #turns our LL is onl 1% faster for 6000 names and about the same speed for
                    #low number of names. It is more consistent though. array could have good
                    #or bad run depending on how often an already removed name is randomly 
                    #selected
            name = names.removeFirst()

            #70% of population healthy
            if random() > 0.7:
                healthStat = randint(1, 5)
            else:
                healthStat = 0
            #creating a person, with random age, rebelliousness, and health status. The initial 
            #infection state is susceptible, and they employ no measures to combat the virus
            p1 = Person(name, randint(1, 90), random(), healthStat, 0, 0)

            #add the person to the family array
            family[jj] = name

            #add the person to the network
            graph.addVertex(name, p1)

        #loops for every person in the family, creating a connection of people living in the 
        #same house between each one. The loops are set up so there is no duplicate connections
        for jj in range(famSize):
            for kk in range(jj + 1, famSize):
                #will aded an un-directed edge, i.e. will be added both ways
                graph.addEdge(family[jj], family[kk], connectType=0, \
                intamacy=connections.getValue(0))

        #keeps track of the number of people added to the network 
        ii += famSize
#END createPeople

#This method creates groups of people who share a type of connection
#totalPpl, is the total number of ppl with this group type
def addGroup(graph, groupType, minMembers, maxMembers, totalPpl, connections):

    #create an array to keep track of each member of the group
    group = np.empty(maxMembers, dtype=object)  

    #creating a copy of the vertices, this turns out to be faster than marking nodes as visited
    #and having the chance of re-generating an already visited index
    verts = DSALinkedList()
    for vert in graph.vertices:
        verts.insertFirst(vert.value.name)

    ii = 0
    while ii < totalPpl:
        #if there is less people left than maximum group size, make a group out of the remaining
        #members, might be less than minMembers, but that's just too bad so sad
            #tbh, any connection could be a group of 2 ppl or more, if its 1, then there will be 
            #no connection and will just be 1 less than totalPpl with this connection type
        if (totalPpl - ii) < maxMembers: 
            groupSize = totalPpl - ii
        else:
            groupSize = randint(minMembers, maxMembers)

        #obtain ppl for the group
        #This is done randomly so the connections may not be realistic, e.g. a group of people 
        #living together, may each have a different number of neighbours
        for jj in range(groupSize):
            idx = randint(0, verts.length-1)
            group[jj] = verts.removeAny(idx) 


        for jj in range(groupSize):
            for kk in range(jj + 1, groupSize):
                #will aded an un-directed edge, i.e. will be added both ways
                graph.addEdge(group[jj], group[kk], connectType=groupType, \
                intamacy=connections.getValue(groupType))

        ii += groupSize

#END addGroup
    

def infectPpl(graph, num2Infect, numPpl, simTime):
    for ii in range(num2Infect):
        #using a Linked list search to update a person at a random index
        idx = randint(0, numPpl - 1)
        p = graph.vertices.peekAny(idx).value
        
        #only change susceptible ppl
        while p.inf.status != 0:
            idx = randint(0, numPpl - 1)
            p = graph.vertices.peekAny(idx).value

        #update status to be infected
        graph.updateInfection(idx, 2)
        p.infDate = simTime

#END infectPpl


