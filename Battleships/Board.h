#ifndef BOARD
#define BOARD
#include "Ship.h"

/*colour macros allows text to be printed in colour*/
#define RED "\033[0;31m"
#define GREEN "\033[0;32m"
#define BLUE "\033[0;34m"
#define MAGENTA "\033[1;35m"
#define CYAN "\033[0;36m"
#define YELLOW "\033[0;33m"
#define BOLD "\033[1m"
#define RESET "\033[0m"

/*Macros for printing the board. If MONO is enabled, they will be uncoloured*/
#ifdef MONO
#define HIT "0"
#define MISS "X"
#define UNSHOT "#"
#define COL ""
#endif

#ifndef MONO
#define HIT GREEN "0" RESET
#define MISS RED "X" RESET
#define UNSHOT BLUE "#" RESET
#define COL YELLOW
#endif

/*This is a struct which holds infromation about the board. It keeps track of the number of 
unsunk ships so that its possible to tell when the game is won. It also contains a 2 dimensional
array of strings which is responsible for printing out the board. The strings will contain any
colour modification necassary. It also contains a 2D array of ship pointers. This arrow contains
a pointer to a ship if there is a ship at that spot on the board, otherwise it contains a null
pointer. This makes it easy to check if a coordinate contains a ship, and to update the ship as 
necassary*/
typedef struct
{
    int width;
    int height;
    int numUnsunkShips;
    char*** boardArr; /*2D array of strings*/
    Ship*** shipArr; /*2D array of ship pointers*/
} Board;

/*Forward Declarations*/
Board* createBoard();
void initBoard(Board*);
int addShip(Ship*, Board*);
void printBoard(Board*);
int hitShip(Board*, int row, int col);
void hitShips(Board*);
void copyBoard(Board* source, Board* dest);
void freeBoard(Board*);
#endif
