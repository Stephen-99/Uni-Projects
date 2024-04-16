####
# Errors.py
#
# Date created: 16/03/2020
# Date last edited: 16/03/2020
#
# Author: Stephen den Boer  19761257
#
# Overview:
#   This is a file to hold all the custom exceptions for Stacks and queues
#   NOTE the original version of this file was submitted for practical 3
#   It has been updated to accomadate linked lists and graphs and was submitted last for 
#   practical 7, however the latest change was liikely in Practical 6. The date last edited may 
#   also be innacurate
####


#
#STACK ERRORS:
#

class StackError(Exception):
#    def __str__(self):
 #       return "There was an error in the stack!"
    pass
#END StackError

class StackOverflow (StackError):
    def __str__(self):
        msg = "StackOverFlow! Attempted to add an item to an already \
full stack"
        return msg
#END StackOverflow

class StackUnderflow(StackError):
    def __init__(self, message=""):
        self.message = message

    def __str__(self):
        if self.message == "":
            msg = "StackUnderflow! Attempted to remove an item from an \
already empty stack"
        else:
            msg = self.message
        return msg
    pass
#END StackUnderflow


#
#QUEUE ERRORS
#

class QueueError(Exception):
    def __str__(self):
        return "There was an error in the queue!"
#End QueueError

class QueueOverflow(QueueError):
    def __str__(self):
        msg = "QueueOverflow! Attempted to add an item to an already full \
queue"
        return msg
#END QueueOverflow

class QueueUnderflow(QueueError):
    def __str__(self):
        msg = "QueueUnderflow! Attempted to remove an itme from an already \
empty queue"
        return msg
#END QueueUnderflow



#
#LINKED LIST ERRORS
#

class ListError(Exception):
    def __init__(self, message=""):
        self.message = message

    def __str__(self):
        if self.message == "":
            msg = "There was an error with the list!" 
        else:
            msg = self.message
        return msg

#END ListError

class ListUnderflow(ListError):
    def __init__(self, message=""):
        self.message = message

    def __str__(self):
        if self.message == "":
            msg = "ListUnderflow! Attempted to access an item from an already \
empty list"
        else:
            msg = self.message
        return msg
#END ListUnderflow


#
#GRAPH ERRORS
#

class GraphError(Exception):
    def __init__(self, message=""):
        self.message = message

    def __str__(self):
        if self.message == "":
            msg = "There was an error with the graph" 
        else:
            msg = self.message
        return msg

class VertexError(GraphError):
    def __init__(self, message=""):
        self.message = message

    def __str__(self):
        if self.message == "":
            msg = "There was an error with the vertex" 
        else:
            msg = self.message
        return msg

class EdgeError(GraphError):
    def __init__(self, message=""):
        self.message = message

    def __str__(self):
        if self.message == "":
            msg = "There was an error with the edge" 
        else:
            msg = self.message
        return msg

