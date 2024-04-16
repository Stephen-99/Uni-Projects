####
# sim.py
#
# Date created:     27/05/2020
# Date last edited: 30/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This holds the code necassary for running in simulation mode
#    
####

from DSAGraph import *
from Population import *
from Government import *
from Connections import *
from Person import *

from random import *
import matplotlib.pyplot as plt
import sys

def simulation(graph, trans_rate, recov_rate, death_rate, car_rate, car_inf_rate, govPrecaution):
    #26 fortnights, 1 full year
    maxIter = 26    
    pop = getStats(graph)
    con = Connections()
    
    gov = Government(govPrecaution)


    percentInf = np.full(maxIter, None, dtype = float)
    percentCar = np.full(maxIter, None, dtype = float)
    percentSusc = np.full(maxIter, None, dtype = float)
    percentRecov = np.full(maxIter, None, dtype = float)
    percentDead = np.full(maxIter, None, dtype = float)

    inf = pop.infected / pop.numPpl

    #all output will go to a file with name reflecting the parameters passed
    filename = str(pop.numPpl) + '_' + '{:.3f}'.format(inf) + '_' + str(trans_rate) + \
    '_' + str(recov_rate) + '_' + str(death_rate) + '_' + str(car_rate) + '_' + \
    str(car_inf_rate) + '_' + str(govPrecaution)

    #Idea for using stdout came from:
        #https://kite.com/python/answers/how-to-redirect-print-output-to-a-text-file-in-python
        #this means even functions called from differen classes will print to the same file
    sys.stdout = open(filename + '.txt', 'w')

    print("Initial stats:")
    pop.generateStatistic()

    ii = 0
    #iterate until a year passes OR there is no one infected or a carrier OR everyone has been 
    #infected (no mor susceptible people left
    while (ii < maxIter) and ((pop.infected + pop.carrier) != 0) and (pop.susc != 0):
        #puting stats into arrays for graphing
        percentInf[ii] = pop.infected / pop.numPpl * 100
        percentCar[ii] = pop.carrier / pop.numPpl * 100
        percentSusc[ii] = pop.susc / pop.numPpl * 100
        percentRecov[ii] = pop.recov / pop.numPpl * 100
        percentDead[ii] = pop.dead / pop.numPpl * 100

        #2*(ii+1) is converting the loop index to the number of weeks
        timestep(graph, pop, gov, con, trans_rate, car_rate, car_inf_rate, death_rate, \
        recov_rate, 2*(ii+1))
        print("\nWeek " + str(2*(ii+1)) + ':')

        #update and output the statistics
        pop.generateStatistic()

        ii += 1

    #print why the simulation ended
    if ii == maxIter:
        print("Simulation ended\nSimulation state: Time interval finished")
    elif (pop.infected + pop.carrier) == 0:
        print("Simulation ended\nSimulation state: Virus cured!")
    elif pop.susc == 0:
        print("Simulation ended\nSimulation state: Virus has infected everyone")

    #make a graph of how the infection rates varied over time
    plt.figure(figsize=(16, 9))
    plt.plot(percentInf, 'r', label='infected')
    plt.plot(percentCar, 'y', label='carrier')
    plt.plot(percentRecov, 'g', label='recovered')
    plt.plot(percentDead, 'k', label='dead')
    plt.plot(percentSusc, 'b', label='susceptible')
    plt.xlabel("Number of fortnights")
    plt.ylabel("% of population")
    plt.legend()

    plt.savefig(filename + '.png', dpi=100)

    sys.stdout.close()
    sys.stdout = sys.__stdout__
#END simulation

#This method gets the overall population statistics for the graph
def getStats(graph):
    numInf = 0
    numCar = 0
    numSusc = 0
    numRecov = 0
    numDead = 0

    #counts the number of people in each infection category
    for vertex in graph.vertices:
        if vertex.value.inf.status == 0:
            numSusc += 1
        elif vertex.value.inf.status == 1:
            numCar += 1
        elif vertex.value.inf.status == 2:
            numInf += 1
        elif vertex.value.inf.status == 3:
            numRecov += 1
        elif vertex.value.inf.status == 4:
            numDead += 1
    
    pop = Population(numInf, numCar, numSusc, numRecov, numDead)
    return pop
#end getStats

def timestep(graph, pop, gov, con, trans_rate, car_rate, car_inf_rate, death_rate, recov_rate, \
numWeeks):
    #calculate percentage infected, **based only on live ppl**
    percentInf = pop.infected / (pop.numPpl - pop.dead)
    
    updateGov(graph, gov, percentInf, con)
    updateIntamacy(graph, con)
    updateIndiv(graph, con, gov, percentInf)
    updateInfection(graph, pop, trans_rate, car_rate, car_inf_rate, death_rate, recov_rate, \
    numWeeks)

#END timestep

#Imposes or relaxes restrictions based on the percentage infected and the government's precaution
def updateGov(graph, gov, percentInf, con):
    gov.updateMeasures(percentInf, con)
#END updateGov

#updating government measures should update the intamacy of different relationships, so now 
#connections need to be updated to reflect those changes
def updateIntamacy(graph, con):
    for connection in graph.edges:
        connection.intamacy = con.getValue(connection.type)
#END updateIntamacy

#Updates individually employed measures
def updateIndiv(graph, con, gov, percentInf):
    for vertex in graph.vertices:
        #they rethink their rebellion each time, otherwise too many rebel
        vertex.value.rebelLock = False
        vertex.value.rebelWork = False

    for vertex in graph.vertices:
        p = vertex.value

        #if infected, apply all measures
        if p.inf.status == 2:
            #giving false input to make sure all methods are applied
            p.prev.updateMethods(-5, 5)
        else:
            p.prev.updateMethods(p.rebel, percentInf)

        #Applying individual prevention measures
        applyIndivMeasures(graph, vertex, gov, con)
#END updateIndiv

def applyIndivMeasures(graph, vertex, gov, con):
    p = vertex.value

#What if 2 connected people both social distance, connection gets updated twice...
    #this is true for this whole method
        #values get reset on every timestep so its actually not a huuuge deal
        #also only occurs for people in the same group, and they both have to rebel, so the 
        #chance is pretty low...

    #if government hasn't implemented social distancing
    if not gov.measures[3]:
        #if person is social distancing
        if p.prev.hasMethod(2):
            for connection in vertex.connections:
                if connection.type != 0:
                    #All connections except those in the same household
                    graph.changeEdgeIntamacy(connection, 0.8 * connection.intamacy)

    #Wait... what if one person social distances, but their friend rebels???
        #Kinda hard to stop the person from breaching social distancing, it only works if both
        #are social distancing, so reseting connections both way is fine
    else:
        #government has implemented social distancing
        #If not self-implementing social distancing
        if not p.prev.hasMethod(2):
            #chance they are rebellious and don't implement social distancing
            if random() < (2 * (1 - p.rebel) ** 2):
                
                #all connections except those in same household back to former intamacy
                for connection in vertex.connections:
                    if connection.type != 0:
                        graph.changeEdgeIntamacy(connection, connection.intamacy / 0.8)
    #END social distancing calcs
        
    #if government hasn't stopped work in-person
    if not gov.measures[6]:
        #if person is working from home
        if p.prev.hasMethod(3):
            for connection in vertex.connections:
                #work or uni or school together
                if connection.type == 1 or connection.type == 2 or connection.type == 3:
                    graph.changeEdgeIntamacy(connection, 0)

    else:
        #Government has asked ppl to work from home
        #If not individually working from home
        if not p.prev.hasMethod(3):
            #chance they are rebelious and don't work from home
            #doesn't represent real world so much since boss may or may not allow people to work
            #in person again.
            if random() < (2 * (1 - p.rebel) ** 2):
                p.rebelWork = True
                
                #all connections except those in same household back to former intamacy
                for connection in vertex.connections:
                    if connection.type != 0:
                        #only changes relationships with those who also rebel
                        if connection.toV.value.rebelWork:
                            #reset value to initial value with social distancing in place and 
                            #meetings restricted to 20 ppl
                            graph.changeEdgeIntamacy(connection, \
                            con.defaults[1] * 0.8 * 0.95 *0.95)
    #END work from home calcs

    #if government hasn't implemented lockdown
    if not gov.measures[8]:
        #if person is in self-imposed lockdown
        if p.prev.hasMethod(4):
            
            #All connections except those in the same household
            for connection in vertex.connections:
                if connection.type != 0:
                    graph.changeEdgeIntamacy(connection, 0)
    else:
        #government has imposed lockdown
        #If not self-implementing lockdown 
        if not p.prev.hasMethod(4):
            #chance they are rebelious and don't implement lockdown
            if random() < (2 * (1 - p.rebel) ** 2):
                p.rebelLock = True
                
                #all connections except those in same household back to former intamacy
                for connection in vertex.connections:
                    if connection.type != 0:
                        #only changes relationships with those who also rebel
                        if connection.toV.value.rebelLock:
                            #reset value to initial value with social distancing in place and 
                            #meetings restricted to 20 ppl
                            graph.changeEdgeIntamacy(connection, \
                            con.defaults[1] * 0.8 * 0.95 *0.95)
    #END lockdown calcs
        
#END applyIndivMeasures

#Go through each person, and all their connections to see if they get infected
def updateInfection(graph, pop, trans_rate, car_rate, car_inf_rate, death_rate, recov_rate, \
numWeeks):
    for vert in graph.vertices:
        p = vert.value
        risk = p.getRiskFactor()
        if p.inf.status == 0:
            #susceptible

            #changed to while loop, loop until done or they got the virus
            changed = False
            ii = 0
            iter1 = iter(vert.connections)
            while (ii < vert.connections.length) and (not changed):
                connect = next(iter1)
                ii += 1
                #if the connected person is infected or a carrier
                if connect.toV.value.inf.status == 1 or \
                connect.toV.value.inf.status == 2:
                    #if random number between 0 and 1 is less than chance of getting the virus
                    if random() < risk * connect.intamacy * trans_rate:
                        #got the virus!
                        changed = True
                        if random() < car_rate:
                            #now a carrier of the virus
                            p.inf.status = 1
                            pop.carrier += 1
                            p.carDate = numWeeks
                        else:
                            #now infected!
                            p.inf.status = 2
                            pop.infected += 1
                            p.infDate = numWeeks
                        pop.susc -= 1

        elif p.inf.status == 1:
            #carrier
                
            #Chance to become fully infected
            if random() < car_inf_rate:
                p.inf.status = 2
                pop.infected += 1
                pop.carrier -= 1
                p.infDate = numWeeks
            
            #not upgraded to infection, has half the chance of recovering, has to get worse 
            #before it gets better
            elif random() < recov_rate * 0.5:
                #recovered!
                p.inf.status = 3
                pop.recov += 1
                pop.carrier -= 1
                p.recovDate = numWeeks

        elif p.inf.status == 2:
            #infected
            if random() < death_rate:
                #died
                p.inf.status = 4
                graph.removeAllConnections(vert)
                pop.dead += 1
                pop.infected -= 1
                p.deathDate = numWeeks
            elif random() < recov_rate:
                #recovered!
                p.inf.status = 3
                pop.recov += 1
                pop.infected -= 1
                p.recovRate = numWeeks

        elif p.inf.status == 3:
            #Recovered, same as susc, except only 10% of the chance to get infected
            changed = False
            ii = 0
            iter1 = iter(vert.connections)
            while (ii < vert.connections.length) and (not changed):
                connect = next(iter1)
                ii += 1
                #if the connected person is infected or a carrier
                if connect.toV.value.inf.status == 1 or \
                connect.toV.value.inf.status == 1:
                    #if random number between 0 and 1 is less than chance of getting the virus
                    if random() < risk * connect.intamacy * trans_rate * 0.1:
                #        print("Recovered patient re-contracts the virus!")
                        #got the virus!
                        if random() < car_rate:
                            #now a carrier of the virus
                            p.inf.status = 1
                            pop.carrier += 1
                            p.carDate = numWeeks
                        else:
                            #now infected!
                            p.inf.status = 2
                            pop.infected += 1
                            p.infDate = numWeeks
                        pop.recov -= 1

        else:
            #dead person,  n0 longer any connections/chance of passing virus on to the living
            #removing dead connections, speeds up following iterations since there is less 
            #connections to iterate over
            graph.removeAllConnections(vert)
#END updateInfection

