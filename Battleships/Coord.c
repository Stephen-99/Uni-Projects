/* Coord.c
overview:       Contains the functions responsible for maintaining a coordinate struct
                struct. This struct is used to represent a position on the board
date created:   14/05/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include "Coord.h"
#include "UserInterface.h"

/*This function creates a pointer to a coord struct setting appropriate default values for all 
the fields. This is used when obtaining coordinates from file and from the user*/
Coord* createCoord()
{
    Coord* coord = (Coord*)malloc(sizeof(Coord));
    coord->vert = -1;
    coord->hoz = '-';
    coord->hozI = -1;
    return coord;
}

/*converts the character value for the horizontal axis to a numerical index. This is required 
in order for the coordinate to represent a specific array element for the arrays in the board 
struct*/
int convertCoord(Coord* coord)
{
    int returnVal = 1;
    /*convert char to uppercase to allow lower and uppercase*/
    coord->hoz = toUpperCase(coord->hoz);
    switch(coord->hoz)
    {
        case 'A':
            coord->hozI = 0;
            break;
        case 'B':
            coord->hozI = 1;
            break;
        case 'C':
            coord->hozI = 2;
            break;
        case 'D':
            coord->hozI = 3;
            break;
        case 'E':
            coord->hozI = 4;
            break;
        case 'F':
            coord->hozI = 5;
            break;
        case 'G':
            coord->hozI = 6;
            break;
        case 'H':
            coord->hozI = 7;
            break;
        case 'I':
            coord->hozI = 8;
            break;
        case 'J':
            coord->hozI = 9;
            break;
        case 'K':
            coord->hozI = 10;
            break;
        case 'L':
            coord->hozI = 11;
            break;
        default:
            returnVal = 0;
     }
     return returnVal;
}

/*Creates a copy of the coordinate, this is usefule when creating a copy of a ship*/
void copyCoord(Coord* source, Coord* dest)
{
    dest->hoz = source->hoz;
    dest->vert = source->vert;
    dest->hozI = source->hozI;
}

/*This function frees the coors struct. This could just as easiy be done with free(), but this 
makes it easier to update in the future if the coord struct changes, since changes only have to 
be made to this file in terms of freeing*/
void freeCoord(Coord* coord)
{
    free(coord);
}
