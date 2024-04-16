/* Missile.c
overview:       This holds functions responsible for maintaining a missile struct. A missile 
                provides the user a way to shoot a tile (or multiple) on the board
date created:   13/05/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Missile.h"
#include "UserInterface.h"

/*This function creates a pointer to a missile struct. This is then stored in a linked list to 
keep track of the missiles when playing the game. When creating the missile the type of missile 
is determined in order to set the 3 fields for a missile struct*/
Missile* createMissile(char* type)
{
    Missile* missile = (Missile*)malloc(sizeof(Missile));
    if (!whichMissile(missile, type))
    {
        free(missile);
        missile = NULL;
    }
    return missile;
}

/*This function deterrmines which type a missile is and stores that information in the missile 
itself. It is called everytime a new missile is created. a return value of 1 means a valid 
missile, while 0 means the missile was invald*/
int whichMissile(Missile* missile, char* type)
{
    int returnVal = 1;  
    /*convert to uppercase to allow any mixed case to be accepted*/
    toUpper(type);
    if (strcmp(type, "SINGLE") == 0)
    {
        missile->msg = "Missile hits a single tile\0";
        missile->execute = &single;
        missile->type = "Single\0";
    }
    else if (strcmp(type, "V-LINE") == 0)
    {
        missile->msg = "Missile hits an entire column of tiles\0";
        missile->execute = &v_line;
        missile->type = "V-Line\0";
    }
    else if (strcmp(type, "H-LINE") == 0)
    {
        missile->msg = "Missile hits an entire row of tiles\0";
        missile->execute = &h_line;
        missile->type = "H-Line\0";
    }
    else if (strcmp(type, "SPLASH") == 0)
    {
        missile->msg = "Missile hits a 3x3 square, centered around given coordinate\0";
        missile->execute = &splash;
        missile->type = "Splash\0";
    }
    else
    {
        returnVal = 0;
    }
    
    return returnVal;
}

/*Missile Execute functions:
*
*   NOTE: Coord->vert is not 0-indexed so need to 0-index it myself
*   A return value of 0 means there are still ships to be sunk, as per the hitShip function,
*   missiles which hit multiple tiles may have a return value greater than 1, since the missile
*   hits all the relavent tiles even if there are no ships left to sink
*/

/*this function is for a single type missile and fires at a single coordinate on the board*/
int  single(Board* board, Coord* coord)
{
    int returnVal = hitShip(board, coord->vert - 1, coord->hozI);

    return returnVal;
}

/*This function is for a v_line missile, which fires at an entire vertical line on the board*/
int  v_line(Board* board, Coord* coord)
{
    int ii, returnVal;
    returnVal = 0;

    /*loop through every row index and hit the tile at the given column*/
    for (ii = 0; ii < board->height; ii++)
    {
        returnVal += hitShip(board, ii, coord->hozI);
    }

    return returnVal;
}

/*This function is for a h_line missile and fires at an entire horizontal line on the board*/
int  h_line(Board* board, Coord* coord)
{
    int ii, returnVal;
    returnVal = 0;

    /*loop through every column index and hit the tile at the given row*/
    for (ii = 0; ii < board->width; ii++)
    {
        returnVal += hitShip(board, coord->vert - 1, ii);
    }
    
    return returnVal;
}

/*This function is for a splash missile and fires at a 3x3 square on the board centred at the 
coordinate passed*/
int  splash(Board* board, Coord* coord)
{
    int ii, jj, returnVal;
    returnVal = 0;
    /*loops through the vertical index from 1 b4 the given value to 1 after. Note: coord->vert 
    is not 0-indexed*/
    for (ii = coord->vert - 2; ii <= coord->vert; ii++)
    {
        /*loops through th e horizontal coordinated from 1 b4 the given value, to 1 after. Note:
        corrd->hozI is 0 indexed*/
        for (jj = coord->hozI - 1; jj <= coord->hozI + 1; jj++)
        {
            /*only want to try hit a ship if its within the baord*/
            if (checkInt(ii, 0, board->height - 1))  
            {
                if (checkInt(jj, 0, board->width - 1))   
                {
                    returnVal += hitShip(board, ii, jj);
                }
            }
        }
    }

    return returnVal;
}

/*This function allows the missile type to be printied. It takes a void* so that it can be used
with the linked list to print all the missiles in a list*/
void printMissile(void* data)
{
    Missile* m = (Missile*)data;
    printf("- %s\n", m->type);
}

/*This function frees the missile, again taking a void* pointer so that it can be easily freed 
with the linked list*/
void freeMissile(void* data)
{
    Missile* m = (Missile*)data;
    free(m);
}
