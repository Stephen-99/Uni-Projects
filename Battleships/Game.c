/* Game.c
overview:       This file is responsible for running the game. It updates the board iteratively
                until the missiles run out, in which case the user lost, or all the ships are
                sunk, in which case the user won
date created:   25/04/2020
last edited:    22/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include "Game.h"
#include "LinkedList.h"
#include "Missile.h"
#include "UserInterface.h"

/*this function runs the play game option from the menu. It displays and updates the board every 
iteration, until there are no more missiles or the player has won by sinking all the ships*/
void playGame(Board* board, LinkedList* missiles)
{
    int ii, numLeft, won;
    char* msg;
    Missile* m = NULL;
    Coord* coord = NULL;
    numLeft = missiles->length - 1;

    /*this will cause ships to appear as magenta unshot tiles*/
    #ifdef DEBUG
    hitShips(board);
    #endif

    printBoard(board);
    ii = 0;
    won = 0;    
    while ((ii < missiles->length) & (!won))
    {
        printf("Remaning missiles: %d\n", numLeft);
        /*get current missile*/
        m = (Missile*)peekAny(missiles, ii);
        msg = joinStr("Current missile: ", m->type);
        print(msg);
        printf("\n"); 
        free(msg);

        /*obtain coordinate*/
        coord = inputCoord("Please enter a coordinate to target:", board->height, board->width);
        if (coord == NULL)
        {   
            /*input coord returns null if the user entered 'help', so now printing information
            about how the missile works. Printed in cyan to stand out*/
            printf(CYAN "%s\n" RESET, m->msg);
        }
        /*all the execute functions return 0 if the hitShip function returned 0, i.e. if there is
        still ships to sink. They return 1 or more if all ships have been sunk*/
        else if (m->execute(board, coord) != 0)
        {
            print(CYAN "Congratulations! You sunk all the ships" RESET);
            won = 1;
        }
        else
        {
            ii++;
            numLeft--;
        }
        freeCoord(coord);
        printBoard(board);
    }
    if (!won)
    {
        print("Game over.\n");
    }
}

