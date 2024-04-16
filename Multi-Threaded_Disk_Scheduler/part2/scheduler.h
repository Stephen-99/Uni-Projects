#ifndef SCHEDULER
#define SCHEDULER
#define NUMSEEKALGS 6
#include "LinkedList.h"

int FCFS(int* data, int len);
int SSTF(int* data, int len);
int SCAN(int* data, int len);
int CSCAN(int* data, int len);
int LOOK(int* data, int len);
int CLOOK(int* data, int len);
#endif