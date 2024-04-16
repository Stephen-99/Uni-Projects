#ifndef LINKEDLIST
#define LINKEDLIST

/*This struct holds the information for a list node. These nodes are joined together to form a 
linkedList*/
typedef struct ListNode
{
    void* data;
    struct ListNode* next;
    struct ListNode* prev;
} ListNode;

/*this struct holds the basic information about the linkedList: where it starts and ends, and 
its length. In this program, the linked list is used to store the missiles*/
typedef struct
{
    ListNode* head;
    ListNode* tail;
    int length;
} LinkedList;

/*this function pointer takes a void* which is the same as the data being stored in the list 
node. This function pointer allows the user of the list, to pass a function for printing and 
freeing their data, customised to the data they are storing in the list*/
typedef void (*listFunc)(void* data);

/*Forward Decs*/
LinkedList* CreateLinkedList();
int IsEmpty(LinkedList* list);
void InsertFirst(LinkedList* list, void* entry);
void* RemoveFirst(LinkedList* list);
void InsertLast(LinkedList* list, void* entry);
void* RemoveLast(LinkedList* list);
void* PeekFirst(LinkedList* list);
void* PeekLast(LinkedList* list);
void* PeekAny(LinkedList* list, int index);
void PrintList(LinkedList* list, listFunc printFunc);
void FreeList(LinkedList* list, listFunc freeFunc);
#endif
