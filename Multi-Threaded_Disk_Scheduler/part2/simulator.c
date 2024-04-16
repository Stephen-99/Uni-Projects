#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>
#include "simulator.h"
#include "scheduler.h"
#include "FileIO.h"

LinkedList* buffer1;
pthread_mutex_t b1Mutex;
pthread_cond_t b1Cond;
int* b1Condition;

int* buffer2;
pthread_mutex_t b2Mutex;
pthread_cond_t b2ReadCond;
pthread_cond_t b2WriteCond;
int b2Condition;

int keepGoing;

int main()
{
    //Initialise globals
    buffer1 = NULL;
    pthread_mutex_init(&b1Mutex, NULL);
    pthread_cond_init(&b1Cond, NULL);
    //b1Condition is an array, so each thread has its own so that no thread runs twice before a write update
    b1Condition = (int*)calloc(NUMSEEKALGS, sizeof(int));
    buffer2 = (int*)malloc(sizeof(int));
    pthread_mutex_init(&b2Mutex, NULL);
    pthread_cond_init(&b2ReadCond, NULL);
    pthread_cond_init(&b2WriteCond, NULL);
    b2Condition = 0;
    keepGoing = 1;

    int* results = (int*)calloc(NUMSEEKALGS, sizeof(int));
    SeekAlg seekAlgs[NUMSEEKALGS] = {&FCFS, &SSTF, &SCAN, &CSCAN, &LOOK, &CLOOK};
    ThreadInfo* threadInfos = (ThreadInfo*)calloc(NUMSEEKALGS, sizeof(ThreadInfo));

    //Create threads
    for (int ii = 0; ii < NUMSEEKALGS; ii++)
    {
        threadInfos[ii].threadNum = ii+1;
        threadInfos[ii].SeekAlg = seekAlgs[ii];
        pthread_create(&threadInfos[ii].threadId, NULL, &RunThread, &threadInfos[ii]);
    }

    char *filename = (char*)malloc(10*sizeof(char));
    printf(PROMPT);
    GetFilename(filename);

    while (strcmp(filename, "QUIT") != 0)
    {
        //obtain buffer1 info from file
        pthread_mutex_lock(&b1Mutex);
        buffer1 = CreateLinkedList();

        if(!ReadFile(filename, buffer1))
        {
            printf("The input file was invalid.\n");
            FreeList(buffer1, &FreeInt);
            pthread_mutex_unlock(&b1Mutex);
            break;
        }

        //Let all threads know they can read from the buffer now
        for (int ii = 0; ii < NUMSEEKALGS; ii++)
        {
            b1Condition[ii] = 1;
        }
        pthread_cond_broadcast(&b1Cond);
        pthread_mutex_unlock(&b1Mutex);

        //Wait for all algs to give their result
        for (int ii = 0; ii < NUMSEEKALGS; ii++)
        {
            pthread_mutex_lock(&b2Mutex);
            while (!b2Condition)
            {   
                pthread_cond_wait(&b2ReadCond, &b2Mutex);
            }
            
            //b2Condition set to threadNum so results can be stored and printed in order
            results[b2Condition-1] = *buffer2;

            //signal buffer2 can be overwritten again
            b2Condition = 0;
            pthread_cond_signal(&b2WriteCond);
            pthread_mutex_unlock(&b2Mutex);
        }
        //reset buffer1 before next iteration
        FreeList(buffer1, &FreeInt);
        buffer1 = NULL;

        //Print results
        char* resultsStr = GetResultsString(results);
        printf("%s", resultsStr);
        free(resultsStr);
    
        printf(PROMPT);
        GetFilename(filename);
    }
    free(filename);
    free(buffer2);
    free(results);

    //Let threads know to end 
    pthread_mutex_lock(&b1Mutex);
    keepGoing = 0;
    for (int ii = 0; ii < NUMSEEKALGS; ii++)
    {
        b1Condition[ii] = 1;
    }
    pthread_cond_broadcast(&b1Cond);
    pthread_mutex_unlock(&b1Mutex);

    //clean up threads
    for (int ii = 0; ii < NUMSEEKALGS; ii++)
    {
        pthread_join(threadInfos[ii].threadId, NULL);
        printf("%lu has terminated\n", threadInfos[ii].threadId);
        fflush(stdout);
    }
    free(threadInfos);
    free(b1Condition);
    pthread_exit(0);
}

void* RunThread(void* args)
{
    ThreadInfo *threadInfo = args;

    //wait till there is data in the buffer   
    pthread_mutex_lock(&b1Mutex);
    while (!b1Condition[threadInfo->threadNum-1])
    {
        pthread_cond_wait(&b1Cond, &b1Mutex);
    }
    //Reset our condition varible, so we cannot read again till a new write occurs
    b1Condition[threadInfo->threadNum-1] = 0;

    while(keepGoing)
    {
        //read data
        int* data = ListToArray(buffer1);
        pthread_mutex_unlock(&b1Mutex);

        //run algorithm
        int res = threadInfo->SeekAlg(data, buffer1->length);

        //wait till we can write result
        pthread_mutex_lock(&b2Mutex);
        while (b2Condition)
        {
            pthread_cond_wait(&b2WriteCond, &b2Mutex);
        }
        b2Condition = threadInfo->threadNum;
        *buffer2 = res;

        //tell reader thread(parent) to read our result
        pthread_cond_signal(&b2ReadCond);
        pthread_mutex_unlock(&b2Mutex);

        //wait for a new write or keepGoing updated to false
        pthread_mutex_lock(&b1Mutex);
        while (!b1Condition[threadInfo->threadNum-1])
        {
            pthread_cond_wait(&b1Cond, &b1Mutex);
        }
        b1Condition[threadInfo->threadNum-1] = 0;  
    }
    pthread_mutex_unlock(&b1Mutex);
    return NULL;
}

int* ListToArray(LinkedList* list)
{
    int len = list->length;
    int *arr = (int*)calloc(len, sizeof(int));
    ListNode *curNode = list->head;

    for (int ii = 0; ii < len; ii++)
    {
        arr[ii] = *(int*)curNode->data;
        curNode = curNode->next;
    }
    return arr;
}

char* GetResultsString(int* results)
{   
    /*assume algorithm name + result + tab and new line doesn't exceed this length
        If you test with a ridiculously large cylinder count, (like 12 digits 
        large) then please update this variable accordingly*/
    int MAXLENGTH = 20;
    char* str = (char*)malloc(NUMSEEKALGS * MAXLENGTH * sizeof(char));
    *str = '\0';
    char* tempStr = (char*)malloc(MAXLENGTH * sizeof(char));

    char algs[NUMSEEKALGS][7] = {"FCFS", "SSTF", "SCAN", "C-SCAN", "LOOK", "C-LOOK"};
    for (int ii = 0; ii < NUMSEEKALGS; ii++)
    {
        sprintf(tempStr, "%s:\t%d\n", algs[ii], results[ii]);
        strcat(str, tempStr);
    }
    free(tempStr);
    return str;
}

//For freeing list of integers e.g. buffer1
void FreeInt(void* data)
{
    int* num = (int*)data;
    free(num);
}


void GetFilename(char* filename)
{
    while (scanf("%s", filename) != 1)
    {
        printf("Invalid input. Please try again\n");
        printf(PROMPT);
        clearBuffer();
    }
}

/*will clear the scanf buffer*/
void clearBuffer()
{   
    while(fgetc(stdin) != '\n');
}
