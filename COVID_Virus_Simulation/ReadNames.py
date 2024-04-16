####
# ReadNames.py
#
# Date created: 28/02/2020
# Date last edited: 27/05/2020
#
# Author: Stephen den Boer  19761257
#
# Overview:
#   Reads in the random student names, and return as an array of student objects.
#   updated to use a linked list of students so they can be removed. It also just returns the 
#   names themselves not the student object
#
#   NOTE This was adapted from something originally submitted for practical 1. The RandNames.csv
#   file was also supplied for practical 1. The code from practical 1 was adapted for practicals
#   5, 8 and 9 and submitted for those practicals. It has since been adaprted for the assignment
####

#NOTE submitted fro prac 5. the csv file is also not mine.......
    #ik that there's no duplicates from when this dataset was used to sort by name in prac 01

import numpy as np
from DSALinkedList import *


class Student():
#A class to hold the student information    
    def __init__(self, ID, firstName, lastName):
        self.ID = ID
        self.firstName = firstName
        self.lastName = lastName
    #END __init__

    #definig a less than function which allows objects to be compared based on 
    #their ID. When two objects are compared in a sort, this method will 
    #automatically be used
    def __lt__(self, other):     
        return self.ID < other.ID

    def __str__(self):
        return str(self.firstName) + ' ' + str(self.lastName) \
        + " Student ID: " + str(self.ID)

#END class Student


def readFile(filename, LL):
    try:    #fileIO has lots of potential for error, so need to catch erros
        openFile = open(filename)

        lineNum = 0
        line = openFile.readline()  #reading file one line at a time

        while line:
            line = line.replace("\n", "")
            #processLine will return a name to be stored in the array
            #arr[lineNum] = processLine(line)

            LL.insertLast(processLine(line))
            lineNum += 1
            line = openFile.readline()
        #END while
        
        openFile.close()

    #END try

    except IOError as err: 
        print("Error in file processing: " + str(err))
    #END except

#END readFile


def processLine(line):
    #split csv file into its diffferent elements
    elements = line.split(",")

    #only using the name
    return elements[1] 

#END processLine


def getNames():
    #creating an array to hold all the student data
    
    students = DSALinkedList()
    #students = np.empty(7000, dtype = object)
    readFile("RandNames.csv", students) 


    return students 
#END main
    
