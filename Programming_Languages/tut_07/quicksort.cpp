//Made specific to the Book class so we can choose to sort by either name, ISBN or id
#include "quicksort.h"

//TODO could make into a class so most of the methods become private...

//Takes a function pointer
void quicksort(Book** arr, int arrLen, bookLessThan bookLT)
{
    quicksortRecurse(arr, 0, arrLen-1, bookLT);
}

//using pivot index as the left most index
void quicksortRecurse(Book** A, int leftIdx, int rightIdx, bookLessThan bookLT)
{
    if (rightIdx > leftIdx)
    {
        int newPivot, pivotIdx;

        pivotIdx = leftIdx;

        newPivot = doPartitioning(A, leftIdx, rightIdx, pivotIdx, bookLT);

        quicksortRecurse(A, leftIdx, newPivot-1, bookLT);
        quicksortRecurse(A, newPivot+1, rightIdx, bookLT);
    }
}

int doPartitioning(Book** A, int leftI, int rightI, int pivotI, bookLessThan bookLT)
{
    int curI;
    Book* pivotVal = A[pivotI];

    swap(A, pivotI, rightI);

    curI = leftI;
    for (int ii = leftI; ii < rightI; ii++)
    {
        if (bookLT(*A[ii], *pivotVal))
        {
            swap(A, ii, curI);
            curI++;
        }
    }

    A[rightI] = A[curI];
    A[curI] = pivotVal;

    //border of sorted area
    return curI;
}

void swap(Book** arr, int i1, int i2)
{
    Book* temp = arr[i1];
    arr[i1] = arr[i2];
    arr[i2] = temp;
}

void printArray(Book** arr, int length)
{
    for (int ii = 0; ii < length; ii++)
    {
        std::cout << *arr[ii] << '\n';
    }
    std::cout << '\n';
}

