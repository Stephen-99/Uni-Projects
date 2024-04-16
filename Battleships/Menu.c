/* Menu.c
overview:       Gives the user the menu options and calls the relavant functions to complete the 
                option they choose
date created:   14/05/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "Menu.h"
#include "UserInterface.h"
#include "LinkedList.h"
#include "Missile.h"
#include "Board.h"
#include "CreateFile.h"
#include "Game.h"

/*This function allows the user to select an option for how they want to use the program. It will
call runChoice to execute that option. It will continue to do this until the user chooses to 
exit. This function is called in main once the input files have succesfully been read*/
void menu(Board* board, LinkedList* missiles)
{
    char* prompt = "Please enter the integer that corresponds to your choice\n1. Play the game"
    "\n2. List missiles\n3. create Board file\n4. create Missile File\n0. Exit";
    int choice = getInt(prompt, 0, 4);
    while (choice != 0)
    {
        runChoice(choice, board, missiles);
        choice = getInt(prompt, 0, 4);
    }
}

/*This function will call the relavant functions to run whatever choice the user has selected*/
void runChoice(int choice, Board* board, LinkedList* missiles)
{
    if (choice == 1)
    {   /*play game*/

        char* prompt = "Please enter the integer that corresponds to your choice\n1. Play again"
        "\n0. Back to Main menu";

        /*create a copy of the bopard so original board is preserved and game can be played 
        multiple times*/
        Board* board2 = createBoard();
        copyBoard(board, board2);

        playGame(board2, missiles);

        /*see if they want to play again*/
        choice = getInt(prompt, 0, 1);

        /*continue to play the game until they choose to return to the main menu*/
        while (choice == 1)
        {
            /*free old board and make another copy*/
            freeBoard(board2);
            board2 = createBoard();
            copyBoard(board, board2);

            playGame(board2, missiles);
            choice = getInt(prompt, 0, 1);
        }
        freeBoard(board2);
    }
    else if (choice == 2)
    {   /*print missiles*/

        printf("\n");
        printList(missiles, &printMissile);
        printf("\n");
    }
    else if (choice == 3)
    {
        createBoardFile();
    }
    else if (choice == 4)
    {
        createMissileList();
    }
}

