####
# DSALinkedList.py
#
# Date created:     26/03/2020
# Date last edited: 01/05/2020
#
# Author:   Stephen den Boer  
# ID:       19761257
#
# Overview:
#   This is a class that holds the LinkedList definition, for a doubly ended, 
#   doubly linked list 
#    
####

#Initial version of this code was previously submitted for Prac 03
#It has been updated and submitted for later practicals as well.
#There has been little to no change since it was last submitted

from DSAListNode import *
from Errors import *

class DSALinkedList():

    def __init__(self):
        self.head = None
        self.tail = None
        self.length = 0
    #END __init__

    def __iter__(self):
        curNode = self.head
        while curNode != None:
            yield curNode.value
            curNode = curNode._next
        return self


    def insertFirst(self, newVal):
        newNode = DSAListNode(newVal)
        if self.isEmpty():
            self.head = newNode
            self.tail = newNode
        else:
            newNode._next = self.head
            self.head._prev = newNode
            self.head = newNode
        #END if-else
        self.length += 1
    #end insertFirst

    def insertLast(self, newVal):
        newNode = DSAListNode(newVal)
        if self.isEmpty():
            self.head = newNode
            self.tail = newNode
        else:
            #old last node points to new last node
            self.tail._next = newNode
            newNode._prev = self.tail
            #tail points to new last node
            self.tail = newNode
        #END if-else
        self.length += 1
    #END insertLast

    def insertAny(self, val, index):
        if index > self.length:
            raise ListError("Tried to add a new value at a non-existent index")
        if index < 0:
            raise ListError("Tried to access a negative index!")
        else:
            if index == 0:
                self.insertFirst(val)
            elif index == self.length:
                self.insertLast(val)
            else:
                curNode = self.head
                for ii in range(index):
                    curNode = curNode._next
                newNode = DSAListNode(val)
                curNode._prev._next = newNode
                newNode._prev = curNode._prev
                curNode._prev = newNode
                newNode._next = curNode
                #don't need to update head or tail pointers, since will only 
                #be the case if index = 0 ot index = length, which is taken 
                #care of by inertFirst or insertLast
                self.length += 1
            #END if-else
        #END if-else
    #END insertAny

    def isEmpty(self):
        return (self.head == None)
    #END isEmpty

    def peekFirst(self):
        if self.isEmpty():
            raise ListUnderflow
        else:
            return self.head.value
        #END if-else
    #END peekFirst

    def peekLast(self):
        if self.isEmpty():
            raise ListUnderflow
        else:
            return self.tail.value
        #END if-else
    #END peekLast

    def peekAny(self, index):
        if self.isEmpty():
            raise ListUnderflow
        if index >= self.length:
            raise ListError("Tried to access a value outside of list length")
        if index < 0:
            raise ListError("Tried to access a negative index!")

        curNode = self.head
        for ii in range(index):
            curNode = curNode._next
        return curNode.value
    #END peekAny

    def removeFirst(self):
        value = self.peekFirst()
        if self.head._next == None:
            self.head = None
            self.tail = None
        else:
            self.head = self.head._next
            self.head._prev = None
        #END if-else
        self.length -= 1
        return value
    #END removeFirst

    def removeLast(self):
        value = self.peekLast()
        if self.head._next == None:
            self.head = None
            self.tail = None
        else:
            self.tail._prev._next = None
            self.tail = self.tail._prev
        #END if-else

        self.length -= 1
        return value
    #END removeLast

    def removeAny(self, index):
        if index == 0:
            value = self.removeFirst()
        elif index == (self.length -1):
            value = self.removeLast()
        else:
            value = self.peekAny(index)
            curNode = self.head
            for ii in range(index):
                curNode = curNode._next
            curNode._prev._next = curNode._next
            curNode._next._prev = curNode._prev
            curNode = None
            self.length -= 1
        return value
    #END removeAny


#END DSALinkedList

