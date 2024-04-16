#ifndef FILEIO
#define FILEIO
#include <stdio.h>
#include "LinkedList.h"

#define LINE_MAX 1024

/*Forward declarations*/
FILE* OpenFile(char* filename, char* readOrWrite);
int ReadFile(char* filename, LinkedList* list);
#endif
