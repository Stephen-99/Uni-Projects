/* linkedList.c
overview:       This holds all the linked list functionality. This code was initially submitted 
                for practical 6. It hasn't been changed except that the free list function now
                checks that the list is empty and comments have been added.
date created:   20/04/2020
last edited:    21/05/2020
name:           Stephen den Boer
ID:             19761257
*/

#include <stdio.h>
#include <stdlib.h>
#include "LinkedList.h"

/*This function creates a pointer to a linkedList struct, setting appropriate values for an empty
list*/
LinkedList* createLinkedList()
{
    LinkedList* list = (LinkedList*)malloc(sizeof(LinkedList));
    list->head = NULL;
    list->tail = NULL;
    list->length = 0;
    return list;
}

/*This function checks if the list is empty, which is useful since operations like insertion and
deletion are often a little different if the list is empty*/
int isEmpty(LinkedList* list)
{
    return (list->head == NULL);
}

/*this function inserts a node at the beginning of the list*/
void insertFirst(LinkedList* list, void* entry)
{
    /*create a list node. It will replace the current head node*/
    ListNode* newNode = (ListNode*)malloc(sizeof(ListNode));
    newNode->data = entry;
    newNode->next = list->head; 
    newNode->prev = NULL;

    if (isEmpty(list))
    {
        list->tail = newNode;
    }
    else
    {
        list->head->prev = newNode;
    }
    list->head = newNode;
    list->length += 1;
}

/*This function removes the first element in the list, returning the value stored there*/
void* removeFirst(LinkedList* list)
{
    void* value = NULL;
    if (!isEmpty(list))
    {
        value = list->head->data;

        /*if there is only one element in the list, st it as an empty list*/
        if (list->head->next == NULL)
        {
            free(list->head);
            list->head = NULL;
            list->tail = NULL;
        }
        else
        {
            /*next node replaces first node*/
            list->head = list->head->next;
            free(list->head->prev);
            list->head->prev = NULL;
        }
        list->length -= 1;
    }
    return value;
}

/*This function inserts a node at he very end of the list*/
void insertLast(LinkedList* list, void* entry)
{
     
    ListNode* newNode = (ListNode*)malloc(sizeof(ListNode));
    newNode->data = entry;
    newNode->next = NULL;

    /*if the list is empty, the new node just becomes the head*/
    if (isEmpty(list))
    {
        list->head = newNode;
    }
    /*otherwise replace the tail with the new node*/
    else
    {
        newNode->prev = list->tail;
        list->tail->next = newNode;
    }
    list->tail = newNode;
    list->length += 1;
}

/*This function removes a node from the end of the list returning its value*/
void* removeLast(LinkedList* list)
{
    void* value = NULL;
    if (!isEmpty(list))
    {
        value = list->tail->data;
        /*if there is only 1 element in the list, set it to be an empty list*/
        if (list->head->next == NULL)
        {
            free(list->tail);
            list->tail = NULL;
            list-> head = NULL;
        }
        /*otherwise replace the tail with the node before it*/
        else
        {
            list->tail = list->tail->prev;
            free(list->tail->next);
            list->tail->next = NULL;
        }
        list->length -= 1;
    }
    return value;
}

/*this function returns the data stored in the first node in the list*/
void* peekFirst(LinkedList* list)
{
    void* value = NULL;
    if (!isEmpty(list))
    {
        value = list->head->data;
    }
    return value;
}

/*thid function returns the  data stored in the last node in the list*/
void* peekLast(LinkedList* list)
{
    void* value = NULL;
    if (!isEmpty(list))
    {
        value = list->tail->data;
    }
    return value;
}

/*This function prints the data for each node in the list. It requires a function pointer to a 
function for printing the data in each node*/
void printList(LinkedList* list, listFunc printFunc)
{
    int ii;
    ListNode* curNode = list->head;
    for (ii = 0; ii < list->length; ii++)
    {
        printFunc(curNode->data);
        curNode = curNode->next;
    }
}

/*This function frees everything in the list. It should be called when the list no longer needs 
to be used. It also requires a function pointer in order to free the data stored in each node.*/
void freeList(LinkedList* list, listFunc freeFunc)
{
    if (!isEmpty(list))
    {
        int ii;
        ListNode* prevNode = list->head;
        ListNode* curNode = list->head->next;

        /*iterate through the list, freeing the previous node*/
        for (ii = 0; ii < list->length; ii++)
        {
            freeFunc(prevNode->data);
            free(prevNode);
            prevNode = curNode;
            if (curNode != NULL)
            {
                curNode = curNode->next;
            }
        }
    }
    free(list);
}

/*This function returns the value at any index which allows the user to access any element in the
list, simialr to an array.*/
void* peekAny(LinkedList* list, int index)
{
    void* value = NULL;
    int ii;
    ListNode* curNode = list->head;

    /*loop until the index*/
    for (ii = 0; ii < index; ii++)
    {
        if (curNode->next != NULL)
        {
            curNode = curNode->next;
        }
    }
    /*if the element at that index exists, return its value*/
    if (curNode != NULL)
    {   
        value = curNode->data;
    }

    return value;
}

