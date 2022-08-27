#!/bin/bash

dir=timeSweepResults

mkdir $dir
cp simBaseTime.py $dir
cp shrimp.py $dir
cp sysArgs.py $dir
cp -r pics/ $dir
cd $dir

lowTime=5
hiTime=100
stepTime=5

for ii in `seq $lowTime $stepTime $hiTime`;
do
	python3 simBaseTime.py 50 1400 900 1.3 $ii
	python3 simBaseTime.py 100 1400 900 1.3 $ii
done	
