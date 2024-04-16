#ifndef SIMULATOR
#define SIMULATOR
#define PROMPT "\nDisk Scheduler Simulation: "
#include "LinkedList.h"
#include "scheduler.h"

//function pointer for seek algs
typedef int (*SeekAlg)(int* data, int len);

typedef struct
{
    pthread_t threadId;
    int threadNum;
    SeekAlg SeekAlg;
} ThreadInfo;

void* RunThread(void* args);
char* GetResultsString(int* results);
int* ListToArray(LinkedList* list);
void FreeInt(void* data);
void GetFilename(char* filename);
void clearBuffer();
#endif