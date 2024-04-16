####
# TestDSAGraph.py
#
# Date created:     23/04/2020
# Date last edited: 23/04/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   Tests the DSA graph class
#   NOTE this HAS NOT been updated to reflect the changes to the DSA graph class
#       As a result the graph only passes some of the tests
#   NOTE: I have recieved permission from Valerie to use the in-built list for testing
#       In order to store test values
#    
####

from DSAGraph import *
import traceback

#TODO update to test adding edges with weight and intamacy

def main():
    numTests = 0
    numPassed = 0
    verts = ['A', 'B', 'C', 'D', 'E', 'abd']

    #test1 creation
    print("Testing initialisation of Constructor:\n")
    numTests += 1
    try:
        graph = DSAGraph()
        if ((graph.numVertices != 0) or (graph.numEdges != 0)):
            raise Exception("Should be 0 vertices and edges")
        numPassed += 1
        print("Passed")
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")
    

    #Test2 add vertices
    print("Testing adding vertices:\n")
    numTests += 1
    try:
        for ch in verts:
            graph.addVertex(ch)
        try:
            graph.addVertex('A', 1)
            print("Failed to stop duplicate vertex being added")
        except VertexError as err:
            print("Error message:", err)
            numPassed += 1
            print("Passed")
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")


    #Test3 adding edges
    print("Testing adding edges:\n")
    numTests += 1
    try:
        graph.addEdge('A', 'B')
        graph.addEdge('C', 'B')
        graph.addEdge('D', 'B')
        graph.addEdge('E', 'B')
        try: 
            graph.addEdge('B', 'B')
            print("Failed: didn't stop creation of edge between the same "
            + "vertex")
        except EdgeError as err:
            print("Error message:", err)
            try:
                graph.addEdge('B', 'A')
                print("Failed to stop existing edge being created")
            except EdgeError as err:
                print("Error message:", err)
                numPassed += 1
                print("Passed")
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")

    #Test4 hasVertex
    print("Testing vertex finding:\n")
    numTests += 1
    try:
        for vert in verts:
            if not graph.hasVertex(vert):
                raise Exception("expected vertex was missing")
        #END for 
        numPassed += 1
        print("Passed")
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")

    #Test5 is adjacent
    print("Testing adjacent verticies:\n")
    numTests += 1
    results = []
    try:
        results.append(graph.isAdjacent('A', 'B'))
        results.append(graph.isAdjacent('C', 'B'))
        results.append(graph.isAdjacent('D', 'B'))
        results.append(graph.isAdjacent('E', 'B'))
        for adjacent in results:
            if not adjacent:
                raise Exception("Marked verticies as not adjacent when they "
                + "were expected to be adjacent")
        #END for
        numPassed += 1
        print("Passed")
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")

    #Test6 display
    print("Testing display:\n")
    numTests += 1
    try:
        graph.display()
        passed = input("You decide, does the display pass? (y/n): ")
        passed = passed[0].lower()
        if passed == 'y':
            print("Passed")
            numPassed += 1
        else:
            print("Failed")
        #END if-else
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")

    #Test7 - alpahbetical order
    print("Testing vertices are inserted alphabetically:\n")
    numTests += 1
    try:
        graph = DSAGraph()
        graph.addVertex('D', 1)
        graph.addVertex('B', 1)
        graph.addVertex('E', 1)
        graph.addVertex('A', 1)
        graph.addVertex('C', 1)
        graph.display()
        iter1 = iter(graph.vertices)
        if next(iter1).label == 'A':
            if next(iter1).label == 'B':
                if next(iter1).label == 'C':
                    if next(iter1).label == 'D':
                        if next(iter1).label == 'E':
                            print("Passed")
                            numPassed += 1
                        else:
                            print("Failed, not in alphabetical order")
                    else:
                        print("Failed, not in alphabetical order")
                else:
                    print("Failed, not in alphabetical order")
            else:
                print("Failed, not in alphabetical order")
        else:
            print("Failed, not in alphabetical order")
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")

    #Test8 Testing edges
    print("Testing edge objects:\n")
    numTests += 1
    try:
        graph.addEdge('A', 'B', True, 2)
        graph.addEdge('B', 'D', False, 4)
        graph.addEdge('B', 'A', True, 1)
        graph.addEdge('E', 'C', True, 3)
        graph.addEdge('C', 'D', True, 7)

        graph.displayE()

        iter1 = iter(graph.edges)
        if graph.numEdges == 6:
            if next(iter1).label == '1-B-A':
                if next(iter1).label == '2-A-B':
                    if next(iter1).label == '3-E-C':
                        if next(iter1).label == '4-B-D':
                            if next(iter1).label == '4-D-B':
                                if next(iter1).label == '7-C-D':
                                    print("Passed")
                                    numPassed += 1
                                else:
                                    print("Failed, not in alphabetical order")
                            else:
                                print("Failed, not in alphabetical order")
                        else:
                            print("Failed, not in alphabetical order")
                    else:
                        print("Failed, not in alphabetical order")
                else:
                    print("Failed, not in alphabetical order")
            else:
                print("Failed, not in alphabetical order")
        else:
            print("Failed, not in alphabetical order")
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")
                
    #Test 9 Depth first search
    print("Testing depth first search\n")
    numTests += 1
    try:
        graph.addEdge('D', 'C', True, 7)
        graph.addEdge('C', 'E', True, 7)
        graph.addEdge('A', 'E', False, 7)
        graph.addEdge('B', 'C', False, 7)
        graph.depthFirstSearch()
        print("passed")
        numPassed += 1
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")
    
    #Test 10 Breadth first search
    print("Testing breadth first search\n")
    numTests += 1
    try:
        graph.breadthFirstSearch()
        print("passed")
        numPassed += 1
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")



    #RESULTS
    print("\nRESULTS:")
    print("============================================")
    print("Number passed: \t\t", numPassed)
    print("Number of tests:\t", numTests)
    print("Percentage passed:\t", str(numPassed/numTests*100) + "%")

#END main




if __name__ == '__main__':
    main()


