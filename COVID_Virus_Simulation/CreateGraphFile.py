####
# CreateGraphFile.py
#
# Date created:     28/05/2020
# Date last edited: 28/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This file allows a serialized graph file to be generated. This is particularly useful for
#   running the parameter sweep since a graph doesn't need to be generated each time
#   This method should not be called directly, but only to be used by other programs
#    
####

from CreateNetwork import *

import sys
import time

def main():
    if len(sys.argv) < 4:
        print("Not enough arguments provided")
    else:
        t1 = time.time()
        #python3 CreateGraphFile.py filename numPpl %infected
        graph = init(int(sys.argv[2]), float(sys.argv[3]))

        graph.saveGraph(sys.argv[1])
        print("Time taken creating and saving graph: ", time.time() - t1)
#END main
        

if __name__ == '__main__':
    main()

    
