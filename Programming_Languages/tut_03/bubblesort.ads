package bubblesort is

    --declares a type of array, an array of integers, with indicies also integers.
    type IntArray is array (Integer range <>) of Integer;

    --swaps 1 value in the array with the next one in the array. No index out of bounds checking.
    procedure swap(arr : in out IntArray; index : in Integer);

    --Performs the bubblesort opperation on the array.
    procedure sort(arr : in out IntArray; arrLen : in Integer);

    --Prints the array out.
    procedure displayArr(arr : in IntArray; arrLen : in Integer);

end bubblesort;