####
# Interaction.py
#
# Date created:     30/05/2020
# Date last edited: 30/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This holds the interactive menu, for use when running HealthSim.py with -i
#    
####

#The sim module includes the timestep method and also imports the other modules we need
from sim import *
from inputNum import *
from CreateNetwork import *

def Menu():
    graph = DSAGraph()
    con = Connections()
    gov = None
    pop = None

    #initiallising to None so that it can be checked if they are initialised
    trans_rate = None
    recov_rate=None
    death_rate=None
    car_rate=None
    car_inf_rate=None
    govPrecaution=None

    numWeeks = 2

    prompt = "Please enter the integer that corresponds to your choice\n1.  Load network\n"\
    + "2.  Randomly generate network\n3.  Set rates/interventions\n4.  Node operations\n5.  "\
    + "Edge operations\n6.  New Infection\n7.  Display network\n8.  Display statistics\n9.  "\
    + "Update (run a timestep)\n10. Save network\n0.  " + fg("Exit", 9)
    choice = inputInt(prompt, 0, 10)

    #While they don't choose to exit
    while choice != 0:
        if choice == 1:
            #load network

            print("Please enter the filename of the network to load:")
            filename = input()
            try:
                graph = DSAGraph.loadGraph(filename)
                pop = getStats(graph)
                print("Network successfully loaded")
            except IOError as err:
                print(err)
                
            pop = getStats(graph)

        elif choice == 2:
            #generate network
            #need at lest 2 people to have connections, and 900 people causes a segfault 
            #when trying to save the graph using pickle. 850 has been tested and doesn't 
            #produce a segfault
            numPpl = inputInt("Please enter the number of people for the network", 2, 850)

            #the percentage should be 0-100, and is converted and stroed as a decimal(i.e. 0-1)
            percentInf = inputFloat("Please enter the percentage of people initially infected",\
            0.0, 100.0) / 100
            
            print("Your network is being created!\nPlease be patient, as this can take up to a "\
            + "few minutes")
            graph = init(numPpl, percentInf)
            pop = getStats(graph)

        elif choice == 3:
            #set rates/interventions
            trans_rate = inputFloat("Please enter the probability of transferring the disease, "\
            + "the original value was: " + str(trans_rate), 0.0, 1.0)

            recov_rate = inputFloat("Please enter the probability of recovering from the "\
            + "disease, the original value was: " + str(recov_rate), 0.0, 1.0)

            death_rate = inputFloat("Please enter the probability of dying from the "\
            + "disease, the original value was: " + str(death_rate), 0.0, 1.0)

            car_rate = inputFloat("Please enter the probability of a newly infected person "\
            + "being an unaware carrier of the disease, the original value was: " \
            + str(car_rate), 0.0, 1.0)

            car_inf_rate = inputFloat("Please enter the probability of a carrier of the "\
            + "disease becoming aware that they are infected, the original value was: " \
            + str(car_inf_rate), 0.0, 1.0)

            govPrecaution = inputFloat("Please enter the caution level of the government on a "\
            + "scale of 0 to 2, where 2 is highly cautious. A higher level of caution means "\
            + "the government is more likely to implement restrictive mesaures. "\
            + "The original value was: " + str(govPrecaution), 0.0, 2.0)
            gov = Government(govPrecaution)
            
        elif choice == 4:
            #Node operation
            if graph.numVertices == 0:
                print("You have to load or generate a graph first!")
            else:
                nodePrompt = ("Please enter the integer that corresponds to yoru choice\n1. "\
                + "Find person\n2. Add person\n3. Delete a person")

                nodeChoice = inputInt(nodePrompt, 1, 3)

                if nodeChoice == 1:
                    #find person

                    print("please enter the name of the person you want to find")
                    name = input()
                    vert = graph.getVertex(name)
                    if vert == None:
                        print("Person wasn't found in the network")
                    else:
                        #print persons individual record
                        print(vert.value)
                elif nodeChoice == 2:
                    #add person

                    print("Please enter the name of the person")
                    name = input()
                    #Sorry people over 100 years old :/
                    age = inputInt("Please enter the age of the person", 1, 100)

                    rebel = inputFloat("Please enter a measure of rebeliousness between 0 and "\
                    + "1, where 1 is the most rebelious. This deterrmines how likely a person "\
                    + "is to follow government regulations and how likely they are to "\
                    + "implement individual precautions", 0, 1)

                    healthStatus = inputInt("Please enter the integer the corresponds to the "\
                    + "person's health status\n1. Healthy\n2. High blood pressure\n3. "\
                    + "Diabetic\n4. Kidney disease\n5. Heart condition\n6. Lung condition", 1, 6)
                    #healthStatus is 0-indexed
                    healthStatus -= 1   

                    infStatus = inputInt("Please enter the integer that corresponds to the "\
                    + "person's infection status\n1. Suceptible\n2. Carrier\n3. Infected\n4. "\
                    + "Recovered\n5. Dead", 1, 5)
                    #infection status is 0-indexed
                    infStatus -= 1

                    #There is no point getting a preventative measure since it auto updates based
                    #on the person's rebeliousness and the spread of the virus
                    prevMeasure = 0

                    p = Person(name, age, rebel, healthStatus, infStatus, prevMeasure)
                    try:
                        graph.addVertex(name, p)

                        print("Person succesfully added to network")

                        #update population stats accordingly
                        if infStatus == 0:
                            pop.susc += 1
                        elif infStatus == 1:
                            pop.carrier += 1
                        elif infStatus == 2:
                            pop.infected += 1
                        elif infStatus == 3:
                            pop.recov += 1
                        else:
                            pop.dead += 1
                        pop.numPpl += 1
                    except VertexError as err:
                        print("Couldn't add person to the network:", err)
                else:
                    #Delete a person

                    print("Please enter the name of the person you want to remove from "\
                    + "the network:")
                    name = input()
                    try:
                        vert = graph.getVertex(name)
                        if vert != None:
                            infStatus = vert.value.inf.status 
                        graph.removeVertex(name)
                        print(name, "was successfully removed from the network")

                        #update population stats accordingly
                        if infStatus == 0:
                            pop.susc -= 1
                        elif infStatus == 1:
                            pop.carrier -= 1
                        elif infStatus == 2:
                            pop.infected -= 1
                        elif infStatus == 3:
                            pop.recov -= 1
                        else:
                            pop.dead -= 1
                        pop.numPpl -= 1
                    except VertexError:
                        print(name, "was not found in the network")

        elif choice == 5:
            #Edge operations

            if graph.numVertices == 0:
                print("You have to load or generate a graph first!")
            else:
                edgeChoice = inputInt("Please enter the integer that corresponds with your "\
                + "choice:\n1. Add a connection\n2. Remove a connection", 1, 2)

                print("Please enter the name of the first person part of the connection:")
                name1 = input()

                print("Please enter the name of the second person part of the connection:")
                name2 = input()

                connectPrompt = "Please enter the integer that corresponds to the type of "\
                + "connection:"
                #connect prompt ends upd in a similar form to the menu prompt
                for ii in range(9):
                    connectPrompt += ('\n' + str(ii + 1) + '. ' + \
                    Connections.connectType2Str(ii))
                connectionType = inputInt(connectPrompt, 1, 9)
            
                #connection type is 0-indexed
                connectionType -= 1

                connectionIntamcay = con.getValue(connectionType)

                if edgeChoice == 1:
                    #add connection
                    try:
                        graph.addEdge(name1, name2, connectType=connectionType, \
                        intamacy=connectionIntamcay)
                        print("Successfully added connection in network")
                    except EdgeError as err:
                        print("Couldn't add connection to network:", err)

                else:
                    #remove connection
                    #NOTE connections are added in groups, this removes a connection between 2 
                    #ppl, it means that they will both still be connected to the other members 
                    #of that group, which isn't necassarily realistic
                    try:
                        graph.removeEdge(name1, name2, connectionType)
                        print("Connection successfully removed from network")
                    except VertexError:
                        print("Couldn't find those people in the network")
                    except EdgeError:
                        print("Couldn't find that connection between those people")
                    

        elif choice == 6:
            #New infection
            
            if graph.numVertices == 0:
                print("You have to load or generate a graph first!")
            else:
                if pop.susc == 0:
                    print("There's no one susceptible left to infect!")
                else:
                    #infect 1 randomly chosen person
                    infectPpl(graph, 1, graph.numVertices, numWeeks)
                    #Update population stats accordingly
                    pop.infected += 1
                    pop.susc -= 1
                    print("Successfully infected another person")
            
        elif choice == 7:
            #Display network

            if graph.numVertices == 0:
                print("You have to load or generate a graph first!")
            else:
                graph.display()

        elif choice == 8:
            #Display stats
            if graph.numVertices == 0:
                print("You have to load or generate a graph first!")
            else:
                statChoice = inputInt("Please enter the integer that corresponds with what "\
                + "you want displayed:\n1. Overall stats\n2. list of names by infection "\
                + "status\n3. An individual's record", 1, 3)

                if statChoice == 1:
                    #overall stats
                    pop.generateStatistic()
                elif statChoice == 2:
                    #list names by infection type
                    #stored in Linked lists since we don't know how many there are in each 
                    #category
                    susc = DSALinkedList()
                    car = DSALinkedList()
                    inf = DSALinkedList()
                    recov = DSALinkedList()
                    dead = DSALinkedList()

                    #go through each vertex(person) and store their name in the corresponding 
                    #list
                    for vert in graph.vertices:
                        if vert.value.inf.status == 0:
                            susc.insertLast(vert.value.name)
                        elif vert.value.inf.status == 1:
                            car.insertLast(vert.value.name)
                        elif vert.value.inf.status == 2:
                            inf.insertLast(vert.value.name)
                        elif vert.value.inf.status == 3:
                            recov.insertLast(vert.value.name)
                        elif vert.value.inf.status == 4:
                            dead.insertLast(vert.value.name)
                    
                    #print the lists, the titles will be in the corresponding colour
                    print(fg("\nSusceptible:\n", 255))
                    for name in susc:
                        print(name)

                    print(fg("\nCarrier:", 226))
                    for name in car:
                        print(name)
                        
                    print(fg("\nInfected:", 9))
                    for name in inf:
                        print(name)
                    print(fg("\nRecovered:", 46))
                    for name in recov:
                        print(name)
                    print(fg("\nDead:", 240))
                    for name in dead:
                        print(name)
                    print()
                else:
                    #individual record
                    #Note this is pretty much the same as choice 4,1
                    print("Please enter the name of the person whose record you would like "\
                    + "to view")
                    name = input()

                    vert = graph.getVertex(name)
                    if vert == None:
                        print("Couldn't find", name, "in the network")
                    else:
                        print(vert.value)

        elif choice == 9:
            #run a timestep 
            if graph.numVertices == 0:
                print("You have to load or generate a graph first!")
            elif trans_rate == None:
                print("You have to set rates in order to run a timestep!")
            else:
                print("Running timestep...")
                timestep(graph, pop, gov, con, trans_rate, car_rate, car_inf_rate, death_rate, \
                recov_rate, numWeeks)
                
                #each iteration represents a fortnight
                numWeeks += 2
            
        elif choice == 10:
            #save graph
            if graph.numVertices == 0:
                print("You have to load or generate a network first!")
            else:
                print("Please enter the filename you would lik to save your network in:")
                filename = input()

                #If you create a graph at max size, and then add nodes and edges to the graph, 
                #trying to save it may cause pickle to segfault, you'd need to add quite a lot 
                #tho
                graph.saveGraph(filename)
                print("Successfully saved graph")
        choice = inputInt(prompt, 0, 10)


if __name__ == '__main__':
    Menu()
