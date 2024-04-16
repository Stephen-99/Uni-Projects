/* CreateFile.c
overview:       This holds functions responsible for creating input board and missile files
date created:   14/04/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "CreateFile.h"
#include "UserInterface.h"
#include "FileIO.h"
#include "Missile.h"
#include "Board.h"

/*This function allows the user to create a board file from scratch. It asks the user for each 
input individually and performs error checking via the user interface checkInt methods as well 
as by creating the board as we go to ensure it all works. Once created this board file can be 
used for input with the program*/
void createBoardFile()
{
    char *choice, *filename, *line;
    FILE* file;
    Board* board = createBoard();
    Ship* ship;
    choice = (char*)malloc(2 *sizeof(char));    /*mallocing so can free every loop iteration*/
    
    /*obtaining the file name*/
    do
    {
        filename = getString("Please enter the name of the file to store the board in");
        file = openFile(filename, "w");
        free(filename);
    } while (file == NULL);
    
    /*obtaining the width and height of the board*/
    board->width = getInt("Please enter the width of the board", 1, 12);
    board->height = getInt("Please enter the height of the board", 1, 12);
    initBoard(board);

    /*writing the width and height to file*/
    fprintf(file, "%d,%d\n", board->width, board->height);

    do
    {
        /*creating a ship to store the values given by the user*/
        ship = createShip();
        /*already set by createShip so need to free otherwise allocated twice*/
        freeCoord(ship->head);  

        /*obtaining the ship fields*/
        /*need to loop when obtaining coordinate since user can enter 'help' as a valid 
        coordinate*/
        do  
        {
            ship->head = inputCoord("Please enter the starting coordinate for the ship", 
                board->height, board->width);
        } while (ship->head == NULL);

        convertCoord(ship->head);
        ship->direction = inputDirection("Please enter the direction the ship is facing");
        ship->length = getInt("Please enter the length of the ship", 1, 12); 
        
        /*already set by createShip so need to free otherwise allocated twice*/
        free(ship->name);
        ship->name = getString("Please enter the name of the ship");

        /*attempting to add the ship to the board. Any remaining invalidity should be caught 
        here. The way the addShip method is setup, allows us to add another ship even if the 
        previous ship was invalid*/
        if (addShip(ship, board))  
        {   /*valid ship*/
            line = getShipString(ship);
            fprintf(file, "%s\n", line);
            free(line);
        }
        else
        {   /*invalid ship*/
            freeShip(ship);
        }
        free(choice);
        /*add another ship? the n is in red since it is the default value*/
        choice = getString("Add another ship? (y/" RED "n" RESET ") ");
        toUpper(choice);
    /*we are only comparing the first character so anything from yes to YellOw will be accepted*/
    } while (choice[0] == 'Y');
   
    /*general cleanup*/ 
    freeBoard(board);
    free(choice);   
    fclose(file);

    print("Board file created");
}

/*This function allows the user to create a list of missiles. It attempts to create a missile 
each time to ensure that it is a valid missile. Once created this file can be used as input for 
the program*/
void createMissileList()
{
    char *type, *filename, *choice;
    FILE* file;
    Missile* m;
    choice = (char*)malloc(2 *sizeof(char));    /*mallocing so can free every loop iteration*/

    /*obtaining filename*/
    do
    {
        filename = getString("Please enter the name of the file to store the missiles in");
        file = openFile(filename, "w");
        free(filename);
    } while (file == NULL);

    /*obtaininng a missile from the user and printing to file*/
    do
    {
        type = getString("Please enter the type of missile:");

        /*attempting to create a missile in order to validate the missile*/
        m = createMissile(type);
        if (m == NULL)  
        {
            print("Invalid type of missile!\nMissile must be either Single, V-line, H-line or"
            " Splash");
        }
        else
        {   /*printing valid missile to file*/
            fprintf(file, "%s\n", type);
        }
        /*freeing variables so they can be re-used*/
        freeMissile(m);
        free(type);
        free(choice);

        /*the n is in red since it is the default value*/
        choice = getString("Add another Missile?(y/" RED "n" RESET ")");
        toUpper(choice);
    /*only comparing to the first character so anything from yes to YellOw is valid*/
    } while (choice[0] == 'Y');

    /*general cleanup*/
    free(choice);   
    fclose(file);

    print("Missile file created");
}


