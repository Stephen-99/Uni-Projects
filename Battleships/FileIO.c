/*  FileIO.c
overview:       Contains the FilIO methods, to read and create a missile list and a board struct
date created:   24/04/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "LinkedList.h"
#include "FileIO.h"
#include "UserInterface.h"
#include "Missile.h"

/*This function is responsible for opening a file for either reading or writing. It is used to 
read the board and missile files as well as to open files for writing missile and board files. 
Calling code checks if returned file is NULL in order to determine if opening was successful*/
FILE* openFile(char* filename, char* readOrWrite)
{
    char msg[60] = {"Could not open \0"};
    FILE *openFile = fopen(filename, readOrWrite);
    
    /*if file doesn't exist*/
    if (openFile == NULL)
    {
        /*ridiculously long filenames get truncated to 45 chars. This should be enough to 
        identify which file it is*/
        strncat(msg, filename, 45);
        /*prints msg to stderr, with additional information about what went wrong*/
        perror(msg);
    }
    return openFile;
}

/*This function is responsible for reading the board file into a board struct. It is called at 
the beginning of the program to allow the user to play the game. A return value fo 1, means the 
file was succesfully read into a board struct, -1 means an error occured before initialising the
board, which means the board is freed diferently. 0 means an error occured after initialising the
board and it can be freed normally*/
int readBoard(char* filename, Board* board)
{
    int returnVal = 0;  /*Default invalid*/
    char *lineCheck;
    char line[301];
    FILE* file = openFile(filename, "r");

    if (file != NULL)
    {
        returnVal = 1; /*File exists. Now we assume valid until proven otherwise*/

        /*read first line of file*/
        lineCheck = fgets(line, 301, file);
        if (lineCheck == NULL)
        {
            fprintf(stderr, "Board file is empty!\n");
            returnVal = -1;
        }
        /*obtain width and height from first line*/
        else if(sscanf(line, "%d, %d", &(board->width), &(board->height)) != 2)
        {
            fprintf(stderr, "Width and height of the board not stored correctly\n");
            returnVal = -1; /*error b4 board initialised*/
        }
        else
        {
            /*make sure width and height are between 1-12*/
            if (!(checkInt(board->width, 1, 12)) | (!checkInt(board->height, 
            1, 12)))
            {
                fprintf(stderr, "Board width and height must be between 1 and 12\n");
                returnVal = -1; /*error b4 board initialised*/
            }
            else
            {
                /*setting up arrays in board struct, width and height required b4 this step*/
                initBoard(board);
    

                /*reading the ships*/
                lineCheck = fgets(line, 301, file);

                /*Double check there is at least one ship*/
                if (lineCheck == NULL)
                {
                    fprintf(stderr, "There are no ships to sink!\n");
                    returnVal = 0;
                }

                while (lineCheck != NULL)
                {
                    if (ferror(file))
                    {
                        perror("Error reading from file");
                        returnVal = 0;
                    }
                    else
                    {
                        /*if the ship could not be read or added properly*/
                        if (!processShip(line, board))
                        {
                            lineCheck = NULL;
                            returnVal = 0;
                        }
                        /*continue to the next line*/
                        else
                        {
                            lineCheck = fgets(line, 301, file);
                        }
                    }
                }
            }
        }
        fclose(file);
    }
    return returnVal;
}

/*This function reads a line and proesses it as a ship. If it was a valid ship, the return value
will be 1, with 0 for an invalid ship. This function is used by the above function when reading 
the board*/
int processShip(char* line, Board* board)
{
    int returnVal = 1;
    
    Ship* ship = createShip();

    /*try parse the line into the ship format. requires direction and horizontal corrdinate to be
    valid */
    if (sscanf(line, "%1[A-La-l]%d %1[NESWnesw] %d %[^\n]",
        &(ship->head->hoz), &(ship->head->vert), &(ship->direction), 
        &(ship->length), (ship->name)) != 5)
    {
        fprintf(stderr, "Ship was stored incorrectly\n");
        returnVal = 0;
        freeShip(ship);
    }
    else
    {
        /*converts coordinate from letter to number*/
        if (!convertCoord(ship->head))
        {
            returnVal = 0;
            freeShip(ship);
        }
        else
        {
            /*check that vertical coordinate is valid and the length is within the maximum board
            size. If the length goes beyond the board, it will be caught when trying to add the 
            ship to the baord*/
            if (!(checkInt(ship->head->vert, 1, board->height)) 
            | !(checkInt(ship->length, 1, 12)))
            {
                fprintf(stderr, "Ship %s, goes outside the board!\n", ship->name);
                freeShip(ship);
                returnVal = 0;
            }
            else
            {
                /*attempt to add the ship to the board*/
                if (!addShip(ship, board))
                {
                    returnVal = 0;
                    freeShip(ship);
                }
            }
        }
    }
    return returnVal;
}

/*This function reads the missiles file into a linked list. A return value of 1, means the file
was succesfully read, while 0 indicates failure. This function is used at the start of the 
program to allow the user to play the game*/
int readMissiles(char* filename, LinkedList* missiles)
{
    int returnVal = 0;  /*assume invalid*/
    char *lineCheck;
    char line[20]; /*correct missile names should be 6 chars, i.e space 7, but could be invalid*/
    Missile* missile = NULL;

    /*open file for reading*/
    FILE* file = openFile(filename, "r");

    if (file != NULL)
    {
        /*File exists! Now assume valid till proven otherwise*/
        returnVal = 1; 

        /*read first line*/
        lineCheck = fgets(line, 20, file);

        /*check file isn't empty*/
        if (lineCheck == NULL)
        {
            returnVal = 0;
            fprintf(stderr, "Missile file is empty!\n");
        }
        /*read subsequent lines till the end of the file*/
        while (lineCheck != NULL)
        {
            if (ferror(file))
            {
                perror("Error reading from file");
                lineCheck = NULL;
                returnVal = 0;
            }
            else
            {
                /* 'splitting' the string on newlines which will mean the string ends at the 
                first newline. Assuming the input file is correct, that will be the newline at 
                the end of the string left there by fgets. If their is a newline in the middle 
                of the string, the file is in the wrong format and will fail either way.*/
                strtok(line, "\n");

                /*create a missile with the line to check its valid*/
                missile = createMissile(line);
                if (missile != NULL)
                {
                    /*Valid missile, adding to the list*/
                    insertLast(missiles, missile);
                    lineCheck = fgets(line, 20, file);
                }
                else
                {
                    /*Invalid missile, stop reading file*/
                    fprintf(stderr, "Invalid missile: %s\n", line);
                    lineCheck = NULL;
                    returnVal = 0;
                }
            }
        }
        fclose(file);
    }
    return returnVal;
}
