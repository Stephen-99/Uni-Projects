#pragma once

#include "book.h"

//forward declarations
void quicksort(Book** A, int arrLen, bookLessThan);
void quicksortRecurse(Book** A, int leftIdx, int rightIdx, bookLessThan);
int doPartitioning(Book** A, int leftI, int rightI, int pivotI, bookLessThan);
void swap(Book** A, int i1, int i2);
void printArray(Book** A, int length);
