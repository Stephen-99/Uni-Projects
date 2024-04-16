#Just wanted to see how easy it was to do in python :D
NUMTIMES = 100

#evaluated according to the fizzbuzz algorithm
def evaluateNum(num):
    if (num % 15 == 0):
        returnVal = "Fizz Buzz"
    elif (num % 5 == 0):
        returnVal = "Buzz"
    elif (num % 3 == 0):
        returnVal = "Fizz"
    else:
        returnVal = str(num)
    return returnVal

for ii in range(1, NUMTIMES+1):
    print(evaluateNum(ii))
