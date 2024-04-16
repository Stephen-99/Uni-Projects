####
# HealthStatus.py
#
# Date created:     30/04/2020
# Date last edited: 30/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This is a class that holds the health status. Status is stored as an integer, but can be 
#   represented as text. each status has an associated risk factor.
#    
####

from DSALinkedList import *

class HealthStatus():

    #This class uses a linked list in order to have multiple conditions. The simulation does not
    #currently exploit this feature, but could in the future

    def __init__(self, status):
        self.status = DSALinkedList()
        self.status.insertLast(status)
    #END __init__

    def __str__(self):
        returnStr = "Health condition:"
        for status in self.status:
            if status == 0:
                returnStr += "\n\t  healthy"
            elif status == 1:
                returnStr += "\n\t  high blood pressure"
            elif status == 2:
                returnStr += "\n\t  diabetic"
            elif status == 3:
                returnStr += "\n\t  kidney disease"
            elif status == 4:
                returnStr += "\n\t  heart condition"
            else: #status will be 5
                returnStr += "\n\t  lung condition"
                #Info on People who aree more susceptible to the virus: 
                #https://www.healthdirect.gov.au/coronavirus-covid-19-groups-at-higher-risk-faqs
            #END if-elif
        #END for
        return returnStr
    #END __str__

    def add(self, status):
        #We assume that this class will be used appropriately and only valid stati will be added
        #i.e. won't add other stati to helathy person, or duplicate stati. 
        self.status.insertLast(status) 
    #END add 

    #The conditions are ranked in order of severity in relation to the virus, so a higher 
    #condition has a higher risk
    def getRiskFactor(self):
        risk = 0
        for status in self.status:
            risk += (1+ (0.1 * status))
        return risk
    #END getRiskFactor

#END class
