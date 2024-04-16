/* start.c
overview:       This file contains the main function which is the starting point for the program
date created:   25/04/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "LinkedList.h"
#include "FileIO.h"
#include "Board.h"
#include "UserInterface.h"
#include "Missile.h"
#include "Menu.h"

/*This is the main function which calls the relavent functions to read the files and run the 
program*/
int main(int argc, char** argv)
{
    /*require at least 2 arguments: a board file and a missile file*/
    if (argc < 2)
    {
        print("not enough arguments given!");
    }
    else
    {   
        /*Attempting to read the board file*/
        Board* board = createBoard();
        int boardIsRead = readBoard(argv[1], board);
        if (boardIsRead == 1) 
        {   /*success!*/
            /*Atempting to read the missiles file*/
            LinkedList *missiles = NULL;
            missiles = createLinkedList();
            if (readMissiles(argv[2], missiles))
            {   /*success!*/
                /*calling the menu to run the program*/
                menu(board, missiles);
            }
            freeList(missiles, &freeMissile);
            freeBoard(board);
        }
        else if (boardIsRead == -1) 
        {       /*board is invalid, function returned b4 initialising board, therfore needs to be
                freed differently*/
            free(board);
        }
        else
        {       /*board is invalid, function returned after initalisng board, therfore can be 
                freed normally*/
            freeBoard(board);
        }    
    }

    return 0;
}
