c Stephen den Boer 19761257
c 
c fizzbuzz program in fortran
c 
c The program will go through the numbers 1-100 printing "Fizz Buzz"
c for multiples of 15, "Buzz" for multiples of 5, and "Fizz" for multiples
c of 3. Any other numbers will simply be printed out

      program fizzbuzz

      integer ii 
      
c 9 is the max length the string will be. The length of "Fizz Buzz"
      character response * 9
      ii = 0      

c looping from 1-100 inclusive
      do 10 ii = 1, 100, 1
            call evaluateFizzBuzz(ii, response)
            write(*,*) response
10    continue

      stop
      end


c This function evaluates whether a number should be a fizz, buzz, fizzbuzz, 
c or just the number itself. This evaluation is based on the fizzbuzz algorithm
c outlined at https://en.wikipedia.org/wiki/Fizz_buzz

      subroutine evaluateFizzBuzz(i, result)
      integer i
      character result *9

c Mod is the modulous operator, often represented by %. It returns the
c remainder after dividing the first number by the second

      if (MOD(i, 15) .eq. 0) then
            result = 'Fizz Buzz'
      elseif (MOD(i, 5) .eq. 0) then
            result = 'Buzz'
      elseif (MOD(i, 3) .eq. 0) then
            result = 'Fizz'
      else

c writing the integer to the 'string' because type conversion looks up 
c the ASCII value, and uses that for the 'string'
            write(result, 20) i

c will never be more than 2 digits since 100 will be buzz.
20          format(I2)
      end if

      return
      end