with Ada.Text_IO; use Ada.Text_IO;
with Ada.Integer_Text_IO; use Ada.Integer_Text_IO;
with Ada.Exceptions; use Ada.Exceptions;

package body bubblesort is

    procedure swap(arr : in out IntArray; 
    index : in Integer)
    is
    begin
        --This works if there is no integer overflow
        arr(index) := arr(index) + arr(index+1);
        arr(index+1) := arr(index) - arr(index+1);
        arr(index) := arr(index) - arr(index+1);

    end swap;
    
    --sorts the array using bubble sort
    procedure sort(arr : in out IntArray; arrLen : in Integer)
    is
        count : Integer := 0;
        unOrdered : Boolean := True;
    begin
        while unOrdered loop
            unOrdered := False;
            for ii in 0 .. (arrLen - count-2) loop
                if (arr(ii) > arr(ii+1)) then
                    swap(arr, ii);
                    unOrdered := True;
                end if;
            end loop; --for loop
            count := count + 1;
        end loop; --while loop
    end sort;

    procedure displayArr(arr : in IntArray; arrLen : in Integer)
    is
    begin
        Put("[");
        for ii in 0..arrLen-2 loop
            Put(arr(ii), Width => 1); Put(", ");
        end loop;
        Put(arr(arrLen-1), Width => 1);
        Put("]"); New_Line;
    end displayArr;

end bubblesort;
