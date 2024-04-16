#ifndef FILEIO
#define FILEIO
#include "Board.h"
#include "LinkedList.h"

/*Forward declarations*/
FILE* openFile(char* filename, char* readOrWrite);
int readBoard(char* filename, Board*);
int processShip(char* line, Board*);
int readMissiles(char* filename, LinkedList*);
#endif
