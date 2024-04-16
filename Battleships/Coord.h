#ifndef COORD
#define COORD

/*This is the coordinates struct which stores all the necessary information for a coordintate.
It has the vertical coordinate as an integer and teh horizontal coordinate as both a character 
and an integer, for both printing the coordinate and using it in calculations. This struct is 
used as the starting coordinate for a ship and also when obtaining coordinates from the user*/
typedef struct
{
    char hoz;
    int hozI;
    int vert;
} Coord;

/*Forward declarations*/
Coord* createCoord();
int convertCoord(Coord* coord);
void copyCoord(Coord* source, Coord* dest);
void freeCoord(Coord*);
#endif
