####
# Government.py
#
# Date created:     25/05/2020
# Date last edited: 30/05/2020
#
# Author: Stephen den Boer  19761257
#
# Overview: This has all the information about the government and what measures they are 
#           putting in place. The measures are stored in order of severity so that if a higher 
#           measure is in place all lower measures will also be in place
#    
####

import numpy as np

#NOTE: Future improvments:  should maybe implement a way to keep track of how long each measure 
#has been in place to 

#simulate mounting social pressure to remove the measure

class Government():
    
    #uses an array to keep track of which measures are in place. If indec 0 contains true then 
    #that measure is in place etc.
    def __init__(self, precaution):
        #Should be 0-2, 2 is most precautious
        self.precaution = precaution
        self.measures = np.full(9, False, dtype = bool)
        
        self.measures[0] = True

    #String returned for when the precaution is put in place
    def _add2Str(self, precaution):
        if precaution == 1:
            returnStr = "prohibited public gatherings greater than 50 people"
        elif precaution == 2:
            returnStr = "closed universities"
        elif precaution == 3:
            returnStr = "implemented social distancing"
        elif precaution == 4:
            returnStr = "prohibited all gatherings greater than 20 people"
        elif precaution == 5:
            returnStr = "closed schools"
        elif precaution == 6:
            returnStr = "asked everyone to work from home"
        elif precaution == 7:
            returnStr = "prohibited all gatherings greater than 10 people"
        elif precaution == 8:
            returnStr = "Forced everyone into complete lockdown"
        
        return returnStr

    #string returned for when the precaution is removed
    def _remove2Str(self, precaution):
        if precaution == 1:
            returnStr = "allowed public gatherings greater than 50 people"
        elif precaution == 2:
            returnStr = "re-opened universities"
        elif precaution == 3:
            returnStr = "removed social distancing"
        elif precaution == 4:
            returnStr = "allowed all gatherings up to 50 people"
        elif precaution == 5:
            returnStr = "re-opened schools"
        elif precaution == 6:
            returnStr = "allowed everyone to work in person"
        elif precaution == 7:
            returnStr = "allowed all gatherings up to 20 people"
        elif precaution == 8:
            returnStr = "removed lockdown state"
        
        return returnStr
            
    #ratio infected will be between 0 and 1
    def updateMeasures(self, ratioInfected, connects):    
        #level of precaution squared so that government has greater impact than ratio infected
        # x7 to avoid getting to small as a result of being squared

        #each time checking to see which measures should be applied. If a measure is applied, 
        #all measures above it will be removed, and all measures below it will be applied

        #The values have been tested to see which proportion of values fall into each category
        #I can't submit that code since it uses the built-in list :O
        if (ratioInfected * self.precaution * self.precaution * 7) > 2.0: 
            self._updateMeasures(8, connects) 
        elif (ratioInfected * self.precaution * self.precaution * 7) > 1.0: 
            self._updateMeasures(7, connects) 
        elif (ratioInfected * self.precaution * self.precaution * 7) > 0.5: 
            self._updateMeasures(6, connects) 
        elif (ratioInfected * self.precaution * self.precaution * 7) > 0.30: 
            self._updateMeasures(5, connects) 
        elif (ratioInfected * self.precaution * self.precaution * 7) > 0.20: 
            self._updateMeasures(4, connects) 
        elif (ratioInfected * self.precaution * self.precaution * 7) > 0.15: 
            self._updateMeasures(3, connects) 
        elif (ratioInfected * self.precaution * self.precaution * 7) > 0.10: 
            self._updateMeasures(2, connects) 
        elif (ratioInfected * self.precaution * self.precaution * 7) > 0.05: 
            self._updateMeasures(1, connects) 
        else:
            self._updateMeasures(0, connects)

    def _updateMeasures(self, num, connects):

        #set measures above current measure to false
        for ii in range(num + 1, 9):
            if self.measures[ii]:
                self.measures[ii] = False
                print("In response to the easing of the virus, the government has", \
                self._remove2Str(ii))
                
                self._removeMeasure(ii, connects)


        #set the methods below and including, to true
        for ii in range(1, num + 1):
            if not self.measures[ii]:
                self.measures[ii] = True
                print("In response to spread of the virus the government has", \
                self._add2Str(ii))

                self._applyMeasure(ii, connects)
    #END _updateMeasures

    #applies the effect of each measure
    def _applyMeasure(self, measure, connects):
        if measure == 1:
            #public gatherings must be <= 50 ppl
            #update all connections except those living in the same house
            for ii in range(1, 9):
                connects.types[ii] *= 0.95
        elif measure == 2:
            #close universities
            connects.updateValue(3, 0)
        elif measure == 3:
            #social distancing
            for ii in range(1, 9):
                connects.types[ii] *= 0.8
        elif measure == 4:
            #gatherings must be <= 20 ppl
            for ii in range(1, 9):
                connects.types[ii] *= 0.95
        elif measure == 5:
            #schools closed
            connects.updateValue(2, 0)
        elif measure == 6:
            #work from home
            connects.updateValue(1, 0)
        elif measure == 7:
            #gatherings must be <= 10 ppl
            #closing sports teams/gyms and religious services
            connects.updateValue(6, 0)
            connects.updateValue(7, 0)
        elif measure == 8:
            #Lockdown!
            #can't see friends, and only very small chance of pasing on to neighbours
            connects.updateValue(5, 0)
            connects.updateValue(8, 0.1)
    #END _applyMeasure

    #will reverse changes from _applyMeasure
    def _removeMeasure(self, measure, connects):
        if measure == 1:
            #public gatherings can be > 50 ppl
            for ii in range(1, 9):
                connects.types[ii] /= 0.95
        elif measure == 2:
            #open universities
            connects.resetValue(3)
            connects.types[3] *= 0.95   #measure 1 still in place
        elif measure == 3:
            #remove social distancing
            for ii in range(1, 9):
                connects.types[ii] /= 0.8
        elif measure == 4:
            #gatherings can be > 20 ppl, still <= 50
            for ii in range(1, 9):
                connects.types[ii] /= 0.95
        elif measure == 5:
            #schools open
            connects.resetValue(2)
            connects.types[2] *= (0.95 * 0.8 * 0.95)    #measures 1-4 still in place
        elif measure == 6:
            #work normally
            connects.resetValue(1)
            connects.types[1] *= (0.95 * 0.8 * 0.95)    #measures 1-5 still in place
        elif measure == 7:
            #gatherings can be > 10 ppl but still <= 20
            #re-open sports teams/gyms and religious services
            connects.resetValue(6)
            connects.resetValue(7)
            connects.types[6] *= (0.95 * 0.8 * 0.95)    #measures 1-6 still in place
            connects.types[7] *= (0.95 * 0.8 * 0.95)    #measures 1-6 still in place
        elif measure == 8:
            #no more lockdown!
            #can see friends, and neighbours
            connects.resetValue(5)
            connects.resetValue(8)
            connects.types[5] *= (0.95 * 0.8 * 0.95)    #measures 1-7 still in place
            connects.types[8] *= (0.95 * 0.8 * 0.95)    #measures 1-7 still in place
    #END _applyMeasure
            

#END class

 
