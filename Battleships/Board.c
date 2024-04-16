/* Board.c
overview:       This holds functions responsible for maintaining a board struct which holds all 
                the information about the shize and shape of the board as well as keeping track
                of the ships on the board
date created:   25/04/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Board.h"
#include "UserInterface.h"

/*This function creates a pointer to a board struct, setting initial values for each field. Its 
used to create a board to read the board file into, to copy the board when playing the game and 
to create a board file*/
Board* createBoard()
{
    Board* board = (Board*)malloc(sizeof(Board));
    board->width = 0;
    board->height = 0;
    board->numUnsunkShips = 0;
    board->boardArr = NULL;
    board->shipArr = NULL;
    return board;
}

/*This function is called after the board has been created and the width and height are known, in
order to initialise the two arrays. The reason for this is that the width and height are 
required in order to know how many elements to malloc for the arrays.*/
void initBoard(Board* board)
{
    int ii, jj;

    /*Mallocing 2D array of strings*/
    board->boardArr = (char***)malloc(board->height * sizeof(char**));
    for (ii = 0; ii < board->height; ii++)
    {
        board->boardArr[ii] = (char**)malloc(board->width * sizeof(char*));
    }
    
    /*Mallocing 2D array of ship pointers*/
    board->shipArr = (Ship***)malloc(board->height * sizeof(Ship**));
    for (ii = 0; ii < board->height; ii++)
    {
        board->shipArr[ii] = (Ship**)malloc(board->width * sizeof(Ship*));
    }

    /*Setting default values for the arrays*/
    for (ii = 0; ii < board->height; ii++)
    {
        for (jj = 0; jj < board->width; jj++)
        {
            /*UNSHOT will be a string representation of an unshot position*/
            board->boardArr[ii][jj] = UNSHOT;   
            board->shipArr[ii][jj] = NULL;
        }
    }
}

/*This function checks that a ship is valid for the board, and adds it to the ship array if it 
is. This function is used when reading in the board file and/or when creating a board file a 
return value of 1 means the ship was succesfully added with 0 indicating an unsucessful attempt*/
int addShip(Ship* ship, Board* board)
{
    int returnVal, ii, jj;
    /*get the indices of the positions the ship will occupy on the board*/
    ShipIndices* indices = getShipIndices(ship);
    returnVal = 1;  /*ship is valid until proven otherwise*/

    /*check that the ship is within the board*/
    if ((indices->startV < 0) | (indices->endV >= board->height) | (indices->startH < 0) 
        | (indices->endH >= board->width))
    {
        returnVal = 0;
        printf("Ship %s, goes outside the board!\n", ship->name);
    }
    else
    {
        /*try to add the ship to the ship array for all positions it should occupy*/
        int valid = 1;
        ii = indices->startV;
        while ((ii <= indices->endV) & (valid))
        {
            jj = indices->startH;
            while ((jj <= indices->endH) & (valid))
            {
                /*check postion doesn't already have an occupant*/
                if (board->shipArr[ii][jj] != NULL)
                {
                    printf("Ship %s overlaps with existing ships\n", ship->name);
                    returnVal = 0;
                    /*make sure we don't keep trying to add the ship!*/
                    valid = 0;

                    /*reseting values set in board, for the invalid ship, to null so another 
                    ship can be added without colliding with the partially added ship. This is 
                    relavent for creating a board file but not for reading a board file in*/
                    while (ii >= indices->startV)
                    {
                        while (jj > indices->startH)
                        {
                            jj--;
                            board->shipArr[ii][jj] = NULL;
                        }
                        ii--;
                    }
                }
                else
                {  
                    /*add the ship pointer to the array. There is multiple pointers pointing to 
                    the same ship so the 1 ship gets updated each time its hit. (provided ship 
                    has lengthe > 1 :D) */
                    board->shipArr[ii][jj] = ship;
                }
                
                jj++;
            }
            ii++;
        }
        /*If the ship was succesfully added to the array, increase the number of unsunk ships 
        on the board*/
        if (valid)
        {
            board->numUnsunkShips += 1;
        }
    }
    free(indices);
    return returnVal;
}

/*This function updates the board according to a shot taken at the location given. It is called
for each iteration when playing the game. A return value of 0 means there are still unsunk ships
on the board, whilst a return of 1 means all the ships have been sunk!*/
int hitShip(Board* board, int row, int col)
{
    int returnVal = 0;  
    /*if not an empty tile, i.e if it contains a ship*/
    if (board->shipArr[row][col] != NULL)
    {   
        /*if not already shot*/
        if (strcmp(board->boardArr[row][col], HIT) != 0)
        {
            /*update the board to represent a hit for the tile*/
            board->boardArr[row][col] = HIT;
            /*increase number of hits on ship*/
            board->shipArr[row][col]->numHits += 1;
            /*If all the tiles on the ship have been hit*/
            if (board->shipArr[row][col]->numHits == board->shipArr[row][col]->length)
            {   
                /*relay ships destruction*/
                printf(GREEN "Ship destroyed:" RESET " %s\n", board->shipArr[row][col]->name);
                board->numUnsunkShips -= 1;
                /*if all the ships have been sunk*/
                if (board->numUnsunkShips == 0)
                {   
                    /*let calling code know game is won*/
                    returnVal = 1;
                }
            }
        }
    }
    /*empty tile, updating board to represent a miss for that tile*/
    else
    {
        board->boardArr[row][col] = MISS;
    }
    return returnVal;
}

/*this function updates all unshot ships to represent magenta unshot tiles. This function is 
called when playing the game with debug enabled.*/ 
void hitShips(Board* board)
{
    int ii, jj;

    for (ii = 0; ii < board->height; ii++)
    {
        for (jj = 0; jj < board->width; jj++)
        {
            /*if there is a ship*/
            if (board->shipArr[ii][jj] != NULL)
            {
                /*if it has not been hit*/
                if (strcmp(board->boardArr[ii][jj], HIT) != 0)
                {   
                    /*make the tile magenta*/
                    board->boardArr[ii][jj] = MAGENTA "#" RESET;
                }
            }
        }
    }
}
/*This is the function that is responsible for displaying the board. When playing the game this
function is called for each iteration. The values in boardArr will be set up by hitShip(s) so 
no processing needs to be done in that regard*/
void printBoard(Board* board)
{
    int ii, jj, num;
    char letter = 'A';
    char *rowSep, *preSpace;

    /*setting initial values for the left most column. If there is 2 digits (more than 9), an 
    extra '-' and space is required*/
    if (board->height > 9)
    {
        rowSep = "+----++";
        preSpace = "  ";
    }
    else
    {
        rowSep = "+---++";
        preSpace = " ";
    }

    /*printing top boarder*/
    printf("\n%s" , rowSep);
    for (ii = 0; ii < board->width; ii++)
    {
        printf("---+" );
    }

    /*printing horizontal axis*/
    printf("\n|%s  ||" , preSpace);
    for (ii = 0; ii < board->width; ii++)
    {
        printf(COL " %c " RESET "|" , letter);
        letter += 1;
    }

    /*printing dividing row*/
    printf("\n%s" , rowSep);
    for (ii = 0; ii < board->width; ii++)
    {
        printf("---+" );
    }
    printf("\n");

    /*printing each following row*/
    for (ii = 0; ii < board->height; ii++)
    {
        num = ii + 1;
        if ((num == 10) | (num == 11) | (num == 12))
        {
            printf("|" COL " %d " RESET "||", num);
        }
        else
        {
            printf("|%s" COL "%d" RESET " ||", preSpace, num);
        }
        for (jj = 0; jj < board->width; jj++)
        {
            printf(" %s |" , board->boardArr[ii][jj]);
        }
        printf("\n");
    }
    /*printing lower border*/
    printf("%s" , rowSep);
    for (ii = 0; ii < board->width; ii++)
    {
        printf("---+" );
    }
    printf("\n\n");
}

/*This function performs a deep copy of a board. This is used to preserve a copy of the board 
when playing the game to allow playing again with an identical board. */
void copyBoard(Board* source, Board* dest)
{
    int ii, jj;
    Ship* ship;

    /*Copying width and height*/
    dest->width = source->width;
    dest->height = source->height;
    
    /*setting up arrays in dest board, width and length are required for this, sou coulnd't do 
    earlier*/
    initBoard(dest);
    for (ii = 0; ii < source->height; ii++)
    {
        for (jj = 0; jj < source->width; jj++)
        {
            /*Ensures we only copy if there is a ship to copy and we haven't already copied the 
            ship*/
            if ((source->shipArr[ii][jj] != NULL) & (dest->shipArr[ii][jj] == NULL))
            {
                ship = createShip();
                copyShip(source->shipArr[ii][jj], ship);
                addShip(ship, dest);
                /*should set next instances of this ship in the array so it will skip over them
                during the iteration, not needing to set them twice and set the numUnsunkShips
                variable*/
            }
            dest->boardArr[ii][jj] = source->boardArr[ii][jj];
        }
    }
}

/*This function frees the board struct and everythin contained in it. This is used to ensure no
memory leakage*/
void freeBoard(Board* board)
{
    int ii, jj, xx, yy;
    ShipIndices* indices;

    /*freeing the array of strings*/
    for (ii = 0; ii < board->height; ii++)
    {
        free(board->boardArr[ii]);
    }
    free(board->boardArr);
    
    /*freeing the array of ship pointers*/
    for (ii = 0; ii < board->height; ii++)
    {
        for (jj = 0; jj < board->width; jj++)
        {
            /*if there is a ship*/
            if (board->shipArr[ii][jj] != NULL)
            {   
                /*get the indices where it resides in the array*/
                indices = getShipIndices(board->shipArr[ii][jj]);
                freeShip(board->shipArr[ii][jj]);
                /*setting pointers for the ship to null at all the indices where ti resides in 
                order to avoid freeing the ship multiple times*/
                for (xx = indices->startV; xx <= indices->endV; xx++)
                {
                    for (yy = indices->startH; yy <= indices->endH; yy++)
                    {
                        board->shipArr[xx][yy] = NULL;
                    }
                }
                free(indices);
            }
        }
        free(board->shipArr[ii]); 
    }
    free(board->shipArr);

    free(board);
    board = NULL;
}
