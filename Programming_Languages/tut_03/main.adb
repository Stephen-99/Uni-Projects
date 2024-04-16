with Ada.Text_IO; use Ada.Text_IO;
with Ada.numerics.discrete_random;

with bubblesort; use bubblesort;

--Starting point of the program. Initialises an array with random integers and calls the 
--bubblesort. Could use generics to make it support floats as well but seems overkill.
procedure main is
    type randRange is new Integer range -100..100;
    package randomInt is new ada.numerics.discrete_random(randRange);
    use randomInt;
    gen : Generator;
    
    numItems : Integer := 100;
    arr : IntArray(0..numItems-1);
begin
    
    --Initialise array with random numbers.
    reset(gen);
    for ii in 0..numItems-1 loop
        arr(ii) := Integer(random(gen));
    end loop;

    Put_Line("Array before sorting:");
    displayArr(arr, numItems);

    sort(arr, numItems);

    New_Line; Put_Line("Array after sorting:");
    displayArr(arr, numItems);
end main;
