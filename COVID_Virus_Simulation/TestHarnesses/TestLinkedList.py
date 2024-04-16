#*********************************************************************
#  FILE: TestLinkedList.java
#  AUTHOR: Valerie Maxville (Python version
#          based on Java version by Connor Beardsmore
#  PURPOSE: Basic Test Harness For Linked List
#  LAST MOD: 01/5/2020 
#  REQUIRES: linkedlists.py - adjust import to match your code
#   NOTE: This was submitted for Practical 4 and is an updated version
#   of the provided test harness
#*********************************************************************
from DSALinkedList import * 
import traceback

numPassed = 0
numTests = 0

ll = None 
sTestString = ""
nodeValue = None

#Test 1 - Constructor
print("\n\nTesting Normal Conditions - Constructor")
print("=======================================")
try:
    numTests += 1
    ll = DSALinkedList()
    print("Testing creation of DSALinkedList (isEmpty()):")
    if (not ll.isEmpty()):
        raise ListError("Head must be None.")
    numPassed += 1
    print("Passed")
except Exception as err:
    print("Failed", err)

     
#Test 2 - Insert First
print("\nTest insert first and remove first - stack behaviour")
print("=======================================")
try:
    numTests += 1
    print("Testing insertFirst():")
    ll.insertFirst("abc")
    ll.insertFirst("jkl")
    ll.insertFirst("xyz")
    numPassed += 1
    print("Passed")
except Exception as err:
    print("Failed", err)


#Test 3 - Peek First
try:
    numTests += 1
    print("Testing peek.First():")
    testString = ll.peekFirst()
    if testString != "xyz":
        raise ListError("Peek First failed")
    numPassed += 1
    print("Passed")
except:
    print("Failed")

#Test 4 - Remove first
try:
    numTests += 1
    print("Testing removeFirst():")
    testString = ll.removeFirst()
    if testString != "xyz":
        raise ListError("Remove first failed")
    testString = ll.removeFirst()
    if testString != "jkl":
        raise ListError("Remove first failed")
    testString = ll.removeFirst()
    if testString != "abc":
        raise ListError("Remove first failed")
    numPassed += 1
    print("Passed")
except:
    print("Failed")

#Test 5 - Remove from empty list
try:
    numTests += 1
    print("Testing removeFirst() when empty")
    testString = ll.removeFirst()
    raise ListError("Remove first when empty failed")
    print("Failed")
except:
    numPassed += 1
    print("Passed")


#Test 6 - Insert Last 
print("\nTest insert last and remove first - queue behaviour")
print("=======================================")
try:
    numTests += 1
    print("Testing insertLast():")
    ll.insertLast("abc")
    ll.insertLast("jkl")
    ll.insertLast("xyz")
    numPassed += 1
    print("Passed")
except:
    print("Failed")

#Test 7 - Peek Last
try:
    numTests += 1
    print("Testing peekFirst():")
    testString = ll.peekFirst()
    if testString != "abc":
        raise ListError("Peek First failed")
    numPassed += 1
    print("Passed")
except:
    print("Failed")

#Test 8 - Remove first
try:
    numTests += 1
    print("Testing removeFirst():")
    testString = ll.removeFirst()
    if testString != "abc":
        raise ListError("Remove first failed")
    testString = ll.removeFirst()
    if testString != "jkl":
        raise ListError("Remove first failed")
    testString = ll.removeFirst()
    if testString != "xyz":
        raise ListError("Remove first failed")
    numPassed += 1
    print("Passed")
except:
    print("Failed")

#Test 9 - Is Empty 2
try:
    numTests += 1
    print("Testing isEmpty when empty")
    testString = ll.removeFirst()
    raise ListError("Remove first when empty failed")
    print("Failed")
except:
    numPassed += 1
    print("Passed")

#Test 10 - iteration
print("\nTesting iteration behaviour")
print("=======================================")
try:
    numTests += 1
    ll.insertFirst("one")
    ll.insertFirst("two")
    ll.insertFirst("three")
    ll.insertFirst("four")

    print("testing next functionality")
    cursor = iter(ll)
    for ii in range(4):
        value = next(cursor)
        print("next value is:", value)

    print("\nusing for loop to display values")
    for item in ll:
        print(item)

    numPassed += 1
except Exception as err:
    print("Failed", err)

#Test 11 - multiple iterators
print("\nTesting with multiple iterators")
try:
    numTests += 1
    ll1 = DSALinkedList()
    ll2 = DSALinkedList()

    for ii in range(10):
        ll1.insertFirst(ii)
        ll2.insertFirst(ii)

    iter1 = iter(ll1)
    iter2 = iter(ll2)
    iter3 = iter(ll2)
    next(iter3)

    for ii in range(9):
        print("iter1 val:", next(iter1))
        print("iter2 val:", next(iter2))
        print("iter3 val:", next(iter3))
        print("length of ll1:", ll1.length)
        print()
    
    numPassed += 1
except Exception as err:
    print("Failed", err)


#Test12 - peekAny
print("Testing the ability to view any element in the list:\n")
numTests += 1
try:
    if ll1.peekAny(0) == 9:
        if ll1.peekAny(3) == 6:
            if ll1.peekAny(9) == 0:
                try:
                    ll1.peekAny(10)
                    print("Failed: didn't stop accessing index beyond list length")
                except ListError as err:
                    print("Erorr msg is: ", err)
                try:
                    ll1.peekAny(-1)
                    print("Failed: didn't stop negative index")
                except ListError as err:
                    print("Error msg is: ", err)
                    print("Passed!")
                    numPassed += 1
                #END try-except
            else:
                print("Failed: didn't produce correct value for last element")
        else:
            print("Failed: didn't produce correct value for index 3")
    else:
        print("Failed: didn't produce correct value for first element")

except Exception as err:
    print("Failed: ", err)
    traceback.print_tb(err.__traceback__)
print("--------------------------------------------")

#Test13 - insertAny
print("Testing ability to insert a value anywhere in the list:\n")
numTests += 1
try:
    ll1.insertAny(111, 0)
    if ll1.peekFirst() == 111:
        ll1.insertAny(121, 11)
        if ll1.peekLast() == 121:
            ll1.insertAny(212, 3)
            if ll1.peekAny(3) == 212:
                try:
                    ll1.insertAny(113, 14)
                    print("Failed: didn't stop value being added outside list index")
                except ListError as err:
                    print("Error msg is:", err)
                try:
                    ll1.insertAny(321, -1)
                    print("Failed: didn't stop insertion at negative index")
                except ListError as err:
                    print("Error msg is:", err)
                    print("Passed!")
                    numPassed += 1
                #END try-except
            else:
                print("Failed: didn't insert at index 3 correctly, expectad value 212, but "\
                + "got value:", ll1.peekAny(3))
        else:
            print("Failed: didn't insert correctly at the end of the list. Expected value 121 "\
            + "but got value:", ll1.peekLast())
    else:
        print("Failed: didn't insert correctly at the beggining of the list. Expected value 111"\
        + " but got value:", ll1.peekFirst())

except Exception as err:
    print("Failed: ", err)
    traceback.print_tb(err.__traceback__)
print("--------------------------------------------")
    

#Test14 - removeAny
print("Testing ability to remove a value anywhere in the list:\n")
numTests += 1
try:
    ll1 = DSALinkedList()
    for ii in range(10):
        ll1.insertLast(ii)
    
    value1 = ll1.removeAny(0)
    if value1 == 0:
        value1 = ll1.removeAny(8)
        if value1 == 9:
            value1 = ll1.removeAny(4)
            if value1 == 5:
                try:
                    ll1.removeAny(10)
                    print("Failed: didn't stop removal outside of list length")
                except ListError as err:
                    print("Error msg was:", err)
                try:
                    ll1.removeAny(-1)
                    print("Failed: didn't stop removal from negative index")
                except ListError as err:
                    print("Error msg was:", err)
                    print("Passed!")
                    numPassed += 1
                #END try-except
            else:
                print("Failed: expected removal from index 4 to be 5, but was:", value1)
        else:
            print("Failed: expected removal from last index to be 9, but was:", value1)
    else:
        print("Failed: expected removal from first index to be 0, but was:", value1)

except Exception as err:
    print("Failed: ", err)
    traceback.print_tb(err.__traceback__)
print("--------------------------------------------")


# Print test summary
print("\nNumber PASSED: ", numPassed, "/", numTests)
print("-> ", numPassed/numTests*100, "%\n")
