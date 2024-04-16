####
# TestHealthStatus.py
#
# Date created:     02/05/2020
# Date last edited: 02/05/2020
#
# Author: Stephen den Boer  19761257
#
# Overview:
#   Test harness for the HealthStatus class
#   #NOTE This HAS NOT been updated to reflect the current health status class
#    
####

from HealthStatus import *
import traceback

def main():
    numTests = 0
    numPassed = 0

    #Test1 - creation
    print("Testing initialisation of Health Status:\n")
    numTests += 1
    try:
        status = HealthStatus(3)
        print(status)
        assert (status.status.peekLast() == 3), "Added wrong value"
        numPassed += 1

    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")

    
    #Test2 - add status
    print("Testing adding additional stati:\n")
    numTests += 1
    try:
        nums = [1, 4, 5]
        for ii in nums:
            status.add(ii)
        print(status)
        print()

        nums.insert(0, 3)
        ii = 0
        for status in status.status:
            assert (status == nums[ii]), "added status inncorrectly. Expected: " + str(nums[ii])\
            + " but got: " + str(status)
            ii += 1
        numPassed += 1
    except Exception as err:
        print("Failed: ", err)
        traceback.print_tb(err.__traceback__)
    print("--------------------------------------------")


    #Test333 - get Risk factor


    #RESULTS
    print("\nRESULTS:")
    print("============================================")
    print("Number passed: \t\t", numPassed)
    print("Number of tests:\t", numTests)
    print("Percentage passed:\t", str(numPassed/numTests*100) + "%")

#END main

if __name__ == '__main__':
    main()

