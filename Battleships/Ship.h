#ifndef SHIP
#define SHIP
#include "Coord.h"

/*This struct holds all the information regarding a ship. It allows the ship to be allocated to 
all the relavent coordinates in the ship array contained in the board struct. It also makes it 
easy to determine if the ship has been sunk by comparing the number of hits to the length.*/
typedef struct
{
    Coord* head;
    char direction;
    int length;
    int numHits;
    char* name;
} Ship;

/*This struct holds the indices where the ship should be added to the ship array in the board 
struct. This also becomes useful when freeing the ship, allowing all refrences to it in the 
ship array to be set to NULL*/
typedef struct
{
    int startH;
    int endH;
    int startV;
    int endV;
} ShipIndices;

/*Forward declarations*/
Ship* createShip();
char* getShipString(Ship*);
void copyShip(Ship* source, Ship* dest);
ShipIndices* getShipIndices(Ship*);
void freeShip(Ship*);
#endif
