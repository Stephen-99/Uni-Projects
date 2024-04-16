;A is the data to be sorted. Must be a vector
(define A (make-vector 10 ))
(vector-set! A 0 7)
(vector-set! A 1 3)
(vector-set! A 2 9)
(vector-set! A 3 2)
(vector-set! A 4 6)
(vector-set! A 5 5)
(vector-set! A 6 1)
(vector-set! A 7 0)
(vector-set! A 8 4)
(vector-set! A 9 8)

;Essentially a global flag. ik not very functional programmy of me, 
;but the other way wan't working...
(define swapped)


;A must be a vector.
(define (swap A i1 i2)
    (display "swapping: ")
    (display i1)
    (display " and ")
    (display i2)
    (newline)
    (display "B4: ")
    (display A)
    (newline)


    (let ((temp (vector-ref A i1)))
        (vector-set! A i1 (vector-ref A i2))
        (vector-set! A i2 temp)
    )

    (display "After: ")
    (display A)
    (newline)
    (newline)
)

;Recursive. For each call returns #t if a swap occurred. Results are or'd together
    ;Now it just changes the global flag :C
(define (bubble_forwards A ii)
    
    ;(newline)
    ;(display "In forwards.")
    ;(newline)
    ;(display "ii: ")
    ;(display ii)
    ;(newline)

    ;If not at end of vector yet e:
    (if (< ii (- (vector-length A) 1))
        (begin 
            (if (> (vector-ref A ii) (vector-ref A (+ ii 1)))
                (begin
                    (swap A ii (+ ii 1))
                    (set! swapped #t)
                )
            )
            ;Call again with next increment. Return value of that swap 
            ;and true since we performed a swap
            (bubble_forwards A (+ ii 1))
        )
    )
)
;May not work cos returning values are funny... 

(define (bubble_backwards A ii)
    ;(newline)
    ;(display "In backwards")
    ;(newline)
    ;(display "ii: ")
    ;(display ii)
    ;(newline)

    (if (> ii 0)
        (begin
            (if (< (vector-ref A ii) (vector-ref A (- ii 1)))
                (begin
                    (swap A ii (- ii 1))
                    (set! swapped #t);
                )
            )
            ;Call again with next increment. Return value of that swap 
            ;and true since we performed a swap
            (bubble_backwards A (- ii 1))
        )
    )
)


;A must be a vector.
(define (cocktail_sort A)
    (if (vector? A)
        (begin
            (set! swapped #f)
            (bubble_forwards A 0)
            (if (eq? swapped #t) ;If swaps occured, continue
                (begin
                    (set! swapped #f)
                    (bubble_backwards A (- (vector-length A) 1)) ;if swaps occured, continue
                    (if (eq? swapped #t) ;if swaps occurred, continue
                        (cocktail_sort A)
                    )
                )
            )
        )
    )
)
        


(cocktail_sort A)
(newline)
(display A)
(newline)

