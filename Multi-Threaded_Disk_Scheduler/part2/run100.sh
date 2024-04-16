#!bin/bash

for ii in `seq 1 1 100`;
do
    valgrind ./simulator < runProg.txt > out.txt
    echo "completed iter: "
    echo $ii
done
