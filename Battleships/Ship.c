/*  Ship.c
overview:       Contains the functions responsible for maintaining the ship struct, which holds
                infromation about where it should be on the board, and keeps track of how many 
                times it ahss been hit
date created:   25/04/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Ship.h"
#include "UserInterface.h"

/*This function creates a pointer to a ship, setting default values for all the fields. This is
used when reading a board file and when creating a board file*/
Ship* createShip()
{
    Ship* ship = (Ship*)malloc(sizeof(Ship));
    ship->head = createCoord();
    ship->length = 0;
    ship->numHits = 0;
    ship->direction = '\0';
    ship->name = (char*)malloc(201 * sizeof(char));
    return ship;
}

/*This function returns the indices the ship should occupy in the board struct's ship array. This
is useful both when adding the ship to the array but also when freeing the ship from the array,
and setting all refrences to the ship to NULL.*/
ShipIndices* getShipIndices(Ship* ship)
{
    ShipIndices* indices = (ShipIndices*)malloc(sizeof(ShipIndices));
    
    ship->direction = toUpperCase(ship->direction);

    /*The will occupy the coordinate at its 'head' and coordinates behind that, according to how 
    long the ship is*/
    switch(ship->direction)
    {
        case 'N':
            indices->startH = ship->head->hozI;
            indices->endH = indices->startH;
            indices->startV = ship->head->vert;
            indices->endV = indices->startV + ship->length - 1;
            break;
        case 'S':
            indices->startH = ship->head->hozI;
            indices->endH = indices->startH;
            indices->endV = ship->head->vert;
            indices->startV = indices->endV - ship->length + 1;
            break;
        case 'W':
            indices->startV = ship->head->vert;
            indices->endV = indices->startV;
            indices->startH = ship->head->hozI;
            indices->endH = indices->startH + ship->length - 1;
            break;
        case 'E':
            indices->startV = ship->head->vert;
            indices->endV = indices->startV;
            indices->endH = ship->head->hozI;
            indices->startH = indices->endH - ship->length + 1;
            break;
        default:
            print("Direction not stored in the correct format");
            indices = NULL;
    }
    if (indices != NULL)
    {
        /*need to subtract 1 since array is 0-indexed. Not needed horizontally, 
        since taken care of when converting coord from char to int*/
        indices->startV -= 1;
        indices->endV -= 1;
    }
    return indices;
}

/*This function returns the ship as a string that can be printed to file. This is useful when 
creating a board file, potentially writing multiple ships to file*/
char* getShipString(Ship* ship)
{
    char* str = (char*)malloc(220 * sizeof(char));  /*name is 201...*/
    sprintf(str, "%c%d %c %d %s", ship->head->hoz, ship->head->vert, ship->direction, 
        ship->length, ship->name);
    return str;
}

/*This allows a deep copy of the ship to be made so that a deep copy of the baord can be made so
that the user can play the game consecutive times with the same board*/
void copyShip(Ship* source, Ship* dest)
{
    dest->length = source->length;
    dest->direction = source->direction;
    dest->numHits = source->numHits;
    strcpy(dest->name, source->name);
    copyCoord(source->head, dest->head);
}

/*This function frees the ship and is called when the board is being freed*/
void freeShip(Ship* ship)
{
    freeCoord(ship->head);
    free(ship->name);
    free(ship);
    ship = NULL;
}
