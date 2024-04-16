/* UserInterface.c
overview:       This holds functions responsible for user input and output to user as well as 
                string manipulation, such as converting to uppercase.
date created:   28/04/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "UserInterface.h"
#include "Coord.h"

/*This is function obtains an integer from the user within a certain range. This is used by the
menu when obtaining the user's choice, and when creating a board file in order to obtain valid 
fields*/
int getInt(char* prompt, int min, int max)
{
    int entry = min - 1;
    char *outStr = (char*)malloc(401 * sizeof(char));
    strcpy(outStr, prompt);

    /*while not in range, ask again*/
    while (!checkInt(entry, min, max))
    {
        print(outStr);
        /*while not read an integer, ask again*/
        while (scanf("%d", &entry) != 1)
        {
            print("\nEntry must be an integer");
            print(prompt);
            /*Need to clear the buffer otherwise will continue to read the same value*/
            clearBuffer();
        }
        /*otherwise not cleared when an integer is entered, causing next iteration to fail*/
        clearBuffer();
        sprintf(outStr, "\nMust be an integer between %d and %d.\n%s ", min, max, prompt);
    }
    free(outStr);
    return entry;
}

/*submitted for prac 1
This function clears the scanf buffer. It is used when reading from the scanf buffer in getInt
and inputDirection*/
void clearBuffer()    
{
    /*will clear the scanf buffer*/
    while(fgetc(stdin) != '\n');
}

/*This function checks if an integer is within a certain range. This is used for validating the
board file and when obtaining a new integer(getInt)*/
int checkInt(int val, int min, int max)
{
    /*Checks integer is within range, returning 1 for pass and 0 for failure*/
    int returnVal = 1;
    if ((val < min) | (val > max))
    {
        returnVal = 0;
    }

    return returnVal;
}

/*This function obtains a coordinate from the user. It reads user input as a whole line allowing
the user to enter 'help when playing the game, in which case the coordinate is returned as NULL*/
Coord* inputCoord(char* prompt, int height, int width)
{
    char line[21];
    int needHelp = 0;
    Coord* coord = createCoord();
    char* outStr = prompt;  

    /*Lopp while user doesn't need help and the horizontal coordinate is out of range. Note: 
    hozI initially -1, also hozI is 0-indexed*/
    while ((!needHelp) & (! checkInt(coord->hozI, 0, width - 1)))
    {
        print(outStr);
        /*read the line and see if the user needs help*/
        needHelp = checkIfNeedHelp(line);

        /*Loop while no help is required and the coordinate is in the inncorrect format*/
        while ((!needHelp) & (sscanf(line, "%c%d", &(coord->hoz), &(coord->vert)) != 2)) 
        {
            print("Invalid Coordinate");
            print(prompt);
            needHelp = checkIfNeedHelp(line);
        }
        convertCoord(coord); 
        
        /*loop while the user doens't need help and the vertival coordinate is out of range. 
        Note: vert isn't 0-indexed*/
        while ((!needHelp) & (! checkInt(coord->vert, 1, height))) 
        {
            print("Invalid Coordinate: vertical axis out of bounds!");
            print(prompt);
            needHelp = checkIfNeedHelp(line);

            /*Loop while no help is required and the coordinate is in the inncorrect format*/
            while ((!needHelp) & ((sscanf(line, "%c%d", &(coord->hoz), &(coord->vert)) 
            != 2))) 
            {
                print("Invalid Coordinate");
                print(prompt);
                needHelp = checkIfNeedHelp(line);
            }
            convertCoord(coord);
        }
        /*Modify the string to print so that if the loop runs again, it will reflect the reason
        why the coordinate must be re-entered*/
        outStr = joinStr("Invalid Coordinate: Horizontal axis out of bounds!\n", prompt);
    }

    /*if the loop ended because the user needs help, set the coord to NULL to let the calling 
    code know*/
    if (needHelp)
    {
        freeCoord(coord);
        coord = NULL;
    }
    free(outStr);
    return coord;    
}

/*This function is used by the above function to read a new line from the user and determine if 
they need help. 0 means they don't need help while 1 means they do*/
int checkIfNeedHelp(char* line)
{
    int needHelp = 0; 
    fgets(line, 20, stdin);
    toUpper(line);

    /*convert newline left by fgets, into null terminator, i.e. end of string*/
    line[strlen(line) - 1] = '\0';
    if (strcmp(line, "HELP") == 0)
    {
        needHelp = 1;
    }
    return needHelp;
}

/*This function obtains a valid direction from the user. This is used when obtaining a ship's 
direction when creating a board file*/
char inputDirection(char* prompt)
{
    char direction;

    print(prompt);
    while (scanf("%[NESWnesw]", &direction) != 1)
    {
        print("\nEntry must be N, E, S or W");
        print(prompt);
        clearBuffer();
    }
    /*otherwise not cleared for valid input, causing next call to fail*/
    clearBuffer();

    return direction;
}

/*This is a function which reads a string from stdin. It mallocs the string, so it will need to 
be freed after use. It is used when obtaining missile types and  ship names in order to reate 
missile and board files respectively*/
char* getString(char* prompt)
{
    char* str = (char*)malloc(201 * sizeof(char));

    do
    {
        print(prompt);
        /*Reading a line from terminal, i.e. entered text*/
        fgets(str, 200, stdin);
        /*setting last char from newline to null terminator*/
        str[strlen(str) - 1] = '\0';
    } while (strlen(str) < 1);

    return str;
}   


/*SUBMITTED EARLIER FOR PRAC 4, modified slightly*/

/*This function converts a string to uppercase and is used fairly extensively when comnparing 
strings in order to allow case insensitive responses to be accepted*/
void toUpper(char* string)
{
    int ii;
    for (ii = 0; ii < strlen(string); ii++)
    {
        string[ii] = toUpperCase(string[ii]);
    }
}

/*SUBMITTED EARLIER FOR PRAC 4, modified slightly*/
/*This function is used mostly in conjunction with the above function and converts a single 
character into to upperCase. It is also used to convert a ship's direction to uppercase*/
int toUpperCase(int ch)
{
    if (ch > 96)    /*will be lowercase*/
    {
        /* -32 is the gap in ascii between lowercase and uppercase*/
        ch -= 32;
    }
    return ch;
}

/*adapted from :
https://stackoverflow.com/questions/8465006/how-do-i-concatenate-two-strings-in-c/8465083 
Accepted answer by David Heffernan*/

/*This function joins 2 strings together. This allows the use of the print function defined below
as opposed to printf. However, its main purpose is when using a prompt in a loop that needs to 
change after the first iteration, such as is done in the inputCoord function*/
char* joinStr(char* str1, char* str2)
{ 
    int len = ((strlen(str1) + strlen(str2)) / sizeof(char)) + 1;
    char *joint = (char*)malloc(len * sizeof(char));
    strcpy(joint, str1);
    strcat(joint, str2);

    return joint;
}

/*This is a simple print function for printing strings, so that I don't forget the newline at the
end. This is used fairly extensively throughout the program*/
void print(char* msg)
{
    printf("%s\n", msg);
}
