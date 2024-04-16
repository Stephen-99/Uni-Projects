%{
    #include <stdio.h>
    #include <stdlib.h>
    #include <string.h>
    #include "LinkedList.h"

    #define TRUE 1
    #define FALSE ! TRUE

    //Function delcarations
    int main();
    int yywrap();
    void yyerror(const char *str);
    void sortList();
    void bubbleSort(int* arr);
    void printCurrentList();
    void printArray(int* arr, int length);
    void printInt(void* data);
    void freeInt(void* data);

    //global variables
    int numItems = 0;
    LinkedList *list = NULL;

%}


%token WORD NUMBER LH_SQ_BRACKET RH_SQ_BRACKET COMMA
%%
lists : lists list
    | list
    ;

list :
    LH_SQ_BRACKET number_list RH_SQ_BRACKET
    {
        printf("Found a valid list! \n");
        printCurrentList();
        sortList();
        numItems = 0;
    }
    ;

number_list : /* empty*/
    | number_list COMMA NUMBER
    {
        int* num = (int*)malloc(sizeof(int));
        *num = $3;   
        numItems++;
        insertLast(list, num);
    }
    | NUMBER
    {
        int* num = (int*)malloc(sizeof(int));
        *num = $1;   
        numItems++;
        insertLast(list, num);
    }
    ;

%%
int main()
{    
    list = createLinkedList();
    yyparse();
    free(list);
}

void yyerror(const char *str)
{
    fprintf(stderr, "error: %s\n", str);
}

int yywrap()
{
    return 1;
}


void sortList()
{
    int* arr = (int*)calloc(numItems, sizeof(int));
    void* data;
    int ii = 0; 

    if (isEmpty(list))
    {
        //Otherwise when printing array, will print a 1-element array with 
        //default value of 0
        printf("sorted list: []\n\n");
    } else {
        //copy list to an array
        while (!isEmpty(list))
        {
            data = removeFirst(list);
            arr[ii] = *(int*)data;
            free((int*)data);
            ii++;
        } 

        bubbleSort(arr);

        printf("sorted list: ");
        printArray(arr, numItems);
        printf("\n");
    }

    free(arr);

    freeList(list, &freeInt);
    list = createLinkedList();
}

void bubbleSort(int* arr)
{
    int count = 0;
    int ordered = FALSE;

    while (!ordered)
    {
        ordered = TRUE;
        for(int ii = 0; ii < numItems-1 -count; ii++)
        {
            if (arr[ii] > arr[ii+1])
            {
                ordered = FALSE;
                arr[ii] = arr[ii] + arr[ii+1];
                arr[ii+1] = arr[ii] - arr[ii+1];
                arr[ii] = arr[ii] - arr[ii+1];
            }
        }
        count++;
    }

    return;
}

void printCurrentList()
{
    printf("[");
    printList(list, &printInt);
    printf("]\n");
}

void printArray(int* arr, int length)
{
    printf("[");
    for (int ii = 0; ii < length - 1; ii++)
    {
        printf("%d, ", arr[ii]);
    }
    printf("%d]\n", arr[length-1]);
}

void printInt(void* data)
{
    int* num = (int*)data;
    printf("%d ", *num);
}

void freeInt(void* data)
{
    free((int*)data);
}
