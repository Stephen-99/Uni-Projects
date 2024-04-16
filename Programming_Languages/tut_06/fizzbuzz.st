Object subclass: #FizzBuzz
    instanceVariableNames: ''
    classVariableNames: ''
    poolDictionaries: ''
    category: nill!

FizzBuzz comment: 'This object is reponsible for executing the fizzbuzz algorithm'!

!FizzBuzz class methodsFor: 'creating a fizzbuzz object'!
new
    | temp |
    temp := super new.
    ^temp
!!

FizzBuzz extend [

    "Evaluate the algorithm on the given number"
    evaluateNum: num [
        | result |
        (num \\ 15) == 0    "\\ is the modulo operator, i.e. remainder after integer division"
            ifTrue: [result:= 'Fizz Buzz']
            ifFalse: [(num \\ 5) == 0
                ifTrue: [result := 'Buzz']
                ifFalse: [(num \\ 3) == 0
                    ifTrue: [result := 'Fizz']
                    ifFalse: [result := num]    "TODO: need to convert to string?"
                ].
            ].
        ^result
    ]

    "Apply the fizzbuzz algorithm to numbers 1-numIter and display results"
    loop: numIter [
        | ii |
        ii := 0.
        numIter timesRepeat: [
            ii := ii + 1.
            (self evaluateNum: ii) displayNl
        ]
    ]
]

"creating a FizzBuzz object"
Smalltalk at: #fizzbuzz put: (FizzBuzz new)!
fizzbuzz loop: 1000!

"Below was added for testing purposes
FizzBuzz extend [
    newMethod: msg [
        msg printNl
    ]
]

fizzbuzz newMethod: 'mah Text here'!"

"Security test...
Integer extend [
    newMethod: msg [
        msg printNl
    ]
]

7 newMethod: 'PRINT ME PLEASE!'!"
