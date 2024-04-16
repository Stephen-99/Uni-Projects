/*  FileIO.c
overview:       Contains the FilIO methods, to open and read a file. 
                Modified from UCP assignment
date created:   24/04/2020
last edited:    06/05/2022
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "FileIO.h"
#include "LinkedList.h"

/*This function is responsible for opening a file Calling code should check if 
returned file is NULL in order to determine if opening was successful*/
FILE* OpenFile(char* filename, char* readOrWrite)
{
    char msg[60] = {"Could not open \0"};
    
    FILE *openFile = fopen(filename, readOrWrite);
    
    /*if file doesn't exist*/
    if (openFile == NULL)
    {
        strncat(msg, filename, 45);
        perror(msg);
    }
    return openFile;
}

/*This function is responsible for reading input. A return value of 1, means the 
file was succesfully read, 0 means an error occured*/
int ReadFile(char* filename, LinkedList* list)
{
    int returnVal = 0;  /*Default invalid*/
    char buffer[LINE_MAX];
    FILE* file = OpenFile(filename, "rb");

    if (file != NULL)
    {
        if (fgets(buffer, LINE_MAX, file) == NULL)
        {
            fclose(file);
            return 0;
        }
        
        fclose(file);

        //read from file
        int totalNum = 0;
        int numLen, numCylinders;
        int *num = NULL;
        num = (int*)malloc(sizeof(int));
        
        if (1 == sscanf(buffer + totalNum, "%d%n ", num, &numLen))
        {
            numCylinders = *num;
            totalNum += numLen;
            InsertLast(list, num);
            num = (int*)malloc(sizeof(int));
        }

        //Read numbers seperated by spaces while they exist
        while (1 == sscanf(buffer + totalNum, "%d%n ", num, &numLen))
        {
            if (*num >= numCylinders)
            {
                printf("Seek request must be less than the number of cylinders!");
                break;
            }

            totalNum += numLen;
            InsertLast(list, num);
            num = (int*)malloc(sizeof(int));
        }

        //Must be at least 4 numbers: n, curLocation, prevReq and nextReq(s)
        if (list->length >= 4)
        {
            returnVal = 1;
        }
        free(num);
    }
    return returnVal;
}

