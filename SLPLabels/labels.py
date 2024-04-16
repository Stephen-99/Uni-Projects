#to store or read/write the labels.
#Will use a labelsModel.py NN to classify it or something

import os
import numpy as np
from ImageGUI import *

from Position import *
from Errors import *

#TODO:
    #load the current numpy array.
        #go through each entry, and re-obtain the final output label based on the 1 and 2 labels, 
        # cos the position thing was wrong and some of them will be wrong.

def main():
    FILENAME = "Position_labels.npy"
    NUM_PARTICIPANTS = 109 #102 for danaLab and 7 for simLab
    NUM_SAMPLES_PER_PARTICIPANT = 45

    #1-hot encoding --> when classifying use confidence intervals
    if (os.path.exists(FILENAME)):
        with open(FILENAME, 'rb') as labels_file:
            label_confidences = np.load(labels_file)
    else:
        label_confidences = np.zeros((NUM_PARTICIPANTS, NUM_SAMPLES_PER_PARTICIPANT, 3), dtype=float)

    label_confidences = userClassify(label_confidences, startParticipant=21, endParticipant=30)

    np.save(FILENAME, label_confidences)

def userClassify(labels, startParticipant=1, startSample=1, endSample=45, endParticipant=3):
    image_thread = ImageGUI()
    image_thread.start()
    for ii in range(startParticipant, endParticipant+1):
        parentFolder = "danaLab/" + (5-len(str(ii))) * '0' + str(ii) + '/'
        for jj in range(startSample, endSample+1):
            imgPath = parentFolder + 'RGB/uncover/image_' + (6-len(str(jj))) * '0' + str(jj) +'.png'
            image_thread.set_image_path(imgPath)

            #allows re-peating for the last value only
            if not getValues(labels, ii, jj):
                print("\nGOING BACK 1 picture to get new input. IMAGE WILL NOT CHANGE\n")
                getValues(labels, ii, jj-1)
                getValues(labels, ii, jj)
    image_thread.stop()
    return labels

def getValues(labels, ii, jj):
    try:
        v1 = inputValue("P" + str(ii) + " S" + str(jj) + "    1. Back, 2. Stomach, 3. Left side, 4. Right side, 0. Other: ", 0, len(Position.S1_POSITIONS))
        v2 = inputValue("P" + str(ii) + " S" + str(jj) + "    1. Hands by side, 2. Hands under head, 0. Other ", 0, len(Position.S2_POSITIONS))

        while (v1 == None) or (v2 == None):
            print("RETRYING")
            v1 = inputValue("P" + str(ii) + " S" + str(jj) + "    1. Back, 2. Stomach, 3. Left side, 4. Right side, 0. Other: ", 0, len(Position.S1_POSITIONS))
            v2 = inputValue("P" + str(ii) + " S" + str(jj) + "    1. Hands by side, 2. Hands under head, 0. Other ", 0, len(Position.S2_POSITIONS))
        labels[ii-1][jj-1][0] = v1
        labels[ii-1][jj-1][1] = v2
        labels[ii-1][jj-1][2] = Position.getPos(labels[ii-1][jj-1][0], labels[ii-1][jj-1][1])
    except GoBackException as e:
        return False
    return True



def printLabelOptions(labelOptions):
    for ii, label in enumerate(labelOptions):
        print(f"{ii}. {label}")

#Integer values only. 99 will cause it to retry. 101 will cause it to go back to previous.
def inputValue(prompt, min, max):
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
        if value == 99:
            return None
        if value == 111:
            raise GoBackException("Need to go back to previous input.")
    return value 

if __name__ == "__main__":
    main()
