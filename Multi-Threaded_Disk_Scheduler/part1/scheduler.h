#ifndef SCHEDULER
#define SCHEDULER
#define PROMPT "\nDisk Scheduler Simulation: "
#include "LinkedList.h"

int FCFS(LinkedList* list, int len);
int SSTF(LinkedList* list, int len);
int SCAN(LinkedList* list, int len);
int CSCAN(LinkedList* list, int len);
int LOOK(LinkedList* list, int len);
int CLOOK(LinkedList* list, int len);
int* ListToArray(LinkedList* list);
void FreeInt(void* data);
void GetFilename(char* filename);
void clearBuffer();
#endif