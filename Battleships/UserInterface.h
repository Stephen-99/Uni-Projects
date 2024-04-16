#ifndef USERINTERFACE
#define USERINTERFACE
#include "Coord.h"

/*Forward declarations*/
int getInt(char* prompt, int min, int max);
int checkInt(int val, int min, int max);
void print(char*);
void toUpper(char*);
int toUpperCase(int ch);
Coord* inputCoord(char* prompt, int height, int width);
int checkIfNeedHelp(char* line);
char inputDirection(char* prompt);
char* getString(char* prompt);
char* joinStr(char* str1, char* str2);
void clearBuffer();
#endif
