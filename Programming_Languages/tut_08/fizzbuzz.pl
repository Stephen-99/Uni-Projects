fizzbuzz(1) :- print('1'), nl, !.
               
fizzbuzz(N) :- N1 is N-1, fizzbuzz(N1),
    X is N mod 15, (X == 0) -> print('fizzbuzz'), nl;
    X is N mod 5, (X == 0) -> print('buzz'), nl;
    X is N mod 3, (X == 0) -> (print('fizz'), nl);
    print(N), nl.


:- initialization(fizzbuzz(1000)).

