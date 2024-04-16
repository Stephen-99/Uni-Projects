#ifndef MISSILE
#define MISSILE
#include "Board.h"

/*This function pointer points to a function that executes the missile. This is useful in that
we can execute the missile when playing the game, without checking what type of missile it is*/
typedef int (*missileFunc)(Board*, Coord*);

/*This missile struct stores the necessary inforfmation for displaying and executing the missile.
It is used as a datatype to be stored in a linked list, which is then used to play the game*/
typedef struct
{
    char* type;     /*only used for printing the missile type*/
    char* msg;
    missileFunc execute;
} Missile;

/*Forward declarations*/
Missile* createMissile(char* type);
int whichMissile(Missile*, char* type);
int single(Board*, Coord*);
int v_line(Board*, Coord*);
int h_line(Board*, Coord*);
int splash(Board*, Coord*);
void printMissile(void* data);
void freeMissile(void* data);
#endif
