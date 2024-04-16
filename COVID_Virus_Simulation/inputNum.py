###############################################################################
# Author:               Stephen den Boer                                      #
# input.py:             contains functions for obtaining numerical input      #
# Date created:         16/09/2019                                            #
# Date last editied:    16/09/2019                                            #
# Submitted for practical 6 in Fundamentals of programming sem 2 2019         #
###############################################################################

def inputInt(prompt, min, max):

    error = "ERORR: value must be between " + str(min) + " and " + str(max)
    outStr = prompt    
    value = min - 1
    
    while value < min or value > max: 
        try:
            print(outStr)
            value = int(input())
            outStr = error + "\n" + prompt
        except ValueError:
            print()
            outStr = "ERROR: input must be an integer\n" + prompt
        #END try except
    #END while
    return value
#END inputInt


def inputFloat(prompt, min, max):

    error = "ERORR: value must be between " + str(min) + " and " + str(max)
    outStr = prompt    
    value = min - 1
    
    while ((value < min) or (value > max)): 
        try:
            print(outStr)
            value = float(input())
            outStr = error + "\n" + prompt
        except ValueError:
            print()
            outStr = "ERROR: input must be an integer\n" + prompt
        #END try except
    #END while
    return value
#END inputFloat





